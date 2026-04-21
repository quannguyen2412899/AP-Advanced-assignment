from type_alias import *
from typing import Callable, Any
from math import sqrt

def get_one_step_GA(fitness: Callable[[GeneString], int],
                    elite: Callable[[Population], Population],
                    select: Callable[[Population, Any], GeneString],
                    crossover: Callable[[GeneString, GeneString, Any], tuple[GeneString, GeneString]],
                    mutate: Callable[[GeneString, Any], GeneString],
                    population_size: int
                    ) -> Callable[[Population], Population]:

    return lambda population, *args:\
           one_step_GA(fitness, elite, select, crossover, mutate, population, population_size, *args)


def one_step_GA(fitness: Callable[[GeneString], int],
                elite: Callable[[Population], list[Chromosome]],
                select: Callable[[Population, Any], GeneString],
                crossover: Callable[[GeneString, GeneString, Any], tuple[GeneString, GeneString]],
                mutate: Callable[[GeneString, Any], GeneString],
                population: Population,
                population_size: int,
                *args
                ) -> Population:
    
    if len(population) != population_size:
        raise ValueError()

    new_population: Population = []

    # Elitism
    elite_group = elite(population)
    new_population += elite_group

    # Reproduction
    harsh_key = 0
    while len(new_population) < population_size:
        # Selection
        p1 = select(population, "p1", harsh_key, *args)
        p2 = select(population, "p2", harsh_key, *args)

        # Crossover
        c1, c2 = crossover(p1, p2, harsh_key, *args)

        # Mutation
        c1 = mutate(c1, "c1", harsh_key, *args)
        c2 = mutate(c2, "c2", harsh_key, *args)

        # Fitness evaluation
        f1 = fitness(c1)
        f2 = fitness(c2)

        # Insertion
        if len(new_population) < population_size:
            new_population.append((c1, f1))
        if len(new_population) < population_size:
            new_population.append((c2, f2))
        
        harsh_key += 1
    
    if len(new_population) != population_size:
        raise ValueError(f"Population size {len(new_population)} does not match expected size {population_size}")
    return new_population


def get_statistics(population: Population, generation: int) -> dict | None:
    size = len(population)
    if size == 0:
        return None
    
    best_sol = None
    min_fit, max_fit = float("inf"), float("-inf")
    total_fit, total_squared_fit = 0, 0

    for c in population:
        sol, fit = c
        if fit < min_fit: min_fit = fit
        if fit > max_fit:
            max_fit = fit
            best_sol = sol
        total_fit += fit
        total_squared_fit += fit**2

    mean_fit = total_fit / size
    if size <= 1:
        variance = 0
    else:
        variance = (total_squared_fit - total_fit**2 / size) / (size - 1)
    std_dev = sqrt(max(variance, 0))

    return {"generation": generation,
            "minFitness": min_fit,
            "maxFitness": max_fit,
            "averageFitness": mean_fit,
            "standardDeviation": std_dev,
            "bestSolution": best_sol}


def execute_GA(one_step: Callable[[Population, Any], Population],
               init_population: Population,
               max_generation: int
               ) -> tuple[GeneString, int, dict]:

    new_population = init_population

    records = []
    optimal_generation = 0
    current_stats = get_statistics(new_population, 0)
    records.append(current_stats)
    best_fitness = current_stats["maxFitness"]

    for generation in range(1, max_generation + 1):
        # Evolution
        new_population = one_step(new_population, generation)

        # Statitics
        current_stats = get_statistics(new_population, generation)
        records.append(current_stats)
        if best_fitness < current_stats["maxFitness"]:
            best_fitness = current_stats["maxFitness"]
            optimal_generation = generation

    all_statistics = {}
    all_statistics["generationOfOptimal"] = optimal_generation
    all_statistics["totalGenerations"] = max_generation
    all_statistics["generations"] = records
    final_best_sol = current_stats["bestSolution"]
    final_best_fit = current_stats["maxFitness"]

    return final_best_sol, final_best_fit, all_statistics