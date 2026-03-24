package models;

public class Population {
    private Chromosome[] population;
    private int size;

    public Population(Chromosome[] population, int size) {
        if(population.length < size) throw new IllegalArgumentException("Population constructor: array size " + population.length + " < required size " + size);
        this.population = new Chromosome[size];
        System.arraycopy(population, 0, this.population, 0, size);
        this.size = size;
    }

    public Chromosome getIndividual(int index) {
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException("getIndividual: index " + index + " out of bounds [0, " + size + ")");
        return population[index];
    }

    public Chromosome[] getPopulation() {
        Chromosome[] clonePopulation = new Chromosome[size];
        for (int i = 0; i < size; i++) {
            clonePopulation[i] = new Chromosome(population[i].getBitString(), population[i].length(), population[i].getFitness());
        }
        return clonePopulation;
    }

    public int size() {
        return size;
    }
}