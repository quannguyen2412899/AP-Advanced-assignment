package utils;
import java.util.BitSet;

// unused
public class UnmodifiableBitSet {
    final private BitSet bitSet;
    int size;

    public UnmodifiableBitSet(BitSet bitSet, int size) {
        if(bitSet.length() > size) throw new IllegalArgumentException("UnmodifiableBitSet construction: BitSet length exceeds specified size");
        this.bitSet = bitSet;
        this.size = size;
    }
    public UnmodifiableBitSet(UnmodifiableBitSet other) {
        this.bitSet = other.bitSet;
        this.size = other.size;
    }

    public boolean get(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("UnmodifiableBitSet::get() index out of bounds");
        return bitSet.get(index);
    }
    public int cardinality() {
        return bitSet.cardinality();
    }
    public BitSet toBitSet() {
        return (BitSet) bitSet.clone();
    }
    public int size() {
        return size;
    }
}