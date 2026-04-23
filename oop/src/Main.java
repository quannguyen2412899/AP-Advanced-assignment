import core.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import models.Chromosome;
import models.Population;
import reporter.*;
import setup.*;

/**
 * Compile: javac -d bin -cp "lib/*" src/models/*.java src/utils/*.java src/fitnesses/*.java src/strategies/*.java src/core/*.java src/reporter/*.java src/setup/*.java src/Main.java
 * Usage: java Main --config <json config file> --out <output file path>
 * Example (from root): java -cp "oop/bin:oop/lib/*" Main --config config.json --out reports/output.json
 * There's no default argument, user must declare all flags.
 * The parent directories for the output file will be automatically created if they do not exist.
 * Paths are relative to the current working directory.
 */
public class Main {

    private static final String USAGE_MESSAGE = "Usage: java Main --config <json config file> --out <output file path>";

    public static void main(String[] args) {
        
        /** Parsing & argument safe guards **/
        Parser p = new Parser(args);
        if(!p.isValid) errorExit("Invalid argument.");
        String generalConfigFile = p.generalConfigFile;
        String outputFile = p.outputFile;

        try {
            /** Create parent directories for output file if they don't exist **/
            java.nio.file.Path outputPath = Paths.get(outputFile);
            Files.createDirectories(outputPath.getParent());
            
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
            
            long startTime = System.nanoTime();
            Chromosome finalBestChromosome = ga.run(initialPopulation, reporter, maxGenerations);
            long endTime = System.nanoTime();

            /** Results exportation **/
            reporter.exportStatistics(outputFile);
            System.out.println("Problem: " + Paths.get(generalConfigFile).toAbsolutePath());
            System.out.println("Final best fitness: " + finalBestChromosome.getFitness());
            System.out.println("Execution time: " + (endTime - startTime) / 1000000.0 + " ms\n");
            
            /** Plot results **/
            String plotFile = outputFile.replace(".json", "_curve.png");
            String plotCommand = "python oop/src/plot_ga_curve.py " + outputFile + " " + plotFile;
            Process plotProcess = Runtime.getRuntime().exec(plotCommand);
            plotProcess.waitFor();
        } 
        catch(Exception e) {
            errorExit(e.getMessage());
        }
    }

    /** Argument parser helper class **/
    private static class Parser {
        private String generalConfigFile, outputFile;
        private boolean isValid;
        public Parser(String[] args) {
            isValid = true;
            if(args.length != 4) isValid = false;
            for (int i = 0; i < args.length - 1 && isValid; i += 2) {
                switch (args[i]) {
                    case "--config" -> generalConfigFile = args[i+1];
                    case "--out" -> outputFile = args[i+1];
                    default -> isValid = false;
                }
            }
            fileExistenceCheck();
            fileTypeCheck();
        }
        private void fileExistenceCheck() {
            if (!isValid) return;
            if (!Files.exists(Paths.get(generalConfigFile))) isValid = false;
        }
        private void fileTypeCheck() {
            if (!isValid) return;
            if (!generalConfigFile.endsWith(".json")) isValid = false;
        }
    }

    /** On error **/
    private static void errorExit(String msg) {
        System.err.println(msg);
        System.err.println(USAGE_MESSAGE);
        System.exit(1);
    }
}