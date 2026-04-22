import sys
from pathlib import Path

# Add parent directory to path so we can import from src
sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from ga_operators import simple_elite_select, tournament_select
from type_alias import Chromosome, Population, GeneString
from fake_random_gen import fake_random_engine


class TestElitism:
    """Test suite for Elitism operator"""
    
    @staticmethod
    def test_01():
        """Test 01: Elitism with 2 elite - select top 2 from 5 chromosomes
        Tests: Selects the 2 highest fitness chromosomes
        """
        population: Population = [
            ([False] * 10, 10.0),
            ([False] * 10, 50.0),
            ([False] * 10, 30.0),
            ([False] * 10, 80.0),
            ([False] * 10, 20.0),
        ]
        
        elite = simple_elite_select(population, 2)
        
        assert len(elite) == 2, "Should select exactly 2 elite"
        fitness_values = sorted([elite[0][1], elite[1][1]])
        assert fitness_values == [50.0, 80.0], f"Elite should have fitness 50.0 and 80.0, got {fitness_values}"
        assert population[0][1] == 10.0, "FP: Original population fitness not modified"
    
    @staticmethod
    def test_02():
        """Test 02: Elitism with 2 elite - select top 2 from 10 chromosomes with duplicate fitness
        Tests: Handles populations with duplicate fitness values correctly
        """
        population: Population = [
            ([False] * 10, 5.0),
            ([False] * 10, 15.0),
            ([False] * 10, 15.0),
            ([False] * 10, 25.0),
            ([False] * 10, 10.0),
            ([False] * 10, 25.0),
            ([False] * 10, 20.0),
            ([False] * 10, 8.0),
            ([False] * 10, 25.0),
            ([False] * 10, 12.0),
        ]
        
        elite = simple_elite_select(population, 2)
        
        assert len(elite) == 2, "Should select exactly 2 elite"
        fitness_values = sorted([elite[0][1], elite[1][1]])
        assert fitness_values == [25.0, 25.0], f"Elite should both be 25.0, got {fitness_values}"
    
    @staticmethod
    def test_03():
        """Test 03: Elitism with 1 elite - select top 1 from 5 chromosomes
        Tests: Correctly identifies single best chromosome
        """
        population: Population = [
            ([False] * 10, 10.0),
            ([False] * 10, 50.0),
            ([False] * 10, 30.0),
            ([False] * 10, 99.0),
            ([False] * 10, 20.0),
        ]
        
        elite = simple_elite_select(population, 1)
        
        assert len(elite) == 1, "Should select exactly 1 elite"
        assert elite[0][1] == 99.0, f"Elite should be 99.0, got {elite[0][1]}"
    
    @staticmethod
    def test_04():
        """Test 04: Elitism with 1 elite - from large population
        Tests: Correctly finds best among many chromosomes
        """
        population: Population = [
            ([False] * 10, float(i * 2.5))
            for i in range(50)
        ]
        
        elite = simple_elite_select(population, 1)
        
        assert len(elite) == 1, "Should select exactly 1 elite"
        expected_fitness = 49 * 2.5
        assert elite[0][1] == expected_fitness, f"Elite should be {expected_fitness}, got {elite[0][1]}"
    
    @staticmethod
    def test_05():
        """Test 05: Edge cases - elitism count 0, and elitism count equals population size
        Tests: Handles boundary conditions correctly
        """
        population: Population = [
            ([False] * 10, float(i * 10))
            for i in range(5)
        ]
        
        # Test count = 0
        elite_0 = simple_elite_select(population, 0)
        assert len(elite_0) == 0, "Elitism count 0 should return empty list"
        
        # Test count = population size
        elite_5 = simple_elite_select(population, 5)
        assert len(elite_5) == 5, "Elitism count equal to population size should return all"
        
        # Verify all are present and within range
        fitness_values = [elite[1] for elite in elite_5]
        for fitness in fitness_values:
            assert 0.0 <= fitness <= 40.0, f"Fitness should be within range [0, 40], got {fitness}"


def run_all_tests():
    """Run all test methods"""
    print("Running Elitism tests...")
    TestElitism.test_01()
    print("  ✓ test_01: Select top 2 from 5")
    TestElitism.test_02()
    print("  ✓ test_02: Duplicate fitness values")
    TestElitism.test_03()
    print("  ✓ test_03: Select top 1")
    TestElitism.test_04()
    print("  ✓ test_04: Large population")
    TestElitism.test_05()
    print("  ✓ test_05: Edge cases (count 0 and count = population size)")
    
    print("\n✅ All elitism tests passed!")


if __name__ == "__main__":
    run_all_tests()