from random_util import random_int
from type_alias import Population, GeneString

def get_selection_strategy(name: str, **kwargs):
    if name == "tournamentSelection":
        tournament_size = kwargs.get("tournament_size", None)
        return lambda population, *args: tournament_select(population, tournament_size, *args)


def tournament_select(population: Population,
                      tournament_size: int,
                      *args) -> GeneString | None:
    best = (None, float("-inf"))

    for count in range(tournament_size):
        pick_idx = random_int(len(population), *args, "selection")
        best = max(best, population[pick_idx], key=lambda x: x[1])

    selected = best[0]
    return None if selected is None else selected.copy()