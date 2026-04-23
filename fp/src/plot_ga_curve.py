import json
import os
import matplotlib.pyplot as plt

def plot(input_file: str, output_file: str) -> None:
    # 1. Read the JSON data
    with open(input_file, 'r') as f:
        data = json.load(f)
    
    optimal_generation = data['generationOfOptimal']
        
    gens = []
    max_fit = []
    avg_fit = []
    std_dev = []
    
    # 2. Extract the data points
    for gen_data in data['generations']:
        gens.append(gen_data['generation'])
        max_fit.append(gen_data['maxFitness'])
        avg_fit.append(gen_data['averageFitness'])
        std_dev.append(gen_data['standardDeviation'])
        
    # 3. Calculate the standard deviation area boundaries
    lower_bound = [a - s for a, s in zip(avg_fit, std_dev)]
    upper_bound = [a + s for a, s in zip(avg_fit, std_dev)]
    
    # 4. Plot exactly what was requested
    plt.figure()
    
    # Red line for max fitness
    plt.plot(gens, max_fit, color='red', label='Population Max Fitness')
    
    # Green line for mean
    plt.plot(gens, avg_fit, color='green', label='Population Average Fitness')
    
    # Transparent area around the green line for standard deviation
    plt.fill_between(gens, lower_bound, upper_bound, color='green', alpha=0.3)
    
    # Vertical dashed line for optimal generation
    plt.axvline(x=optimal_generation, color='black', linestyle='--', linewidth=2, label=f'Optimal Generation ({optimal_generation})')
    
    # Add labels and title
    plt.xlabel('Generation')
    plt.ylabel('Fitness')
    plt.title('Fitness Improvement Over Generations')
    plt.legend()
    plt.grid(True, alpha=0.3)
    
    # 5. Create parent directories if needed and save
    output_dir = os.path.dirname(output_file)
    if output_dir:
        os.makedirs(output_dir, exist_ok=True)
    
    # Save and clear memory
    plt.savefig(output_file)
    plt.close()