package models;

public class Population {
    private Chromosome[] population;
    private int size;

    public Population(Chromosome[] population, int size) {
        if(population.length < size) throw new IllegalArgumentException("Population constructor: array size " + population.length + " < required size " + size);
        this.population = new Chromosome[size];
        System.arraycopy(population, 0, this.population, 0, size); // shallow copy !
        this.size = size;
    }

    public Chromosome getIndividual(int index) {
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException("getIndividual: index " + index + " out of bounds [0, " + size + ")");
        return population[index];
    }

    // public Chromosome[] getPopulation() {
    //     Chromosome[] copy = new Chromosome[size];
    //     System.arraycopy(population, 0, copy, 0, size);
    //     return copy;
    // }

    public int size() {
        return size;
    }
}