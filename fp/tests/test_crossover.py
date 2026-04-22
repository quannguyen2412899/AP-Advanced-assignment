import sys
from pathlib import Path

# Add parent directory to path so we can import from src
sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from ga_operators import onepoint_crossover
from type_alias import Population
from fake_random_gen import fake_random_engine


class TestCrossover:
    """Test suite for OnePointCrossover operator"""
    
    @staticmethod
    def test_01():
        """Test 01: OnePointCrossover - no crossover happens (bernoulli false)
        Tests: When bernoulli returns false (0), offspring are identical to parents
        """
        parent1 = [True] * 5 + [False] * 5
        parent2 = [False] * 5 + [True] * 5
        
        # Bernoulli false (2**256) = no crossover, crossover index not used
        rand_eng = fake_random_engine([2**256])
        offspring = onepoint_crossover(parent1, parent2, 0.5, rand_eng=rand_eng)
        
        assert len(offspring) == 2, "Should return 2 offspring"
        assert offspring[0] == parent1, "Offspring 1 should be identical to parent1"
        assert offspring[1] == parent2, "Offspring 2 should be identical to parent2"
    
    @staticmethod
    def test_02():
        """Test 02: OnePointCrossover - crossover happens at index 5
        Tests: When bernoulli true (2**256), crossover at specified index
        """
        parent1 = [True] * 5 + [False] * 5
        parent2 = [False] * 5 + [True] * 5
        
        # Bernoulli true (0), crossover at index 5
        rand_eng = fake_random_engine([0, 5])
        offspring = onepoint_crossover(parent1, parent2, 1.0, rand_eng=rand_eng)
        
        assert len(offspring) == 2, "Should return 2 offspring"
        # Offspring 1: parent1[0:6] + parent2[6:10]
        expected_c1 = parent1[:6] + parent2[6:]
        # Offspring 2: parent2[0:6] + parent1[6:10]
        expected_c2 = parent2[:6] + parent1[6:]
        assert offspring[0] == expected_c1, "Offspring 1 should be parent1[0:6] + parent2[6:]"
        assert offspring[1] == expected_c2, "Offspring 2 should be parent2[0:6] + parent1[6:]"
    
    @staticmethod
    def test_03():
        """Test 03: OnePointCrossover - crossover at different indices
        Tests: Verify correct bit swapping at different crossover points
        """
        parent1 = [True] * 10
        parent2 = [False] * 10
        
        # Bernoulli true (0), crossover at index 3
        rand_eng = fake_random_engine([0, 3])
        offspring = onepoint_crossover(parent1, parent2, 1.0, rand_eng=rand_eng)
        
        assert len(offspring) == 2, "Should return 2 offspring"
        # Offspring 1: parent1[0:4] + parent2[4:10]
        expected_c1 = [True] * 4 + [False] * 6
        # Offspring 2: parent2[0:4] + parent1[4:10]
        expected_c2 = [False] * 4 + [True] * 6
        assert offspring[0] == expected_c1, "Offspring 1 should be [T]*4 + [F]*6"
        assert offspring[1] == expected_c2, "Offspring 2 should be [F]*4 + [T]*6"
    
    @staticmethod
    def test_04():
        """Test 04: OnePointCrossover - edge cases (boundaries and large chromosomes)
        Tests: Crossover at boundaries (index 1, index 9), large chromosomes
        """
        parent1 = [True] * 5 + [False] * 5
        parent2 = [False] * 5 + [True] * 5
        
        # Edge case 1: Crossover at index 1 (minimal swap)
        rand_eng = fake_random_engine([0, 1])
        offspring = onepoint_crossover(parent1, parent2, 1.0, rand_eng=rand_eng)
        expected_c1 = parent1[:2] + parent2[2:]
        expected_c2 = parent2[:2] + parent1[2:]
        assert offspring[0] == expected_c1, "Offspring 1 should swap at index 1"
        assert offspring[1] == expected_c2, "Offspring 2 should swap at index 1"
        
        # Edge case 2: Large chromosomes with alternating pattern
        parent3 = [i % 2 == 0 for i in range(50)]
        parent4 = [i % 2 == 1 for i in range(50)]
        
        rand_eng = fake_random_engine([0, 25])
        offspring = onepoint_crossover(parent3, parent4, 1.0, rand_eng=rand_eng)
        expected_c1 = parent3[:26] + parent4[26:]
        expected_c2 = parent4[:26] + parent3[26:]
        assert offspring[0] == expected_c1, "Offspring 1 should crossover at index 25"
        assert offspring[1] == expected_c2, "Offspring 2 should crossover at index 25"
    
    @staticmethod
    def test_05():
        """Test 05: OnePointCrossover - with probability control
        Tests: Crossover respects probability (no crossover vs crossover)
        """
        parent1 = [True, False, True, False, True, False, True, False, True, False]
        parent2 = [False, True, False, True, False, True, False, True, False, True]
        
        # Test with no crossover (bernoulli false)
        rand_eng = fake_random_engine([2**256])
        offspring_no = onepoint_crossover(parent1, parent2, 0.0, rand_eng=rand_eng)
        assert offspring_no[0] == parent1, "No crossover: offspring 1 identical to parent1"
        assert offspring_no[1] == parent2, "No crossover: offspring 2 identical to parent2"
        
        # Test with crossover (bernoulli true)
        rand_eng = fake_random_engine([0, 5])
        offspring_yes = onepoint_crossover(parent1, parent2, 1.0, rand_eng=rand_eng)
        # Verify bits after index 5 are swapped
        swapped = False
        for i in range(5, 10):
            if offspring_yes[0][i] == parent2[i] and offspring_yes[1][i] == parent1[i]:
                swapped = True
                break
        assert swapped, "Crossover should have swapped bits from index 5 onwards"
    
    @staticmethod
    def test_06():
        """Test 06: OnePointCrossover - two-bit chromosomes (minimum valid case)
        Tests: Verify crossover works with minimal chromosome size (2 bits)
        """
        parent1 = [True, True]
        parent2 = [False, False]
        
        # Bernoulli true (0), crossover at index 0 (only valid point for length 2)
        rand_eng = fake_random_engine([0, 1])
        offspring = onepoint_crossover(parent1, parent2, 1.0, rand_eng=rand_eng)
        
        assert len(offspring) == 2, "Should return 2 offspring"
        assert len(offspring[0]) == 2, "Offspring should have length 2"
        assert len(offspring[1]) == 2, "Offspring should have length 2"
        
        # Offspring 1: parent1[0:1] + parent2[1:] = [T] + [F] = [T, F]
        # Offspring 2: parent2[0:1] + parent1[1:] = [F] + [T] = [F, T]
        expected_c1 = [True, False]
        expected_c2 = [False, True]
        assert offspring[0] == expected_c1, "Offspring 1 should be [T, F]"
        assert offspring[1] == expected_c2, "Offspring 2 should be [F, T]"
        assert parent1 == [True, True], "FP: Parent1 not modified by crossover"
        assert parent2 == [False, False], "FP: Parent2 not modified by crossover"


def run_all_tests():
    """Run all test methods"""
    print("Running Crossover tests...")
    TestCrossover.test_01()
    print("  ✓ test_01: No crossover (bernoulli false)")
    TestCrossover.test_02()
    print("  ✓ test_02: Crossover at index 5")
    TestCrossover.test_03()
    print("  ✓ test_03: Crossover at different indices")
    TestCrossover.test_04()
    print("  ✓ test_04: Edge cases (boundaries and large chromosomes)")
    TestCrossover.test_05()
    print("  ✓ test_05: Probability control")
    TestCrossover.test_06()
    print("  ✓ test_06: Two-bit chromosomes")
    
    print("\n✅ All crossover tests passed!")


if __name__ == "__main__":
    run_all_tests()
