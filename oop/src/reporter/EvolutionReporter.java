package reporter;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import models.Population;

public class EvolutionReporter {
    
    private List<GenerationStatistics> allGenerationStatistics;
    private int generationOfOptimal;
    private int totalGenerations;

    public EvolutionReporter(Population initPopulation) {
        allGenerationStatistics = new ArrayList<>();
        allGenerationStatistics.add(GenerationStatistics.statisticsOf(initPopulation, 0));
        generationOfOptimal = 0;
        totalGenerations = 0;
    }

    public void record(GenerationStatistics statistics) {
        allGenerationStatistics.add(statistics);
        totalGenerations++;
        if (totalGenerations == 1) generationOfOptimal = 0;
        else {
            int newRecordIndex = totalGenerations - 1;
            double currentBest = allGenerationStatistics.get(newRecordIndex).maxFitness;
            double previousBest = allGenerationStatistics.get(generationOfOptimal).maxFitness;   
            if (currentBest > previousBest) {
                generationOfOptimal = newRecordIndex;
            }
        }
    }

    public void exportStatistics(String destination) {
        try {
            ReportData report = new ReportData(generationOfOptimal, totalGenerations, allGenerationStatistics);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(report);
            Files.write(Paths.get(destination), json.getBytes());
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException("Failed to export statistics: " + e.getMessage(), e);
        }
    }
}

class ReportData {
    public int generationOfOptimal;
    public int totalGenerations;
    public List<GenerationStatistics> generations;
    
    ReportData(int generationOfOptimal, int totalGenerations, List<GenerationStatistics> generations) {
        this.generationOfOptimal = generationOfOptimal;
        this.totalGenerations = totalGenerations;
        this.generations = generations;
    }
}