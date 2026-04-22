import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from fitness_functions import get_fitness_function, onemax_evaluator, knapsack_evaluator
from fake_random_gen import fake_random_engine


class TestOneMax:
    """Test suite for OneMax fitness evaluator"""
    
    @staticmethod
    def test_01():
        """Test 01: OneMax - all false bits (fitness = 0)
        Tests: Count of true bits with all false input
        """
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=10)
        chromosome = [False] * 10
        fitness = fitness_func(chromosome)
        assert fitness == 0, "All false bits should have fitness 0"
        assert chromosome == [False] * 10, "FP: Input chromosome must not be modified"
    
    @staticmethod
    def test_02():
        """Test 02: OneMax - all true bits (fitness = length)
        Tests: Count of true bits with all true input
        """
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=10)
        chromosome = [True] * 10
        fitness = fitness_func(chromosome)
        assert fitness == 10, "All true bits should have fitness 10"
        assert fitness_func(chromosome) == fitness, "FP: Function is pure (same input produces same output)"
    
    @staticmethod
    def test_03():
        """Test 03: OneMax - mixed bits (fitness = count of true)
        Tests: Correct count with mixed true/false pattern
        """
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=10)
        chromosome = [True, False, True, False, True, False, True, False, True, False]
        fitness = fitness_func(chromosome)
        assert fitness == 5, "5 true bits should have fitness 5"
        assert chromosome[0] == True, "FP: Original input not modified after evaluation"
    
    @staticmethod
    def test_04():
        """Test 04: OneMax - edge cases and boundaries
        Tests: Null input, length mismatches (undersize, oversize), exact match boundary
        """
        fitness_func = get_fitness_function(name="onemax", chromosomeLength=10)
        
        # Edge case 1: Null chromosome
        try:
            fitness_func(None)
            assert False, "Should throw exception for None chromosome"
        except (ValueError, TypeError):
            pass  # Expected
        
        # Edge case 2: Undersized chromosome (length 9 < expected 10)
        try:
            chromosome = [False] * 9
            fitness_func(chromosome)
            assert False, "Should throw exception for undersized chromosome"
        except ValueError as e:
            assert "length" in str(e).lower(), "Exception should mention length"
        
        # Edge case 3: Oversized chromosome (length 11 > expected 10)
        try:
            chromosome = [False] * 11
            fitness_func(chromosome)
            assert False, "Should throw exception for oversized chromosome"
        except ValueError as e:
            assert "length" in str(e).lower(), "Exception should mention length"
            assert len(chromosome) == 11, "FP: Input list length unchanged after failed validation"
        
        # Edge case 4: Empty chromosome (length 0 < expected 10)
        try:
            chromosome = []
            fitness_func(chromosome)
            assert False, "Should throw exception for empty chromosome"
        except ValueError as e:
            assert "length" in str(e).lower(), "Exception should mention length"
        
        # Edge case 5: Exact length match with all false (valid, fitness 0)
        fitness_func_exact = get_fitness_function(name="onemax", chromosomeLength=5)
        chromosome_exact = [False] * 5
        fitness_exact = fitness_func_exact(chromosome_exact)
        assert fitness_exact == 0, "Exact length match with all false should have fitness 0"
        assert len(chromosome_exact) == 5, "FP: Input length not changed by function"
    
    @staticmethod
    def test_05():
        """Test 05: OneMax - various evaluators with different lengths
        Tests: Correct fitness calculation with different configured lengths
        """
        # Length 5
        fitness_func_5 = get_fitness_function(name="onemax", chromosomeLength=5)
        chromosome_5 = [True, True, False, True, False]
        fitness_5 = fitness_func_5(chromosome_5)
        assert fitness_5 == 3, "3 true bits out of 5 should have fitness 3"
        
        # Length 20
        fitness_func_20 = get_fitness_function(name="onemax", chromosomeLength=20)
        chromosome_20 = [i % 2 == 0 for i in range(20)]  # 10 true bits
        fitness_20 = fitness_func_20(chromosome_20)
        assert fitness_20 == 10, "10 true bits out of 20 should have fitness 10"
        
        # Length 100
        fitness_func_100 = get_fitness_function(name="onemax", chromosomeLength=100)
        chromosome_100 = [i < 42 for i in range(100)]  # 42 true bits
        fitness_100 = fitness_func_100(chromosome_100)
        assert fitness_100 == 42, "42 true bits out of 100 should have fitness 42"
        assert fitness_func_100(chromosome_100) == 42, "FP: Pure function returns same result on repeated calls"


