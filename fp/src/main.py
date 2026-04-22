import sys
import json
import time
from pathlib import Path
from type_alias import *
from random_util import random_bernoulli
from genetic_algorithm import get_one_step_GA, execute_GA
from fitness_functions import get_fitness_function
from operators_factory import *


def read_config(config_path: str) -> dict:
    """
    Read and parse the JSON configuration file.
    """
    try:
        with open(config_path, 'r') as f:
            config = json.load(f)
        return config
    except FileNotFoundError:
        print(f"Error: Configuration file '{config_path}' not found.")
        sys.exit(1)
    except json.JSONDecodeError:
        print(f"Error: Invalid JSON in configuration file '{config_path}'.")
        sys.exit(1)

    
def init_GA_param(config: dict) -> tuple[Callable[[Population], Population], Population, int]:
    # Extract configuration parameters
    population_size = config.get("populationSize")
    chromosome_length = config.get("chromosomeLength")
    max_generations = config.get("maxGenerations")
    random_seed = config.get("randomSeed")
    
    selection_config = config.get("selection", {})
    crossover_config = config.get("crossover", {})
    mutation_config = config.get("mutation", {})
    elitism_config = config.get("elitismStrategy", {})
    problem_config = config.get("problem", {})
    
    # Validate required parameters
    if not all([population_size, chromosome_length, max_generations, selection_config,
                crossover_config, mutation_config, elitism_config, problem_config]):
        print("Error: Missing required configurations.")
        sys.exit(1)

    # Add chromosome length to configs for validation
    # selection_config_with_len = {**selection_config, "chromosomeLength": chromosome_length}
    # crossover_config_with_len = {**crossover_config, "chromosomeLength": chromosome_length}
    # mutation_config_with_len = {**mutation_config, "chromosomeLength": chromosome_length}

    fitness_func = get_fitness_function(**problem_config, chromosomeLength=chromosome_length)
    elitism = get_elitism_strategy(**elitism_config)
    selection = get_selection_strategy(**selection_config)
    crossover = get_crossover_strategy(**crossover_config)
    mutation = get_mutation_strategy(**mutation_config)

    onestep = get_one_step_GA(fitness_func, elitism, selection, crossover, mutation, population_size, random_seed)
    init_population = init_random_population(population_size, chromosome_length, fitness_func, random_seed)

    return onestep, init_population, max_generations


def init_random_population(population_size: int,
                           chromosome_len: int,
                           fitness_func: Callable[[GeneString], int],
                           random_seed: int
                           ) -> Population:
    population = []
    for i in range(population_size):
        bitstring = []
        for j in range(chromosome_len):
            bitstring.append(random_bernoulli(0.5, random_seed, "init", i, j))
        population.append((bitstring, fitness_func(bitstring)))
    return population


def main(args):
    """
    Usage: python main.py --config <input json path> --out <output json path>
    """
    # Parse command line arguments
    if len(args) != 4 or args[0] != "--config" or args[2] != "--out":
        print("Usage: python main.py --config <input json path> --out <output json path>")
        sys.exit(1)
    
    config_path = args[1]
    output_path = args[3]
    
    # Read configuration from JSON file
    config = read_config(config_path)
    
    # Initialization
    onestep, init_population, max_generation = init_GA_param(config)

    # Execution
    start_time = time.time()
    final_best_chrom, final_best_fit, stats = execute_GA(onestep, init_population, max_generation)
    end_time = time.time()
    execution_time = (end_time - start_time) * 1000

    # Export result
    print("Problem: " + config_path)
    print("Final best fitness: " + str(final_best_fit))
    print("Execution time: " + str(execution_time) + " ms")
    with open(output_path, 'w') as f:
        json.dump(stats, f, indent=4)


if __name__ == "__main__":
    main(sys.argv[1:])