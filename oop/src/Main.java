import core.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import models.Population;
import reporter.*;
import setup.*;

/**
 * Usage: java Main --config <json config file> --problem <json problem file> --out <output directory>
 * Example: java Main --config config.json --problem onemax.json --out reports
 * There's no default argument, user must declare all flags.
 * <output directory>'s existence is user's responsibility, the program doesn't automatically create one if not found.
 **/
public class Main {

    private static final String USAGE_MESSAGE = "Usage: java Main --config <json config file> --problem <json problem file> --out <output directory>";

    public static void main(String[] args) {
        
        /** Parsing & argument safe guards **/
        Parser p = new Parser(args);
        if(!p.isValid) errorExit("Invalid argument.");
        String generalConfigFile = p.generalConfigFile;
        // String problemConfigFile = p.problemConfigFile;
        String outputDirectory = p.outputDirectory;

        try {
            /** Initialization  **/
            GAConfig config = GAConfigLoader.load(generalConfigFile);
            int maxGenerations = config.maxGenerations();
            GeneticAlgorithm ga = new GeneticAlgorithm(config.fitnessEvaluator(),
                                                        config.elitismStrategy(),
                                                        config.selectionStrategy(),
                                                        config.crossoverStrategy(),
                                                        config.mutationStrategy(),
                                                        config.randomUtil());
            EvolutionReporter reporter = new EvolutionReporter();

            /** Execution **/
            Population initialPopulation = config.generateRandomPopulation();
            ga.run(initialPopulation, reporter, maxGenerations);

            /** Results exportation **/
            reporter.exportStatistics(outputDirectory);
        } 
        catch(Exception e) {
            errorExit(e.getMessage());
        }
    }

    /** Argument parser helper class **/
    private static class Parser {
        private String generalConfigFile, problemConfigFile, outputDirectory;
        private boolean isValid;
        public Parser(String[] args) {
            isValid = true;
            if(args.length != 6) isValid = false;
            for (int i = 0; i < args.length - 1 && isValid; i += 2) {
                switch (args[i]) {
                    case "--config" -> generalConfigFile = args[i+1];
                    case "--problem" -> problemConfigFile = args[i+1];
                    case "--out" -> outputDirectory = args[i+1];
                    default -> isValid = false;
                }
            }
            fileExistenceCheck();
            fileTypeCheck();
        }
        private void fileExistenceCheck() {
            if (!isValid) return;
            if (!Files.exists(Paths.get(generalConfigFile))) isValid = false;
            if (!Files.exists(Paths.get(problemConfigFile))) isValid = false;
            if (!Files.exists(Paths.get(outputDirectory))) isValid = false;
        }
        private void fileTypeCheck() {
            if (!isValid) return;
            if (!generalConfigFile.endsWith(".json")) isValid = false;
            if (!problemConfigFile.endsWith(".json")) isValid = false;
        }
    }

    /** On error **/
    private static void errorExit(String msg) {
        System.err.println(msg);
        System.err.println(USAGE_MESSAGE);
        System.exit(1);
    }
}