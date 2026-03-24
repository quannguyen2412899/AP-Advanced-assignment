
import java.util.BitSet;

public class UnmodifiableBitSet {
    final private BitSet bitSet;

    public UnmodifiableBitSet(BitSet bitSet) {
        this.bitSet = (BitSet) bitSet.clone();
    }
    public UnmodifiableBitSet(UnmodifiableBitSet other) {
        this.bitSet = (BitSet) other.bitSet.clone();
    }

    public boolean get(int index) {
        return bitSet.get(index);
    }
    public int cardinality() {
        return bitSet.cardinality();
    }
    public BitSet toBitSet() {
        return (BitSet) bitSet.clone();
    }
}