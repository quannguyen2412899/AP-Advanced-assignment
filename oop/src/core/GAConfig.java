package core;

import fitnesses.*;
import models.*;
import strategies.*;
import utils.RandomUtil;

public class GAConfig {

    public GAConfig(String generalConfigFile) {

    }

    public FitnessEvaluator fitnessEvaluator(String problemConfigFile) {
        return null;
    }
    public ElitismStrategy elitismStrategy() {
        return null;
    }
    public SelectionStrategy selectionStrategy() {
        return null;
    }
    public CrossoverStrategy crossoverStrategy() {
        return null;
    }
    public MutationStrategy mutationStrategy() {
        return null;
    }
    public Population generateRandomPopulation() {
        return null;
    }
    public int populationSize() {
        return 0;
    }
    public int chromosomeLength() {
        return 0;
    }
    public int tournamentSize() {
        return 0;
    }
    public double crossoverRate() {
        return 0.0;
    }
    public double mutationRate() {
        return 0.0;
    }
    public int elitismCount() {
        return 0;
    }
    public int maxGenerations() {
        return 0;
    }
    public double maxFitness() {
        return 0.0;
    }
    public int randomSeed() {
        return 0;
    }
    public RandomUtil randomUtil() {
        return null;
    }
}