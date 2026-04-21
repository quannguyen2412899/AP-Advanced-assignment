from ga_operators import *

def get_elitism_strategy(name: str, **kwargs):
    if name == "simpleElitism":
        count = kwargs.get("count", None)
        return lambda population: simple_elite_select(population, count)


def get_selection_strategy(name: str, **kwargs):
    if name == "tournamentSelection":
        tournament_size = kwargs.get("tournament_size", None)
        return lambda population, *args: tournament_select(population, tournament_size, *args)


def get_crossover_strategy(name: str, **kwargs):
    if name == "onePointCrossover":
        chrom_len = kwargs.get("chromosomeLength", None)
        prob = kwargs.get("rate", None)
        return lambda p1, p2, *args: onepoint_crossover(p1, p2, chrom_len, prob, *args)


def get_mutation_strategy(name: str, **kwargs):
    if name == "bitFlipMutation":
        prob = kwargs.get("ratePerBit", None)
        chrom_len = kwargs.get("chromosomeLength", None)
        return lambda c, *args: bitflip_mutate(c, chrom_len, prob, *args)

