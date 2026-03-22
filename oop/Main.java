import core.*;
import models.Population;

/**
 * Usage: java Main --config <json config file> --problem <json problem file> --out <output directory>
 * Example: java Main --config config.json --problem onemax.json --out reports
 **/
public class Main {
    public static void main(String[] args) {

        /* get configs from args - incomplete implementation */
        String generalConfigFile = "";
        String problemConfigFile = "";
        String outputDirectory = "";

        /** Initialization  **/
        GAConfig config = new GAConfig(generalConfigFile, problemConfigFile);
        GeneticAlgorithm ga = new GeneticAlgorithm(config.fitnessEvaluator(),
                                                    config.elitismStrategy(),
                                                    config.selectionStrategy(),
                                                    config.crossoverStrategy(),
                                                    config.mutationStrategy(),
                                                    config.randomGenerator());
        EvolutionReporter reporter = new EvolutionReporter();

        /** Execution **/
        Population initialPopulation = config.generateRandomPopulation();
        ga.run(initialPopulation, reporter);

        /** Export results **/
        reporter.exportStatistics(outputDirectory);
    }
}