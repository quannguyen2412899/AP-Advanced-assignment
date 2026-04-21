import json
import random
import os

def generate_knapsack_problem():
    """
    Generate a knapsack problem configuration with random weights and values.
    Saves to knapsack.json in the same directory.
    """
    # Define configuration independently
    chromosome_length = 100
    population_size = 100
    max_generations = 300
    random_seed = 42
    
    # Generate random weights and values in range [1, 50]
    weights = [random.randint(1, 50) for _ in range(chromosome_length)]
    values = [random.randint(1, 50) for _ in range(chromosome_length)]
    
    # Create knapsack problem configuration
    knapsack_config = {
        "populationSize": population_size,
        "chromosomeLength": chromosome_length,
        "maxGenerations": max_generations,
        "randomSeed": random_seed,
        "selection": {
            "strategy": "tournamentSelection",
            "size": 3
        },
        "crossover": {
            "strategy": "onePointCrossover",
            "rate": 0.9
        },
        "mutation": {
            "strategy": "bitFlipMutation",
            "ratePerBit": 0.01
        },
        "elitismStrategy": {
            "strategy": "simpleElitism",
            "count": 2
        },
        "problem": {
            "name": "knapsack",
            "weights": weights,
            "values": values,
            "capacityOverTotal": 0.4
        }
    }
    
    # Save to knapsack.json
    output_path = os.path.join(os.path.dirname(__file__), 'knapsack.json')
    with open(output_path, 'w') as f:
        json.dump(knapsack_config, f, indent=4)
    
    print(f"Generated knapsack problem configuration: {output_path}")
    print(f"Chromosome length: {chromosome_length}")
    print(f"Population size: {population_size}")
    print(f"Max generations: {max_generations}")
    print(f"Weights range: [1, 50]")
    print(f"Values range: [1, 50]")
    print(f"Capacity over total: 0.4")

if __name__ == "__main__":
    # Set seed for reproducibility (optional)
    random.seed(42)
    generate_knapsack_problem()
