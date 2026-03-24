package models;
import java.util.BitSet;

public class Chromosome {
    private BitSet chromosome;
    private int length;
    private double fitness;

    public Chromosome(BitSet chromosome, int length, double fitness) {
        if(chromosome.size() < length) throw new IllegalArgumentException("Chromosome constructor: BitSet size " + chromosome.size() + " < required length " + length);
        this.chromosome = (BitSet) chromosome.clone();
        this.length = length;
        this.fitness = fitness;
    }

    public Chromosome(Chromosome other) {
        this.chromosome = (BitSet) other.chromosome.clone();
        this.length = other.length;
        this.fitness = other.fitness;
    }

    public boolean getBit(int index) {
        if(index < 0 || index >= length) throw new IndexOutOfBoundsException("getBit: index " + index + " out of bounds [0, " + length + ")");
        return chromosome.get(index);
    }

    public BitSet getBitString() {
        return (BitSet) chromosome.clone();
    }

    public double getFitness() {
        return fitness;
    }

    public int length() {
        return length;
    }
}