from random_util import random_int, random_bernoulli
from type_alias import GeneString

def get_crossover_strategy(name: str, **kwargs):
    if name == "onePointCrossover":
        chrom_len = kwargs.get("chromosomeLength", None)
        prob = kwargs.get("rate", None)
        return lambda p1, p2, *args: onepoint_crossover(p1, p2, chrom_len, prob, *args)


def onepoint_crossover(p1: GeneString,
                       p2: GeneString,
                       chrom_len: int,
                       prob: float,
                       *args) -> tuple[GeneString]:
    if chrom_len < 0:
        raise ValueError()
    if len(p1) != chrom_len:
        raise ValueError()
    if len(p2) != chrom_len:
        raise ValueError()
    if not random_bernoulli(prob, *args, "crossover", "bernoulli"):
        return p1.copy(), p2.copy()
    
    index = random_int(len(p1) - 1, *args, "crossover", "uniform")
    if index < 0 or index >= len(p1) - 1:
        raise ValueError()    
    
    c1 = p1[:index + 1] + p2[index + 1:]
    c2 = p2[:index + 1] + p1[index + 1:]

    return c1, c2