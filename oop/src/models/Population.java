package models;

import java.util.List;

public class Population {
    private List<Chromosome> population;
    private int size;

    Population(List<Chromosome> population) {
        /* implements */
    }

    public Chromosome getIndividual(int index) {
        /* implements */
        return null;
    }

    public List<Chromosome> getPopulation() {
        /* implements - return unmodifiable view */
        return null;
    }

    public int size() {
        /* implements */
        return 0;
    }
}