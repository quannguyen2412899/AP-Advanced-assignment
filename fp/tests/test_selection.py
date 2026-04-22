import sys
from pathlib import Path

# Add parent directory to path so we can import from src
sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from ga_operators import tournament_select
from type_alias import Population
from fake_random_gen import fake_random_engine


class TestSelection:
    """Test suite for Tournament Selection operator"""
    
    @staticmethod
    def test_01():
        """Test 01: Tournament selection with size 3 from population of 5
        Tests: Selects the best chromosome from tournament (indices 0, 3, 4)
        """
        population: Population = [
            ([False] * 10, 10.0),
            ([False] * 10, 50.0),
            ([False] * 10, 30.0),
            ([True] * 10, 80.0),  # Best: index 3 with fitness 80.0
            ([False] * 10, 20.0),
        ]
        
        # Tournament selects from indices 0, 3, 4 -> best is index 3 with fitness 80.0
        rand_eng = fake_random_engine([0, 3, 4])
        selected = tournament_select(population, 3, rand_eng=rand_eng)
        
        assert selected is not None, "Should select a chromosome"
        assert selected == [True] * 10, "Should return the best chromosome (all True)"
    
    @staticmethod
    def test_02():
        """Test 02: Tournament selection - verify correct chromosome selection with different indices
        Tests: Selects best chromosome from different tournament participants
        """
        population: Population = [
            ([False] * 10, 25.0),
            ([False] * 10, 15.0),
            ([True] * 10, 90.0),  # Best: index 2 with fitness 90.0
            ([False] * 10, 40.0),
            ([False] * 10, 60.0),
            ([False] * 10, 35.0),
        ]
        
        # Tournament selects from indices 1, 2, 4, 5 -> best is index 2 with fitness 90.0
        rand_eng = fake_random_engine([1, 2, 4, 5])
        selected = tournament_select(population, 4, rand_eng=rand_eng)
        
        assert selected is not None, "Should select a chromosome"
        assert selected == [True] * 10, "Should return the best chromosome (all True)"
    
    @staticmethod
    def test_03():
        """Test 03: Tournament selection - single participant tournament
        Tests: Correctly returns that single participant
        """
        population: Population = [
            ([False] * 10, 10.0),
            ([False] * 10, 85.0),
            ([True] * 10, 30.0),  # Selected: index 2
            ([False] * 10, 50.0),
            ([False] * 10, 20.0),
        ]
        
        # Tournament size 1 selects index 2 -> returns that chromosome
        rand_eng = fake_random_engine([2])
        selected = tournament_select(population, 1, rand_eng=rand_eng)
        
        assert selected is not None, "Should select a chromosome"
        assert selected == [True] * 10, "Should return the chromosome at index 2"
    
    @staticmethod
    def test_04():
        """Test 04: Tournament selection - edge cases
        Tests: Tournament size equals population size, same fitness values, large populations
        """
        population: Population = [
            ([False] * 10, 50.0),
            ([False] * 10, 50.0),
            ([True] * 10, 100.0),  # Best: index 2 with fitness 100.0
            ([False] * 10, 50.0),
        ]
        
        # Edge case 1: Tournament size equals population size
        # Selects from indices 0, 1, 2, 3 -> best is index 2 with fitness 100.0
        rand_eng = fake_random_engine([0, 1, 2, 3])
        selected = tournament_select(population, 4, rand_eng=rand_eng)
        assert selected is not None, "Should select a chromosome"
        assert selected == [True] * 10, "Should return the best chromosome (all True)"
        
        # Edge case 2: Tournament size 2 with duplicate fitnesses
        # Selects from indices 1, 3 -> both have fitness 50.0
        rand_eng = fake_random_engine([1, 3])
        selected = tournament_select(population, 2, rand_eng=rand_eng)
        assert selected is not None, "Should select a chromosome"
        assert selected == [False] * 10, "Should return one of the selected chromosomes"
        
        # Edge case 3: Large tournament size from large population
        large_pop: Population = [
            ([False] * 10, float(i * 2.0)) if i != 45 else ([True] * 10, 90.0)
            for i in range(50)
        ]
        # Selects from indices 5, 15, 25, 35, 45, 8, 12, 22, 30, 40
        # Best is index 45 with fitness 90.0
        rand_eng = fake_random_engine([5, 15, 25, 35, 45, 8, 12, 22, 30, 40])
        selected = tournament_select(large_pop, 10, rand_eng=rand_eng)
        assert selected is not None, "Should select a chromosome"
        assert selected == [True] * 10, "Should return the best chromosome (all True)"
    
    @staticmethod
    def test_05():
        """Test 05: Tournament selection - all same fitness values
        Tests: Selection with identical fitness values
        """
        population: Population = [
            ([False] * 10, 75.0),
            ([True] * 10, 75.0),  # This one is all True
            ([False] * 10, 75.0),
        ]
        
        # All chromosomes have same fitness, selection from indices 0, 2
        rand_eng = fake_random_engine([0, 2])
        selected = tournament_select(population, 2, rand_eng=rand_eng)
        
        assert selected is not None, "Should select a chromosome"
        assert selected in [[False] * 10, [True] * 10], "Should return one of the tournament participants"
        assert population[0][0] == [False] * 10, "FP: Original population not modified by selection"


def run_all_tests():
    """Run all test methods"""
    print("Running Selection tests...")
    TestSelection.test_01()
    print("  ✓ test_01: Tournament size 3 from 5")
    TestSelection.test_02()
    print("  ✓ test_02: Different indices")
    TestSelection.test_03()
    print("  ✓ test_03: Single participant")
    TestSelection.test_04()
    print("  ✓ test_04: Edge cases")
    TestSelection.test_05()
    print("  ✓ test_05: Same fitness values")
    
    print("\n✅ All selection tests passed!")


if __name__ == "__main__":
    run_all_tests()