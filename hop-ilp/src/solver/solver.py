from gurobipy import *

from logger import logger


class Solver(object):

    def __init__(self, name, problem, num_futures, debug=False):
        self.problem = problem
        self.num_futures = num_futures

        self.m = Model(name)
        self.variables, self.states, self.actions = self.add_variables()
        self.add_hop_action_constraints()

        if debug:
            self.m.write('model.lp')

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
        Adds first step constraints on state and action variables
        """
        m = self.m
        variables = self.variables

        first_step_states = self.states.select('*', '*', 0)
        init_values = self.problem.variables
        for v in first_step_states:
            init_value = init_values[v[0]]
            m.addConstr(variables[v] == init_value)

        for a in self.problem.actions:
            first_step_actions = self.actions.select(a, '*', 0)
            for i in range(len(first_step_actions) - 1):
                a1 = variables[first_step_actions[i]]
                a2 = variables[first_step_actions[i + 1]]
                m.addConstr(a1 == a2)

        m.update()
        logger.info('added_hop_action_constraints')