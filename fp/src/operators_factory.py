from typing import Callable, Any
from ga_operators import *

def get_elitism_strategy(**kwargs) -> Callable[[Population], list[Chromosome]]:
    name = kwargs.get("strategy")
    if name == "simpleElitism":
        count = kwargs.get("count")
        if count is None:
            raise ValueError
        return lambda population: simple_elite_select(population, count)
    
    raise ValueError()


def get_selection_strategy(**kwargs) -> Callable[[Population, Any], GeneString | None]:
    name = kwargs.get("strategy")
    if name == "tournamentSelection":
        tournament_size = kwargs.get("size")
        if tournament_size is None:
            raise ValueError
        return lambda population, *args: tournament_select(population, tournament_size, *args)
    
    raise ValueError()


def get_crossover_strategy(**kwargs) -> Callable[[GeneString, GeneString, Any], tuple[GeneString, GeneString]]:
    name = kwargs.get("strategy")
    if name == "onePointCrossover":
        prob = kwargs.get("rate")
        if prob is None:
            raise ValueError
        return lambda p1, p2, *args: onepoint_crossover(p1, p2, prob, *args)
    
    raise ValueError()


def get_mutation_strategy(**kwargs) -> Callable[[GeneString, Any], GeneString]:
    name = kwargs.get("strategy")
    if name == "bitFlipMutation":
        prob = kwargs.get("ratePerBit", None)
        if prob is None:
            raise ValueError
        return lambda c, *args: bitflip_mutate(c, prob, *args)
    
    raise ValueError()
