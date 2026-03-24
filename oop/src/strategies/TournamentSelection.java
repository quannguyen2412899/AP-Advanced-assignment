package strategies;

import models.*;
import utils.RandomUtil;

public class TournamentSelection implements SelectionStrategy {
    private int tournamentSize;

    public TournamentSelection(int tournamentSize) {
        if (tournamentSize <= 0) throw new IllegalArgumentException("TournamentSelection: tournamentSize must be > 0, got " + tournamentSize);
        this.tournamentSize = tournamentSize;
    }
    public TournamentSelection(TournamentSelection other) {
        this.tournamentSize = other.tournamentSize;
    }
    
    @Override
    public Chromosome select(Population population, RandomUtil random) {
        if (tournamentSize > population.size()) throw new IllegalArgumentException("TournamentSelection.select: tournamentSize " + tournamentSize + " > population size " + population.size());
        
        int populationSize = population.size();
        Chromosome bestChromosome = null;
        double bestFitness = Double.NEGATIVE_INFINITY;

        for(int i = 0; i < tournamentSize; i++) {
            Chromosome chrom = population.getIndividual(random.nextInt(populationSize));
            if(chrom.getFitness() > bestFitness) {
                bestFitness = chrom.getFitness();
                bestChromosome = chrom;
            }
        }

        return bestChromosome;
    }
}