package models;
import java.util.BitSet;

public class Chromosome {
    private BitSet chromosome;
    private int length;
    private double fitness;

    public Chromosome(boolean[] chromosome, double fitness) {
        if (chromosome == null) throw new IllegalArgumentException("Chromosome constructor: chromosome is null");
        this.length = chromosome.length;
        this.chromosome = new BitSet(length);
        for (int i = 0; i < length; i++) {
            if (chromosome[i]) {
                this.chromosome.set(i);
            }
        }
        this.fitness = fitness;
    }

    public boolean getBit(int index) {
        if(index < 0 || index >= length) throw new IndexOutOfBoundsException("getBit: index " + index + " out of bounds [0, " + length + ")");
        return chromosome.get(index);
    }

    public boolean[] getBitString() {
        boolean[] bits = new boolean[length];
        for (int i = 0; i < length; i++) {
            bits[i] = chromosome.get(i);
        }
        return bits;
    }

    public double getFitness() {
        return fitness;
    }

    public int length() {
        return length;
    }
}