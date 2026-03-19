import core.*;
import models.Population;
import utils.RandomUtil;

/**
 * Usage: java Main --config <config.json> --problem <'onemax'|'knapsack'
 * Example: java Main --config config.json --problem 'onemax'
 **/
public class Main {
    public static void main(String[] args) {

        /* get configs from args - incomplete implementation */
        String generalConfig = "";
        String problemConfig = "";        

        /** Initialization  **/
        GAConfig config = new GAConfig(generalConfig, problemConfig);
        GeneticAlgorithm ga = new GeneticAlgorithm(config.fitnessEvaluator(),
                                                    config.elitism(),
                                                    config.selectionStrategy(),
                                                    config.crossoverStrategy(),
                                                    config.mutationStrategy());
        EvolutionReporter reporter = new EvolutionReporter();
        RandomUtil.init(config.randomSeed());

        /** Execution **/
        Population initialPopulation = config.generateRandomPopulation();
        ga.run(initialPopulation, reporter);

        /** Export results **/
        reporter.exportStatistics();
    }
}