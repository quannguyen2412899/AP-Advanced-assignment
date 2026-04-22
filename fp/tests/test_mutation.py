import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from ga_operators import bitflip_mutate
from type_alias import GeneString
from fake_random_gen import fake_random_engine


class TestMutation:
    """Test suite for bitflip_mutate operator"""
    
    @staticmethod
    def test_01():
        """Test 01: BitFlipMutation - no mutation happens (mutation rate 0.0)
        Tests: When mutation rate is 0.0, chromosome unchanged regardless of random values
        """
        chromosome = [True, False, True, False, True, False, True, False, True, False]
        
        # With mutation rate 0.0, bernoulli always returns False (no flips)
        # Need 10 bernoulli False values (2**256 in fake_random_engine)
        rand_eng = fake_random_engine([2**256] * 10)
        mutated = bitflip_mutate(chromosome, 0.0, rand_eng=rand_eng)
        
        assert len(mutated) == 10, "Mutated chromosome should have same length"
        assert mutated == chromosome, "Chromosome should be unchanged with rate 0.0"
    
    @staticmethod
    def test_02():
        """Test 02: BitFlipMutation - all bits mutate (mutation rate 1.0)
        Tests: When mutation rate is 1.0, all bits are always flipped
        """
        chromosome = [True, False, True, False, True, False, True, False, True, False]
        
        # With mutation rate 1.0, every bernoulli returns True (flip)
        # Need 10 bernoulli True values (0 in fake_random_engine)
        rand_eng = fake_random_engine([0] * 10)
        mutated = bitflip_mutate(chromosome, 1.0, rand_eng=rand_eng)
        
        assert len(mutated) == 10, "Mutated chromosome should have same length"
        # All bits should be flipped
        for i in range(10):
            assert mutated[i] == (not chromosome[i]), f"Bit {i} should be flipped"
    
    @staticmethod
    def test_03():
        """Test 03: BitFlipMutation - selective mutations (determined bernoulli sequence)
        Tests: Only specific bits mutate based on bernoulli sequence
        """
        chromosome = [True, True, True, True, True, False, False, False, False, False]
        
        # Mutation rate 0.5, flip bits at indices 1, 3, 5, 7, 9
        # Bernoulli sequence: [False(no), True(yes), False(no), True(yes), ...]
        # 0 means bernoulli returns True (flip), 2**256 means False (no flip)
        bernoulli_seq = [2**256, 0, 2**256, 0, 2**256, 0, 2**256, 0, 2**256, 0]
        rand_eng = fake_random_engine(bernoulli_seq)
        mutated = bitflip_mutate(chromosome, 0.5, rand_eng=rand_eng)
        
        assert len(mutated) == 10, "Mutated chromosome should have same length"
        
        # Verify correct bits were flipped
        assert mutated[0] == True, "Bit 0 should not mutate"
        assert mutated[1] == False, "Bit 1 should be flipped"
        assert mutated[2] == True, "Bit 2 should not mutate"
        assert mutated[3] == False, "Bit 3 should be flipped"
        assert mutated[4] == True, "Bit 4 should not mutate"
        assert mutated[5] == True, "Bit 5 should be flipped"
        assert mutated[6] == False, "Bit 6 should not mutate"
        assert mutated[7] == True, "Bit 7 should be flipped"
        assert mutated[8] == False, "Bit 8 should not mutate"
        assert mutated[9] == True, "Bit 9 should be flipped"
    
    @staticmethod
    def test_04():
        """Test 04: BitFlipMutation - edge cases
        Tests: Empty chromosome, single-bit chromosome, large chromosome
        """
        # Edge case 1: Empty chromosome
        empty_chromosome: GeneString = []
        rand_eng = fake_random_engine([])
        mutated_empty = bitflip_mutate(empty_chromosome, 0.5, rand_eng=rand_eng)
        assert len(mutated_empty) == 0, "Empty chromosome should remain empty"
        
        # Edge case 2: Single-bit chromosome (all mutation)
        single_bit: GeneString = [True]
        rand_eng = fake_random_engine([0])  # Bernoulli True (flip)
        mutated_single = bitflip_mutate(single_bit, 1.0, rand_eng=rand_eng)
        assert len(mutated_single) == 1, "Single-bit chromosome should have length 1"
        assert mutated_single[0] == False, "Single bit should be flipped"
        
        # Edge case 3: Large chromosome (100 bits)
        large_chromosome: GeneString = [i % 2 == 0 for i in range(100)]  # alternating pattern
        # Mutation at every 3rd bit (indices 0, 3, 6, 9, ...)
        bernoulli_large = []
        for i in range(100):
            if i % 3 == 0:
                bernoulli_large.append(0)  # Flip
            else:
                bernoulli_large.append(2**256)  # No flip
        
        rand_eng = fake_random_engine(bernoulli_large)
        mutated_large = bitflip_mutate(large_chromosome, 0.5, rand_eng=rand_eng)
        assert len(mutated_large) == 100, "Large chromosome should have correct length"
        
        # Verify correct bits were flipped in large chromosome
        for i in range(100):
            if i % 3 == 0:
                assert mutated_large[i] == (not large_chromosome[i]), f"Bit {i} should be flipped"
            else:
                assert mutated_large[i] == large_chromosome[i], f"Bit {i} should not be flipped"
    
    @staticmethod
    def test_05():
        """Test 05: BitFlipMutation - original chromosome unchanged
        Tests: Original chromosome is not modified (defensive copy)
        """
        chromosome = [True, False, True, False, True]
        original_copy = chromosome.copy()
        
        # All bits mutate
        rand_eng = fake_random_engine([0] * 5)
        mutated = bitflip_mutate(chromosome, 1.0, rand_eng=rand_eng)
        
        # Verify original chromosome unchanged
        assert chromosome == original_copy, "Original chromosome should not be modified"
        
        # Verify mutated is different
        assert mutated != chromosome, "Mutated should be different from original"
        
        # Verify mutated is independent
        mutated[0] = True
        assert chromosome[0] == original_copy[0], "Original chromosome should be independent from mutated result"
    
    @staticmethod
    def test_06():
        """Test 06: BitFlipMutation - two-bit chromosome with mixed mutations
        Tests: Verify mutation works correctly with minimal chromosome size
        """
        chromosome = [True, True]
        
        # First bit mutates, second bit doesn't
        rand_eng = fake_random_engine([0, 2**256])
        mutated = bitflip_mutate(chromosome, 0.5, rand_eng=rand_eng)
        
        assert len(mutated) == 2, "Should have length 2"
        assert mutated[0] == False, "First bit should be flipped"
        assert mutated[1] == True, "Second bit should not be flipped"
        assert chromosome == [True, True], "FP: Original chromosome not modified by mutation"


def run_all_tests():
    """Run all test methods"""
    print("Running Mutation tests...")
    TestMutation.test_01()
    print("  ✓ test_01: No mutation (rate 0.0)")
    TestMutation.test_02()
    print("  ✓ test_02: All bits mutate (rate 1.0)")
    TestMutation.test_03()
    print("  ✓ test_03: Selective mutations")
    TestMutation.test_04()
    print("  ✓ test_04: Edge cases (empty, single-bit, large)")
    TestMutation.test_05()
    print("  ✓ test_05: Original chromosome unchanged")
    TestMutation.test_06()
    print("  ✓ test_06: Two-bit chromosome")
    
    print("\n✅ All mutation tests passed!")


if __name__ == "__main__":
    run_all_tests()
