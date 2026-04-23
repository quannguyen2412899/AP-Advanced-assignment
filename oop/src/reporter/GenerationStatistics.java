package reporter;

import models.Chromosome;
import models.Population;

public class GenerationStatistics {
    public int generation;
    public double minFitness;
    public double maxFitness;
    public double averageFitness;
    public double standardDeviation;
    public boolean[] bestSolution;
    
    private GenerationStatistics(int generation,
                                double minFitness,
                                double maxFitness,
                                double averageFitness,
                                double standardDeviation,
                                boolean[] bestSolution) {
        this.generation = generation;
        this.minFitness = minFitness;
        this.maxFitness = maxFitness;
        this.averageFitness = averageFitness;
        this.standardDeviation = standardDeviation;
        this.bestSolution = bestSolution;
    }

    static public GenerationStatistics statisticsOf(Population population, int generation) {
        int populationSize = population.size();
        Chromosome bestChromosome = null;
        double minFitness = Double.POSITIVE_INFINITY;
        double maxFitness = Double.NEGATIVE_INFINITY;
        double totalFitness = 0, totalSquaredFitness = 0;

        for(int i = 0; i < populationSize; i++) {
            Chromosome c = population.getIndividual(i);
            double fit = c.getFitness();
            totalFitness += fit;
            totalSquaredFitness += fit * fit;
            if(fit < minFitness) minFitness = fit;
            if(fit > maxFitness) {
                maxFitness = fit;
                bestChromosome = c;
            }
        }
        boolean[] bestSolution = bestChromosome.getBitString();
        double averageFitness = totalFitness / populationSize;
        double averageSquaredFitness = totalSquaredFitness / populationSize;
        double variance = populationSize == 1 ? 0 : (averageSquaredFitness - averageFitness * averageFitness)
                                                    * populationSize / (populationSize - 1);
        double standardDeviation = Math.sqrt(Math.max(0, variance));

        return new GenerationStatistics(generation,
                                        minFitness,
                                        maxFitness,
                                        averageFitness,
                                        standardDeviation,
                                        bestSolution);
    }

}