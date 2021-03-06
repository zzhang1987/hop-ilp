from gurobipy import *
import random

from logger import logger
cimport utils


class Solver(object):

    def __init__(self, name, problem, num_futures, time_limit=None, debug=False):
        self.problem_name = name
        self.problem = problem
        self.num_futures = num_futures

        self.m = Model(name)
        self.variables, self.states, self.actions = self.add_variables()
        self.init_constrs = self.add_init_states_constraints()
        self.add_hop_action_constraints()
        self.add_hop_quality_criterion()
        self.add_concurrency_constraints()
        self.intermediate_vars = []
        self.transition_constrs = self.add_transition_constraints()

        if time_limit:
            self.m.params.timeLimit = time_limit

        if debug:
            self.m.update()
            self.m.write('model.lp')
        else:
            self.m.setParam(GRB.Param.OutputFlag, 0)

    def solve(self):
        logger.info('optimizing_model')
        self.m.optimize()

        if self.m.Status == GRB.Status.INFEASIBLE or self.m.Status == GRB.Status.INF_OR_UNBD:
            raise Exception('Failed to find solution')

        if self.m.Status == GRB.Status.TIME_LIMIT:
            logger.info('time_limit_exceeded')

        cdef dict suggested_actions = {}
        for a in self.actions.select('*', 0, 0):
            suggested_actions[a[0]] = int(self.variables[a].X)

        return suggested_actions, self.m.objVal

    def init_next_step(self, dict states_init_values):
        """
        Reinitialize the solver with a new set of states' values and
        prepare the transitions determinization for the next time step
        """
        for constr in self.init_constrs:
            self.m.remove(constr)
        self.problem.variables.update(states_init_values)
        self.init_constrs = self.add_init_states_constraints()

        for c in self.transition_constrs:
            self.m.remove(c)
            self.transition_constrs = []
        for v in self.intermediate_vars:
            self.m.remove(v)
            self.intermediate_vars = []
        self.transition_constrs = self.add_transition_constraints()

        logger.info("reinitialized_model")

    def add_transition_constraints(self):
        random.seed()
        m = self.m
        cdef int horizon = self.problem.horizon
        cdef int num_futures = self.num_futures
        cdef dict transition_trees = self.problem.transition_trees
        cdef list intermediate_vars = self.intermediate_vars
        cdef list transition_constrs = []
        cdef int k, t
        cdef unicode v

        for k in range(num_futures):
            for t in range(horizon - 1):
                for v in transition_trees:
                    path_vars = self.determinize_paths(transition_trees[v],
                                                       k, t, v,
                                                       transition_constrs)
                    intermediate_vars.extend(path_vars)

                    i, constrs = self.combine_paths(k, t, v, path_vars)
                    intermediate_vars.append(i)
                    transition_constrs.extend(constrs)

                    next_step_var = self.variables[v, k, t + 1]
                    constr_name = 'trans_{}_{}_{}'.format(v, k, t)
                    constr = m.addConstr(next_step_var == i, name=constr_name)
                    transition_constrs.append(constr)

        return transition_constrs

    def determinize_paths(self, transition_tree, int k, int t, unicode v, list transition_constrs):
        m = self.m
        cdef list path_vars = []
        cdef list p = [0]

        def determinize_path(list nodes):
            if random.random() > nodes[-1][1]:
                return

            cdef str name = 'f_{}_{}_{}_{}'.format(v, k, t, p[0])
            i = m.addVar(vtype=GRB.BINARY, name=name)
            path_vars.append(i)
            m.update()

            cdef list signed_vars = self.tree_path_to_signed_vars(nodes, k, t)
            cdef tuple constrs = utils.add_and_constraints(m, signed_vars, i, name=name)
            transition_constrs.extend(constrs)

            p[0] += 1

        transition_tree.traverse_paths(determinize_path, [])
        return path_vars

    def combine_paths(self, int k, int t, unicode v, list path_vars):
        name = 'fs_{}_{}_{}'.format(v, k, t)
        i = self.m.addVar(vtype=GRB.BINARY, name=name)
        self.m.update()

        cdef tuple constrs = utils.add_or_constraints(self.m, path_vars, i, name=name)

        return i, constrs

    def add_hop_quality_criterion(self):
        cdef int num_futures = self.num_futures
        m = self.m
        p = [0]

        def paths_handler(nodes):
            def subpaths_handler(all_nodes):
                cdef int k, t
                for k in range(num_futures):
                    for t in range(self.problem.horizon):
                        coeff = 1./num_futures * all_nodes[-1][1]
                        name = 'w_{}_{}_{}'.format(k, t, p[0])
                        v = m.addVar(vtype=GRB.BINARY, obj=coeff, name=name)
                        m.update()

                        signed_vars = self.tree_path_to_signed_vars(all_nodes,
                                                                    k, t)
                        constr_name = 'reward_{}_{}_{}'.format(k, t, p[0])
                        utils.add_and_constraints(m, signed_vars, v,
                                                  name=constr_name)
                p[0] += 1

            for subtree in nodes[-1][1]:
                subtree.traverse_paths(subpaths_handler, nodes[:-1])

        self.problem.reward_tree.traverse_paths(paths_handler, [])

        m.ModelSense = GRB.MAXIMIZE
        logger.info('added_hop_quality_criterion')

    def tree_path_to_signed_vars(self, list nodes, int k, int t):
        """
        Returns of list of (variable, sign) from a list of nodes along
        a decision tree path. `sign` has the same value as the value
        associated with each node

        :param nodes: list of (node name, value) on a decision path
        :param k: the future index
        :param t: the horizon index
        """
        return [(self.variables[name, k, t], val) for (name, val) in nodes[:-1]]

    def add_variables(self):
        """
        Adds variables to the LP model. Variables include both
        state variables and action variables
        :return : (all variables, states tuplelist, actions tuplelist)
        """
        if self.problem.horizon is None:
            raise AttributeError('missing horizon attribute in problem')

        horizon = self.problem.horizon
        m = self.m
        variables = {}
        states = tuplelist()
        actions = tuplelist()

        for v in self.problem.variables:
            for k in range(self.num_futures):
                for t in range(horizon):
                    lp_var = m.addVar(vtype=GRB.BINARY,
                                      name='%s_%d_%d' % (v, k, t))
                    variables[v, k, t] = lp_var
                    states.append((v, k, t))

        for a in self.problem.actions:
            for k in range(self.num_futures):
                for t in range(horizon):
                    lp_var = m.addVar(vtype=GRB.BINARY,
                                      name='%s_%d_%d' % (a, k, t))
                    variables[a, k, t] = lp_var
                    actions.append((a, k, t))

        m.update()
        logger.info('added_variables|num_vars=%d' % len(variables))
        return variables, states, actions

    def add_hop_action_constraints(self):
        """
        Adds first step constraints on action variables
        """
        m = self.m
        variables = self.variables
        init_constrs = []

        for a in self.problem.actions:
            first_step_actions = self.actions.select(a, '*', 0)
            for i in range(len(first_step_actions) - 1):
                a1 = variables[first_step_actions[i]]
                a2 = variables[first_step_actions[i + 1]]
                constr_name = 'act_{}_{}'.format(a, i)
                m.addConstr(a1 == a2, name=constr_name)

        logger.info('added_hop_action_constraints')
        return init_constrs

    def add_init_states_constraints(self):
        """
        Adds first step constraints on state variables
        """
        m = self.m
        cdef dict variables = self.variables
        cdef list init_constrs = []

        first_step_states = self.states.select('*', '*', 0)
        cdef dict init_values = self.problem.variables
        for v in first_step_states:
            init_value = init_values[v[0]]
            constr_name = 'init_{}_{}'.format(v[0], v[1])
            constr = m.addConstr(variables[v] == init_value, name=constr_name)
            init_constrs.append(constr)

        return init_constrs

    def add_concurrency_constraints(self):
        horizon = self.problem.horizon
        max_concurrency = self.problem.max_concurrency

        if max_concurrency == len(self.problem.actions):
            return

        for k in range(self.num_futures):
            for t in range(horizon):
                s = quicksum([self.variables[a]
                              for a in self.actions.select('*', k, t)])
                self.m.addConstr(s <= max_concurrency,
                                 'maxcon_{}_{}'.format(k, t))

        logger.info('added_max_concurrency_constraints')
