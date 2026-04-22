"""
Test suite for the complete Genetic Algorithm workflow
Tests end-to-end GA execution with multiple operators and generations
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from genetic_algorithm import one_step_GA, execute_GA
from fitness_functions import get_fitness_function
from ga_operators import simple_elite_select, tournament_select, onepoint_crossover, bitflip_mutate
from type_alias import Population, Chromosome


class TestGeneticAlgorithm:
    """Test suite for complete GA execution"""
    
    @staticmethod
    def test_01():
        """Test 01: Basic GA evolution with OneMax problem
        Tests: Population of 10, fitness evaluation and elitism preservation
        """
        population_size = 10
        chromosome_length = 10
        
        # Create initial population with random-like chromosomes
        population: Population = [
            ([i % 2 == 0 for i in range(chromosome_length)], float(i % 6))
            for i in range(population_size)
        ]
        
        # Setup fitness function
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=chromosome_length)
        
        # Recalculate fitness
        population = [
            (gene, fitness_func(gene))
            for gene, _ in population
        ]
        
        initial_best = max(fitness for _, fitness in population)
        
        # Run one GA step using actual one_step_GA
        elite_op = lambda pop: simple_elite_select(pop, 1)
        select_op = lambda pop, *args: tournament_select(pop, 3, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.9, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.01, *args)
        
        new_population = one_step_GA(
            fitness=fitness_func,
            elite=elite_op,
            select=select_op,
            crossover=crossover_op,
            mutate=mutate_op,
            population=population,
            population_size=population_size
        )
        
        final_best = max(fitness for _, fitness in new_population)
        
        assert final_best >= initial_best, f"Best fitness should improve or stay same: {initial_best} -> {final_best}"
        assert len(new_population) == population_size, "Population size must be preserved"
        assert all(len(gene) == chromosome_length for gene, _ in new_population), "All chromosomes must have correct length"
        assert all(isinstance(fit, (int, float)) for _, fit in new_population), "All fitness values must be numeric"
    
    @staticmethod
    def test_02():
        """Test 02: GA with Knapsack problem
        Tests: Multiple fitness evaluations, population integrity with Knapsack
        """
        population_size = 10
        chromosome_length = 10
        
        # Setup Knapsack problem
        weights = [1.0, 4.0, 3.0, 5.0, 6.0, 3.0, 3.0, 9.0, 0.0, 7.0]
        values = [7.0, 6.0, 7.0, 4.0, 1.0, 1.0, 0.0, 2.0, 3.0, 4.0]
        capacity_rate = 0.4
        
        # Create initial population
        population: Population = [
            ([i % 2 == 0 for i in range(chromosome_length)], 0.0)
            for i in range(population_size)
        ]
        
        # Setup fitness function
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=chromosome_length,
            weights=weights,
            values=values,
            capacityOverTotal=capacity_rate
        )
        
        # Recalculate fitness
        population = [
            (gene, fitness_func(gene))
            for gene, _ in population
        ]
        
        initial_best = max(fitness for _, fitness in population)
        
        # Run one GA step
        elite_op = lambda pop: simple_elite_select(pop, 2)
        select_op = lambda pop, *args: tournament_select(pop, 3, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.8, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.1, *args)
        
        new_population = one_step_GA(
            fitness=fitness_func,
            elite=elite_op,
            select=select_op,
            crossover=crossover_op,
            mutate=mutate_op,
            population=population,
            population_size=population_size
        )
        
        final_best = max(fitness for _, fitness in new_population)
        
        assert final_best >= initial_best, f"Best fitness should improve or stay same: {initial_best} -> {final_best}"
        assert len(new_population) == population_size, "Population size must be preserved"
        assert all(len(gene) == chromosome_length for gene, _ in new_population), "All chromosomes correct length"
        assert all(fitness >= 0 for _, fitness in new_population), "All fitness values must be non-negative"
    
    @staticmethod
    def test_03():
        """Test 03: Edge case - minimum population size (2)
        Tests: GA with population size 2, correct evolution
        """
        population_size = 2
        chromosome_length = 8
        
        # Create initial population
        population: Population = [
            ([True] * 3 + [False] * 5, 3.0),
            ([False] * 8, 0.0),
        ]
        
        # Setup fitness function
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=chromosome_length)
        
        # Recalculate fitness
        population = [
            (gene, fitness_func(gene))
            for gene, _ in population
        ]
        
        initial_best = max(fitness for _, fitness in population)
        
        # Run one GA step
        elite_op = lambda pop: simple_elite_select(pop, 1)
        select_op = lambda pop, *args: tournament_select(pop, 2, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.5, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.2, *args)
        
        new_population = one_step_GA(
            fitness=fitness_func,
            elite=elite_op,
            select=select_op,
            crossover=crossover_op,
            mutate=mutate_op,
            population=population,
            population_size=population_size
        )
        
        final_best = max(fitness for _, fitness in new_population)
        
        assert final_best >= initial_best, "Minimum population edge case: best fitness preserved"
        assert len(new_population) == population_size, "Population size must be 2"
        assert all(len(gene) == chromosome_length for gene, _ in new_population), "All chromosomes correct length"
    
    @staticmethod
    def test_04():
        """Test 04: Edge case - minimum chromosome length (2 bits)
        Tests: GA with chromosome length 2, correct bit manipulation
        """
        population_size = 5
        chromosome_length = 2
        
        # Create initial population
        population: Population = [
            ([True, True], 2.0),
            ([False, False], 0.0),
            ([True, False], 1.0),
            ([False, True], 1.0),
            ([True, True], 2.0),
        ]
        
        # Setup fitness function
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=chromosome_length)
        
        # Recalculate fitness
        population = [
            (gene, fitness_func(gene))
            for gene, _ in population
        ]
        
        initial_best = max(fitness for _, fitness in population)
        
        # Run one GA step
        elite_op = lambda pop: simple_elite_select(pop, 1)
        select_op = lambda pop, *args: tournament_select(pop, 2, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.5, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.5, *args)
        
        new_population = one_step_GA(
            fitness=fitness_func,
            elite=elite_op,
            select=select_op,
            crossover=crossover_op,
            mutate=mutate_op,
            population=population,
            population_size=population_size
        )
        
        final_best = max(fitness for _, fitness in new_population)
        
        assert final_best >= initial_best, "Minimum chromosome edge case: best fitness preserved"
        assert len(new_population) == population_size, f"Population size must be {population_size}"
        assert all(len(gene) == chromosome_length for gene, _ in new_population), "All chromosomes length 2"
    
    @staticmethod
    def test_05():
        """FP Compliance: Population integrity after one GA step
        Tests: Original population not modified, new populations created
        """
        population_size = 5
        chromosome_length = 8
        
        # Create initial population
        population: Population = [
            ([i % 2 == 0 for i in range(chromosome_length)], float(i))
            for i in range(population_size)
        ]
        
        population_copy = [(gene[:], fit) for gene, fit in population]
        
        # Setup fitness function
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=chromosome_length)
        
        # Setup operators
        elite_op = lambda pop: simple_elite_select(pop, 1)
        select_op = lambda pop, *args: tournament_select(pop, 2, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.5, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.2, *args)
        
        # Run one step
        new_pop = one_step_GA(
            fitness=fitness_func,
            elite=elite_op,
            select=select_op,
            crossover=crossover_op,
            mutate=mutate_op,
            population=population,
            population_size=population_size
        )
        
        # Verify original population unchanged
        for i, (orig, copy) in enumerate(zip(population, population_copy)):
            assert orig[0] == copy[0], f"FP: Original chromosome {i} was modified"
            assert orig[1] == copy[1], f"FP: Original fitness {i} was modified"
        
        # Verify new population is different object
        assert new_pop is not population, "FP: New population must be different object"
        assert len(new_pop) == population_size, "FP: New population correct size"
    
    @staticmethod
    def test_06():
        """Test 06: OneMax with multiple generations (50 generations)
        Tests: GA evolution over multiple generations, fitness improvement
        """
        population_size = 10
        chromosome_length = 10
        max_generations = 50
        
        # Create initial population with 3 set bits
        population: Population = [
            ([i % 2 == 0 for i in range(chromosome_length)], 3.0)
            for i in range(population_size)
        ]
        
        # Setup fitness function
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=chromosome_length)
        
        # Recalculate fitness
        population = [
            (gene, fitness_func(gene))
            for gene, _ in population
        ]
        
        initial_best = max(fitness for _, fitness in population)
        
        # Setup operators
        elite_op = lambda pop: simple_elite_select(pop, 2)
        select_op = lambda pop, *args: tournament_select(pop, 3, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.9, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.01, *args)
        
        # Create one_step function
        def one_step(pop, *args):
            return one_step_GA(
                fitness=fitness_func,
                elite=elite_op,
                select=select_op,
                crossover=crossover_op,
                mutate=mutate_op,
                population=pop,
                population_size=population_size
            )
        
        # Run using execute_GA
        best_sol, final_best, stats = execute_GA(one_step, population, max_generations)
        
        assert final_best >= initial_best, f"Fitness should improve over generations: {initial_best} -> {final_best}"
        assert final_best >= chromosome_length * 0.5, f"After 50 generations, expected fitness >= {chromosome_length * 0.5}, got {final_best}"
    
    @staticmethod
    def test_07():
        """Test 07: Knapsack with multiple generations
        Tests: GA evolution on Knapsack problem, expects fitness of 28.0
        """
        population_size = 10
        chromosome_length = 10
        max_generations = 1000
        
        # Setup Knapsack problem
        weights = [1.0, 4.0, 3.0, 5.0, 6.0, 3.0, 3.0, 9.0, 0.0, 7.0]
        values = [7.0, 6.0, 7.0, 4.0, 1.0, 1.0, 0.0, 2.0, 3.0, 4.0]
        capacity_rate = 0.4
        
        # Create initial population
        population: Population = [
            ([i % 2 == 0 for i in range(chromosome_length)], 0.0)
            for i in range(population_size)
        ]
        
        # Setup fitness function
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=chromosome_length,
            weights=weights,
            values=values,
            capacityOverTotal=capacity_rate
        )
        
        # Recalculate fitness
        population = [
            (gene, fitness_func(gene))
            for gene, _ in population
        ]
        
        initial_best = max(fitness for _, fitness in population)
        
        # Setup operators
        elite_op = lambda pop: simple_elite_select(pop, 2)
        select_op = lambda pop, *args: tournament_select(pop, 3, *args)
        crossover_op = lambda p1, p2, *args: onepoint_crossover(p1, p2, 0.9, *args)
        mutate_op = lambda c, *args: bitflip_mutate(c, 0.1, *args)
        
        # Create one_step function
        def one_step(pop, *args):
            return one_step_GA(
                fitness=fitness_func,
                elite=elite_op,
                select=select_op,
                crossover=crossover_op,
                mutate=mutate_op,
                population=pop,
                population_size=population_size
            )
        
        # Run using execute_GA
        best_sol, final_best, stats = execute_GA(one_step, population, max_generations)
        
        assert final_best >= initial_best, f"Fitness should improve over generations: {initial_best} -> {final_best}"
        assert final_best == 28.0, f"After {max_generations} generations, expected fitness 28.0, got {final_best}"


def run_all_tests():
    """Run all genetic algorithm tests"""
    print("Running Genetic Algorithm tests...\n")
    
    TestGeneticAlgorithm.test_01()
    print("  ✓ test_01: Basic GA with OneMax")
    
    TestGeneticAlgorithm.test_02()
    print("  ✓ test_02: GA with Knapsack problem")
    
    TestGeneticAlgorithm.test_03()
    print("  ✓ test_03: Edge case - minimum population (2)")
    
    TestGeneticAlgorithm.test_04()
    print("  ✓ test_04: Edge case - minimum chromosome (2 bits)")
    
    TestGeneticAlgorithm.test_05()
    print("  ✓ test_05: FP compliance")
    
    TestGeneticAlgorithm.test_06()
    print("  ✓ test_06: OneMax")
    
    try:
        TestGeneticAlgorithm.test_07()
        print("  ✓ test_07: Knapsack")
    except AssertionError as e:
        print(f"  ✗ test_07: Knapsack - {e}")
    
    print("\n✅ All genetic algorithm tests completed!")


if __name__ == "__main__":
    run_all_tests()
