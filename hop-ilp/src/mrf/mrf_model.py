from logger import logger
from mrf.mrf_clique import MRFClique


class MRFModel(object):
    """
    MRF Model variables' indices order:
        states[future, time step],
        actions[future, time step],
        rewards[future, time step, path index]
    """
    vars_group_size = 0

    num_state_vars = 0
    num_action_vars = 0
    num_reward_vars = 0
    # Dict of variable names to local indices. These indices are locally
    # offset for each group of variables: states, actions. Rewards variables
    # indices are injected directly into the nodes of the reward tree
    vars_local_indices = {}

    cliques = []
    preamble = 'MARKOV'

    def __init__(self, num_futures, problem):
        self.num_futures = num_futures
        self.problem = problem
        self.vars_group_size = num_futures * problem.horizon
        self.num_reward_vars = self.vars_group_size
        self.map_vars_to_indices(problem)

    def add_states_clique(self, determinized_tree, tree_vars, k, t, v):
        """
        Adds a clique i.e. a potential function that represents a determinized transition tree.
        The function maps to 1 or 0 whether the values assigned to the variables in the tree
        satisfies the determinized value or not respectively
        :param determinized_tree: a determinized transition tree
        :param tree_vars: all variables in the transition tree
        :param k: the future value
        :param t: the horizon step
        :param v: name of state variable to make the transition to
        """
        var_indices = self.state_vars_to_indices(tree_vars, k, t)
        clique = MRFClique(var_indices)
        clique.generate_states_function_table(determinized_tree, tree_vars, v)
        self.cliques.append(clique)

    def add_reward_cliques(self, reward_tree, tree_vars):
        assert(self.num_futures > 0 and self.problem.horizon > 0)
        var_indices = self.state_vars_to_indices(tree_vars, 0, 0)
        var_indices.append(self.get_reward_var_index(0, 0))
        clique_proto = MRFClique(var_indices)
        clique_proto.generate_reward_function_table(reward_tree, tree_vars)
        self.cliques.append(clique_proto)

        for k in range(self.num_futures):
            for t in range(self.problem.horizon):
                if k == 0 and t == 0:
                    continue
                var_indices = self.state_vars_to_indices(tree_vars, k, t)
                var_indices.append(self.get_reward_var_index(k, t))
                clique = MRFClique(var_indices)
                clique.function_table = clique_proto.function_table
                self.cliques.append(clique)

    def add_init_states_constrs_cliques(self):
        vars_list = list(self.problem.variables)
        vars_vals_bitset = 0
        for i, v in enumerate(vars_list):
            vars_vals_bitset |= (int(self.problem.variables[v]) << i)

        for k in range(self.num_futures):
            vars_indices = self.state_vars_to_indices(vars_list, k, 0)
            clique = MRFClique(vars_indices)
            for i in range(2**len(vars_list)):
                if i == vars_vals_bitset:
                    clique.function_table.append(1)
                else:
                    clique.function_table.append(0)

            self.cliques.append(clique)
        logger.info('added_init_states_cliques|cur_num_cliques={}'.format(len(self.cliques)))

    def add_init_actions_constrs_cliques(self):
        pass

    def state_vars_to_indices(self, vars, k, t):
        return [self.get_state_var_index(var, k, t) for var in vars]

    def map_vars_to_indices(self, problem):
        self.num_state_vars = len(problem.variables)
        self.num_action_vars = len(problem.actions)

        for i, v in enumerate(problem.variables):
            self.vars_local_indices[v] = i
        for i, a in enumerate(problem.actions):
            self.vars_local_indices[a] = i + self.num_state_vars

    def get_state_var_index(self, var_name, future, horizon):
        local_index = self.vars_local_indices[var_name]
        return local_index * self.vars_group_size + future * self.problem.horizon + horizon

    def get_reward_var_index(self, future, horizon):
        local_index = future * self.problem.horizon + horizon
        return (self.num_state_vars + self.num_action_vars) * self.vars_group_size + local_index

    def to_file(self, filename):
        # TODO: DRY
        num_state_action_vars = (self.num_state_vars + self.num_action_vars) * self.vars_group_size
        num_reward_vars = self.num_reward_vars * self.vars_group_size

        with open(filename, 'w') as f:
            write_line(f, self.preamble)
            write_line(f, num_state_action_vars + num_reward_vars)
            write_line(f, ' '.join(['2'] * num_state_action_vars + ['1'] * num_reward_vars))

            write_line(f, (len(self.cliques)))
            for clique in self.cliques:
                write_line(f, ' '.join(stringify(clique.vars[::-1])))
                write_line(f, ' '.join(stringify(clique.function_table)))

            logger.info('write_model_to_file|f={}'.format(filename))


def stringify(l):
    return [str(x) for x in l]


def write_line(f, l):
    f.write('{}\n'.format(l))
