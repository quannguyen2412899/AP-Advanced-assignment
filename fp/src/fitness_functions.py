from type_alias import GeneString

def get_fitness_function(**kwargs):
    problem = kwargs.get("name")
    if problem == "onemax":
        chrom_len = kwargs.get("chromosomeLength", None)
        return lambda c: onemax_evaluator(c, chrom_len)
    
    if problem == "knapsack":
        chrom_len = kwargs.get("chromosomeLength", None)
        weights = kwargs.get("weights", None)
        values = kwargs.get("values", None)
        capacity = kwargs.get("capacity", None)
        return lambda c: knapsack_evaluator(c, chrom_len, weights, values, capacity)


def onemax_evaluator(c: GeneString, chrom_len: int) -> int:
    if len(c) != chrom_len:
        raise ValueError(f"Chromosome length {len(c)} does not match expected length {chrom_len}")
    return sum(c)


def knapsack_evaluator(c: GeneString,
                       chrom_len: int,
                       weights: list[int],
                       values: list[int],
                       capacity: float) -> int:
    if len(c) != chrom_len:
        raise ValueError(f"Chromosome length {len(c)} does not match expected length {chrom_len}")
    if len(weights) != chrom_len:
        raise ValueError(f"Weights length {len(weights)} does not match chromosome length {chrom_len}")
    if len(values) != chrom_len:
        raise ValueError(f"Values length {len(values)} does not match chromosome length {chrom_len}")
    if any(x < 0 for x in weights):
        raise ValueError()
    if any(x < 0 for x in values):
        raise ValueError()
    
    total_weight = sum(b * w for b, w in zip(c, weights))
    total_value = sum(b * v for b, v in zip(c, values))

    return total_value if total_weight <= capacity else 0