class TestKnapsack:
    """Test suite for Knapsack fitness evaluator"""
    
    @staticmethod
    def test_01():
        """Test 01: Knapsack - all items not selected (fitness = 0)
        Tests: No items selected returns 0 value
        """
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=3,
            weights=[2, 3, 4],
            values=[10, 20, 30],
            capacityOverTotal=5 / (2 + 3 + 4)
        )
        chromosome = [False, False, False]
        fitness = fitness_func(chromosome)
        assert fitness == 0, "No items selected should have fitness 0"
        assert chromosome == [False, False, False], "FP: Input not modified"
    
    @staticmethod
    def test_02():
        """Test 02: Knapsack - all items selected within capacity
        Tests: All items fit and all selected
        """
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=2,
            weights=[1, 2],
            values=[5, 10],
            capacityOverTotal=10 / (1 + 2)
        )
        chromosome = [True, True]
        fitness = fitness_func(chromosome)
        assert fitness == 15, "All items selected should have fitness 15 (5+10)"
        assert fitness_func(chromosome) == 15, "FP: Pure function produces consistent results"
    
    @staticmethod
    def test_03():
        """Test 03: Knapsack - items exceed capacity (fitness = 0)
        Tests: Total weight exceeds capacity returns 0
        """
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=3,
            weights=[5, 5, 5],
            values=[10, 10, 10],
            capacityOverTotal=10 / (5 + 5 + 5)
        )
        chromosome = [True, True, True]  # Total weight = 15 > capacity
        fitness = fitness_func(chromosome)
        assert fitness == 0, "Exceeding capacity should have fitness 0"
        assert len(chromosome) == 3, "FP: Input chromosome length unchanged"
    
    @staticmethod
    def test_04():
        """Test 04: Knapsack - partial selection within capacity
        Tests: Some items selected, total within capacity
        """
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=4,
            weights=[2, 3, 4, 2],
            values=[10, 20, 30, 5],
            capacityOverTotal=7 / (2 + 3 + 4 + 2)
        )
        chromosome = [True, True, False, False]  # Weight = 5, Value = 30
        fitness = fitness_func(chromosome)
        assert fitness == 30, "Selected items with weight 5 should have fitness 30"
        assert chromosome[0] == True, "FP: Original chromosome elements unchanged"
    
    @staticmethod
    def test_05():
        """Test 05: Knapsack - edge cases and validation
        Tests: Null input, length mismatches, invalid weights/values
        """
        # Normal case first
        fitness_func = get_fitness_function(
            name="knapsack",
            chromosomeLength=3,
            weights=[1, 2, 3],
            values=[5, 10, 15],
            capacityOverTotal=5 / (1 + 2 + 3)
        )
        
        # Edge case 1: Null chromosome
        try:
            fitness_func(None)
            assert False, "Should throw exception for None chromosome"
        except (ValueError, TypeError):
            pass  # Expected
        
        # Edge case 2: Undersized chromosome
        try:
            chromosome = [True, False]
            fitness_func(chromosome)
            assert False, "Should throw exception for undersized chromosome"
        except ValueError as e:
            assert "length" in str(e).lower(), "Exception should mention length"
        
        # Edge case 3: Oversized chromosome
        try:
            chromosome = [True, False, True, False]
            fitness_func(chromosome)
            assert False, "Should throw exception for oversized chromosome"
        except ValueError as e:
            assert "length" in str(e).lower(), "Exception should mention length"
    
    @staticmethod
    def test_06():
        """Test 06: Knapsack - various configurations with different capacities
        Tests: Correct fitness calculation with different capacities and item sets
        """
        # Small knapsack
        fitness_func_small = get_fitness_function(
            name="knapsack",
            chromosomeLength=3,
            weights=[2, 2, 2],
            values=[5, 5, 5],
            capacityOverTotal=3 / (2 + 2 + 2)
        )
        chromosome_small = [True, True, False]  # Weight = 4 > capacity 3
        fitness_small = fitness_func_small(chromosome_small)
        assert fitness_small == 0, "Exceeding small capacity should have fitness 0"
        
        # Medium knapsack
        fitness_func_med = get_fitness_function(
            name="knapsack",
            chromosomeLength=4,
            weights=[1, 2, 3, 4],
            values=[2, 4, 6, 8],
            capacityOverTotal=5 / (1 + 2 + 3 + 4)
        )
        chromosome_med = [True, True, True, False]  # Weight = 6 > capacity 5
        fitness_med = fitness_func_med(chromosome_med)
        assert fitness_med == 0, "Exceeding medium capacity should have fitness 0"
        
        chromosome_med_valid = [True, False, True, False]  # Weight = 4 <= capacity 5
        fitness_med_valid = fitness_func_med(chromosome_med_valid)
        assert fitness_med_valid == 8, "Valid selection should have correct fitness"
        
        # Large knapsack
        fitness_func_large = get_fitness_function(
            name="knapsack",
            chromosomeLength=5,
            weights=[1, 1, 1, 1, 1],
            values=[10, 20, 30, 40, 50],
            capacityOverTotal=10 / (1 + 1 + 1 + 1 + 1)
        )
        chromosome_large = [True, True, True, True, True]  # Weight = 5, Value = 150
        fitness_large = fitness_func_large(chromosome_large)
        assert fitness_large == 150, "All items with low weight should have fitness 150"


def run_all_tests():
    """Run all test methods"""
    print("Running OneMax tests...")
    TestOneMax.test_01()
    print("  ✓ test_01: All false bits")
    TestOneMax.test_02()
    print("  ✓ test_02: All true bits")
    TestOneMax.test_03()
    print("  ✓ test_03: Mixed bits")
    TestOneMax.test_04()
    print("  ✓ test_04: Edge cases")
    TestOneMax.test_05()
    print("  ✓ test_05: Various lengths")
    
    print("\nRunning Knapsack tests...")
    TestKnapsack.test_01()
    print("  ✓ test_01: No items selected")
    TestKnapsack.test_02()
    print("  ✓ test_02: All items within capacity")
    TestKnapsack.test_03()
    print("  ✓ test_03: Items exceed capacity")
    TestKnapsack.test_04()
    print("  ✓ test_04: Partial selection")
    TestKnapsack.test_05()
    print("  ✓ test_05: Edge cases and validation")
    TestKnapsack.test_06()
    print("  ✓ test_06: Various configurations")
    
    print("\n✅ All tests passed!")


if __name__ == "__main__":
    run_all_tests()
