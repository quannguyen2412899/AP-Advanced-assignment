def get_fitness_function(problem: str, **kwargs):
    if problem == "onemax":
        return onemax_evaluator
    
    if problem == "knapsack":
        weights = kwargs.get("weights", None)
        values = kwargs.get("values", None)
        capacity = kwargs.get("capacity", None)
        return lambda c: knapsack_evaluator(c, weights, values, capacity)


def onemax_evaluator(c: list[bool]) -> int:
    return sum(c)


def knapsack_evaluator(c: list[bool],
                       weights: list[int],
                       values: list[int],
                       capacity: float) -> int:
    if any(x < 0 for x in weights):
        raise ValueError()
    if any(x < 0 for x in values):
        raise ValueError()
    
    total_weight = sum(b * w for b, w in zip(c, weights))
    total_value = sum(b * v for b, v in zip(c, values))

    return total_value if total_weight <= capacity else 0