package core;

import fitnesses.FitnessEvaluator;
import models.Population;
import strategies.*;
import utils.RandomUtil;

public class GeneticAlgorithm {
    private EvolutionStep oneStepEvolution;
    private int maxGenerations;
    
    public GeneticAlgorithm(FitnessEvaluator fe,
                            ElitismStrategy es,
                            SelectionStrategy ss,
                            CrossoverStrategy cs,
                            MutationStrategy ms,
                            RandomUtil random) {
        /* implements */
    }

    public Population run(Population initialPopulation, EvolutionReporter analyzer) {
        /* implements */
        return null;
    }
}

class EvolutionStep {
    private FitnessEvaluator fitnessEvaluator;
    private ElitismStrategy elitismStrategy;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;

    public EvolutionStep(FitnessEvaluator fe,
                        ElitismStrategy es,
                        SelectionStrategy ss,
                        CrossoverStrategy cs,
                        MutationStrategy ms,
                        RandomUtil random) {
    
    }
    public Population evolve(Population population) {
        return null;
    }
}