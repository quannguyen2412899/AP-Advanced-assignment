from random_util import random_bernoulli


def get_mutation_strategy(name: str, **kwargs):
    if name == "bitFlipMutation":
        prob = kwargs.get("ratePerBit", None)
        return lambda c, *args: bitflip_mutate(c, prob, *args)


def bitflip_mutate(c: list[bool],
                   prob: float,
                   *args) -> list[bool]:
    mutated = []
    for i, bit in enumerate(c):
        mutated.append(not bit if random_bernoulli(prob, *args, "mutation", i) else bit)
    return mutated