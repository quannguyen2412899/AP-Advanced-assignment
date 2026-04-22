from ga_operators import *

def get_elitism_strategy(**kwargs):
    name = kwargs.get("strategy")
    if name == "simpleElitism":
        count = kwargs.get("count", None)
        return lambda population: simple_elite_select(population, count)


def get_selection_strategy(**kwargs):
    name = kwargs.get("strategy")
    if name == "tournamentSelection":
        tournament_size = kwargs.get("size", None)
        return lambda population, *args: tournament_select(population, tournament_size, *args)


def get_crossover_strategy(**kwargs):
    name = kwargs.get("strategy")
    if name == "onePointCrossover":
        # chrom_len = kwargs.get("chromosomeLength", None)
        prob = kwargs.get("rate", None)
        return lambda p1, p2, *args: onepoint_crossover(p1, p2, prob, *args)


def get_mutation_strategy(**kwargs):
    name = kwargs.get("strategy")
    if name == "bitFlipMutation":
        prob = kwargs.get("ratePerBit", None)
        # chrom_len = kwargs.get("chromosomeLength", None)
        return lambda c, *args: bitflip_mutate(c, prob, *args)

