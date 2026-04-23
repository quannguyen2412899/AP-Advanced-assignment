from typing import Callable, Any
from type_alias import *
from random_util import random_gen, random_int, random_bernoulli
import heapq

def simple_elite_select(population: Population,
                        count: int) -> list[Chromosome]:
    if count >= len(population):
        return sorted(population, key=lambda x: x[1], reverse=True)
    
    # Use heapq.nlargest to efficiently get top k elements
    top_k = heapq.nlargest(count, population, key=lambda x: x[1])
    return [(genestring.copy(), fitness) for genestring, fitness in top_k]


def tournament_select(population: Population,
                      tournament_size: int,
                      *args,
                      rand_eng: Callable[[Any], int] = random_gen
                      )-> GeneString | None:
    if tournament_size == 0:
        return None
    
    pool = map(
        lambda count: population[random_int(len(population), *args, "selection", count, random_engine=rand_eng)],
        range(tournament_size)
    )
    selected, _ = max(pool, key=lambda x: x[1])
    return selected.copy()


def onepoint_crossover(p1: GeneString,
                       p2: GeneString,
                       prob: float,
                       *args,
                       rand_eng: Callable[[Any], int] = random_gen
                       ) -> tuple[GeneString]:
    if len(p1) != len(p2):
        raise ValueError()
    if not random_bernoulli(prob, *args, "crossover", "bernoulli", random_engine=rand_eng):
        return p1.copy(), p2.copy()
    
    index = random_int(len(p1) - 1, *args, "crossover", "uniform", random_engine=rand_eng)
    if index < 0 or index >= len(p1) - 1:
        raise ValueError()    
    
    c1 = p1[:index + 1] + p2[index + 1:]
    c2 = p2[:index + 1] + p1[index + 1:]

    return c1, c2


def bitflip_mutate(c: GeneString,
                   prob: float,
                   *args,
                   rand_eng: Callable[[Any], int] = random_gen
                   ) -> GeneString:
    if prob < 0 or prob > 1:
        raise ValueError(f"Mutation probability must be between 0 and 1, got {prob}")
    mutated = list(map(
        lambda i: not c[i] if random_bernoulli(prob, *args, "mutation", i, random_engine=rand_eng) else c[i],
        range(len(c))
    ))
    return mutated