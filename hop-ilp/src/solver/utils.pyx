from gurobipy import LinExpr, quicksum


cdef tuple add_and_constraints(model, list xs, z, str name):
    """
    Adds constraints to Gurobi model such that z is equivalent
    to the logic AND of variables in xs using the rule:
    * nz <= (x1 + x2 + ... + xn) <= (n - 1) + z

    :param model: Gurobi model
    :param xs: list of variables or (variable, sign)
    :param z:
    :param name: constraints' prefix
    """

    sum_expr = sum_vars(xs)

    cdef int n = len(xs)
    cdef str name_lb = "{}_lb".format(name) if name else ""
    cdef str name_ub = "{}_ub".format(name) if name else ""
    lb_cons = model.addConstr(n * z <= sum_expr, name_lb)
    up_cons = model.addConstr(sum_expr <= (n - 1) + z, name_ub)

    return lb_cons, up_cons


cdef tuple add_or_constraints(model, list xs, z, str name):
    """
    Adds and constraints to Gurobi model such that z is equivalent
    to the logic OR of variables in xs using the rule:
    * z <= (x1 + x2 + ... + xn) <= nz

    :param model: Gurobi model
    :param xs: list of variables or (variable, sign)
    :param z:
    :param name: constraints' prefix
    """

    sum_expr = sum_vars(xs)

    cdef int n = len(xs)
    cdef str name_lb = "{}_lb".format(name) if name else ""
    cdef str name_ub = "{}_ub".format(name) if name else ""
    lb_cons = model.addConstr(z <= sum_expr, name_lb)
    ub_cons = model.addConstr(sum_expr <= n * z, name_ub)

    return lb_cons, ub_cons


cdef object sum_vars(list xs):
    """
    Returns a Gurobi's LinExpr that represents the sum of variables in xs

    :param xs: list of variables or (variable, sign)
    """

    sum_expr = LinExpr()
    cdef int sign

    if len(xs) == 0:
        return sum_expr

    if isinstance(xs[0], tuple):
        for x, sign in xs:
            if sign == 1:
                sum_expr += x
            elif sign == -1:
                sum_expr += (1 - x)

        return sum_expr

    return quicksum(xs)
