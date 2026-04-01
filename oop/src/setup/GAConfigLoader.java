package setup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import fitnesses.*;
import strategies.*;

public class GAConfigLoader {
    public static GAConfig load(String configFile) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(configFile)));
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(content, JsonObject.class);
            
            // Parse primitive fields
            int populationSize = json.get("populationSize").getAsInt();
            int chromosomeLength = json.get("chromosomeLength").getAsInt();
            int maxGenerations = json.get("maxGenerations").getAsInt();
            int randomSeed = json.get("randomSeed").getAsInt();
            
            // Parse and initialize strategies
            JsonObject selectionObj = json.getAsJsonObject("selection");
            String selectionType = selectionObj.get("strategy").getAsString();
            SelectionStrategy selection = createSelectionStrategy(selectionType, selectionObj);
            
            JsonObject crossoverObj = json.getAsJsonObject("crossover");
            String crossoverType = crossoverObj.get("strategy").getAsString();
            CrossoverStrategy crossover = createCrossoverStrategy(crossoverType, crossoverObj);
            
            JsonObject mutationObj = json.getAsJsonObject("mutation");
            String mutationType = mutationObj.get("strategy").getAsString();
            MutationStrategy mutation = createMutationStrategy(mutationType, mutationObj);
            
            JsonObject elitismObj = json.getAsJsonObject("elitismStrategy");
            String elitismType = elitismObj.get("strategy").getAsString();
            ElitismStrategy elitism = createElitismStrategy(elitismType, elitismObj);
            
            // Parse fitness evaluator based on problem
            JsonObject problemObj = json.getAsJsonObject("problem");
            String problemName = problemObj.get("name").getAsString();
            FitnessEvaluator fitnessEvaluator = createFitnessEvaluator(problemName, problemObj, chromosomeLength);
            
            return new GAConfig(populationSize, chromosomeLength, maxGenerations, randomSeed, 
                                fitnessEvaluator, elitism, selection, crossover, mutation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse config file: " + e.getMessage(), e);
        }
    }

    private static SelectionStrategy createSelectionStrategy(String strategy, JsonObject obj) {
        return switch (strategy) {
            case "tournamentSelection" -> {
                int tournamentSize = obj.get("size").getAsInt();
                yield new TournamentSelection(tournamentSize);
            }
            default -> throw new IllegalArgumentException("Unknown selection strategy: " + strategy);
        };
    }
    
    private static CrossoverStrategy createCrossoverStrategy(String strategy, JsonObject obj) {
        return switch (strategy) {
            case "onePointCrossover" -> {
                double crossoverRate = obj.get("rate").getAsDouble();
                yield new OnePointCrossover(crossoverRate);
            }
            default -> throw new IllegalArgumentException("Unknown crossover strategy: " + strategy);
        };
    }
    
    private static MutationStrategy createMutationStrategy(String strategy, JsonObject obj) {
        return switch (strategy) {
            case "bitFlipMutation" -> {
                double mutationRate = obj.get("ratePerBit").getAsDouble();
                yield new BitFlipMutation(mutationRate);
            }
            default -> throw new IllegalArgumentException("Unknown mutation strategy: " + strategy);
        };
    }
    
    private static ElitismStrategy createElitismStrategy(String strategy, JsonObject obj) {
        return switch (strategy) {
            case "simpleElitism" -> {
                int elitismCount = obj.get("count").getAsInt();
                yield new SimpleElitism(elitismCount);
            }
            default -> throw new IllegalArgumentException("Unknown elitism strategy: " + strategy);
        };
    }
    
    private static FitnessEvaluator createFitnessEvaluator(String problem, JsonObject problemObj, int chromosomeLength) {
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
}