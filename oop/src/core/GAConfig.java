package core;

import fitnesses.*;
import models.*;
import strategies.*;
import utils.RandomUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GAConfig {
    private int populationSize;
    private int chromosomeLength;
    private int maxGenerations;
    private int randomSeed;
    private FitnessEvaluator fitnessEvaluator;
    private ElitismStrategy elitismStrategy;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;
    // private RandomUtil randomGenerator;
    /** Strategies are stateless so all class can use the same strategy object, the oppsite for RandomUtil */

    public GAConfig(String configFile) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(configFile)));
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(content, JsonObject.class);
            
            // Parse primitive fields
            this.populationSize = json.get("populationSize").getAsInt();
            this.chromosomeLength = json.get("chromosomeLength").getAsInt();
            this.maxGenerations = json.get("maxGenerations").getAsInt();
            this.randomSeed = json.get("randomSeed").getAsInt();
            
            // Parse and initialize strategies
            JsonObject selectionObj = json.getAsJsonObject("selection");
            String selectionStrategy = selectionObj.get("strategy").getAsString();
            int tournamentSize = selectionObj.get("size").getAsInt();
            this.selectionStrategy = createSelectionStrategy(selectionStrategy, tournamentSize);
            
            JsonObject crossoverObj = json.getAsJsonObject("crossover");
            String crossoverStrategy = crossoverObj.get("strategy").getAsString();
            double crossoverRate = crossoverObj.get("rate").getAsDouble();
            this.crossoverStrategy = createCrossoverStrategy(crossoverStrategy, crossoverRate);
            
            JsonObject mutationObj = json.getAsJsonObject("mutation");
            String mutationStrategy = mutationObj.get("strategy").getAsString();
            double mutationRate = mutationObj.get("ratePerBit").getAsDouble();
            this.mutationStrategy = createMutationStrategy(mutationStrategy, mutationRate);
            
            JsonObject elitismObj = json.getAsJsonObject("elitismStrategy");
            String elitismStrategy = elitismObj.get("strategy").getAsString();
            int elitismCount = elitismObj.get("count").getAsInt();
            this.elitismStrategy = createElitismStrategy(elitismStrategy, elitismCount);
            
            // Parse fitness evaluator based on problem
            JsonObject problemObj = json.getAsJsonObject("problem");
            String problemName = problemObj.get("name").getAsString();
            this.fitnessEvaluator = createFitnessEvaluator(problemName, problemObj);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse config file: " + e.getMessage(), e);
        }
    }
    
    private SelectionStrategy createSelectionStrategy(String strategy, int size) {
        return switch (strategy) {
            case "tournamentSelection" -> new TournamentSelection(size);
            default -> throw new IllegalArgumentException("Unknown selection strategy: " + strategy);
        };
    }
    
    private CrossoverStrategy createCrossoverStrategy(String strategy, double rate) {
        return switch (strategy) {
            case "onePointCrossover" -> new OnePointCrossover(rate);
            default -> throw new IllegalArgumentException("Unknown crossover strategy: " + strategy);
        };
    }
    
    private MutationStrategy createMutationStrategy(String strategy, double rate) {
        return switch (strategy) {
            case "bitFlipMutation" -> new BitFlipMutation(rate);
            default -> throw new IllegalArgumentException("Unknown mutation strategy: " + strategy);
        };
    }
    
    private ElitismStrategy createElitismStrategy(String strategy, int count) {
        return switch (strategy) {
            case "simpleElitism" -> new SimpleElitism(count);
            default -> throw new IllegalArgumentException("Unknown elitism strategy: " + strategy);
        };
    }
    
    private FitnessEvaluator createFitnessEvaluator(String problem, JsonObject problemObj) {
        return switch (problem) {
            case "knapsack" -> {
                double[] weights = new Gson().fromJson(problemObj.get("weights"), double[].class);
                double[] values = new Gson().fromJson(problemObj.get("values"), double[].class);
                double capacityOverTotal = problemObj.get("capacityOverTotal").getAsDouble();
                yield new KnapsackFitnessEvaluator(chromosomeLength, weights, values, capacityOverTotal);
            }
            case "onemax" -> new OneMaxFitnessEvaluator(chromosomeLength);
            default -> throw new IllegalArgumentException("Unknown problem: " + problem);
        };
    }

    public FitnessEvaluator fitnessEvaluator() {
        return fitnessEvaluator;
    }
    public ElitismStrategy elitismStrategy() {
        return elitismStrategy;
    }
    public SelectionStrategy selectionStrategy() {
        return selectionStrategy;
    }
    public CrossoverStrategy crossoverStrategy() {
        return crossoverStrategy;
    }
    public MutationStrategy mutationStrategy() {
        return mutationStrategy;
    }
    public RandomUtil randomUtil() {
        return new RandomUtil(randomSeed);
    }
    public Population generateRandomPopulation() {
        return null;
    }
    public int populationSize() {
        return populationSize;
    }
    public int chromosomeLength() {
        return chromosomeLength;
    }
    public int maxGenerations() {
        return maxGenerations;
    }
    public int randomSeed() {
        return randomSeed;
    }
    // public int tournamentSize() {
    //     return 0;
    // }
    // public double crossoverRate() {
    //     return 0.0;
    // }
    // public double mutationRate() {
    //     return 0.0;
    // }
    // public int elitismCount() {
    //     return 0;
    // }
    // public double maxFitness() {
    //     return 0.0;
    // }
}