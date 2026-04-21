from random_util import random_int


def get_selection_strategy(name: str, **kwargs):
    if name == "tournamentSelection":
        tournament_size = kwargs.get("tournament_size", None)
        return lambda population, *args: tournament_select(population, tournament_size, *args)


def tournament_select(population: list[tuple[list[bool], int]],
                      tournament_size: int,
                      *args) -> list[bool] | None:
    best = None, float("-inf")

    for count in range(tournament_size):
        pick_idx = random_int(len(population), *args, "selection")
        best = max(best, population[pick_idx], key=lambda x: x[1])

    return best