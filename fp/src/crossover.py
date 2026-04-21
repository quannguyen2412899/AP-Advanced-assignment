from random_util import random_int, random_bernoulli


def get_crossover_strategy(name: str, kwargs):
    if name == "onePointCrossover":
        prob = kwargs.get("rate", None)
        return lambda p1, p2, *args: onepoint_crossover(p1, p2, prob, *args)


def onepoint_crossover(p1: list[bool],
                       p2: list[bool],
                       prob: float,
                       *args) -> tuple[list[bool]]:
    if len(p1) != len(p2):
        raise ValueError()
    if not random_bernoulli(prob, *args, "crossover", "bernoulli"):
        return p1.copy(), p2.copy()
    
    index = random_int(len(p1) - 1, *args,"crossover", "uniform")
    if index < 0 or index >= len(p1) - 1:
        raise ValueError()    
    
    c1 = p1[:index + 1] + p2[index + 1:]
    c2 = p2[:index + 1] + p1[index + 1:]

    return c1, c2