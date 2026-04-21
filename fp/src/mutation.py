from random_util import random_bernoulli
from type_alias import GeneString

def get_mutation_strategy(name: str, **kwargs):
    if name == "bitFlipMutation":
        prob = kwargs.get("ratePerBit", None)
        chrom_len = kwargs.get("chromosomeLength", None)
        return lambda c, *args: bitflip_mutate(c, chrom_len, prob, *args)


def bitflip_mutate(c: GeneString,
                   chrom_len: int,
                   prob: float,
                   *args) -> GeneString:
    if len(c) != chrom_len:
        raise ValueError(f"Chromosome length {len(c)} does not match expected length {chrom_len}")
    if prob < 0 or prob > 1:
        raise ValueError(f"Mutation probability must be between 0 and 1, got {prob}")
    
    mutated = []
    for i, bit in enumerate(c):
        mutated.append(not bit if random_bernoulli(prob, *args, "mutation", i) else bit)
    return mutated