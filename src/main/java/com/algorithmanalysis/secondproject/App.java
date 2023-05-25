package com.algorithmanalysis.secondproject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;

import com.algorithmanalysis.secondproject.algorithms.Backtracking;
import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.storage.LoadJson.ParsedData;

import com.algorithmanalysis.secondproject.algorithms.Genetic;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;

/**
 * Project: Algorithm Analysis Project 2
 * File Name: App.java
 *
 * Purpose: Main class for the project
 */
public class App {
    public static void main(String[] args) throws IOException, ParseException {
        ParsedData data = LoadJson.fromFile("data/data0.json");
        List<List<Allele>> combinations = Backtracking.getCombinations(data.alleles, data.courses);
        List<List<Allele>> filteredData = combinations.stream()
                .filter(combination -> {

                    HashMap<String, Integer> timesSeen = new HashMap<>();
                    for (Allele allele : combination) {
                        timesSeen.putIfAbsent(allele.getProfessor().getName(), 1);
                        timesSeen.computeIfPresent(allele.getProfessor().getName(), (k, a) -> a + 1);
                    }

                    int max = Collections.max(timesSeen.values());
                    return combination.size() == data.courses && max <= 4;
                })
                .sorted(Comparator.comparingInt(combination -> -combination.stream().mapToInt(Allele::getGrade).sum()))
                .limit(data.population)
                .collect(Collectors.toList());
        System.out.println(filteredData);
        
        String fileName = "data/data0.json"; // File name
        ParsedData parsedData = LoadJson.fromFile(fileName); // Load the data from the file

        Genetic genetic = new Genetic(); // Create a new genetic object
        genetic.setPopulation(parsedData.alleles); // Set the population
        genetic.setPopulationSize(parsedData.population); // Set the population size
        genetic.setTotalOfProfessors(parsedData.alleles.size() / parsedData.courses); // Set the total of professors
        genetic.setTotalOfCourses(parsedData.courses); // Set the total of courses

        // Check if the chromosomes were created successfully
        ErrorCodes error = genetic.createChromosomes();
        switch (error) {
            case ERROR_INCAPABLE: // The program can't generate the desired result
                System.out.println("The program can't generate the desired result, reason: Invalid data");
                break;
            case MAX_ATTEMPTS_EXCEEDED: // The program couldn't generate the desired result in the maximum attempts
                System.out.println("The program can't generate the desired result, reason: Maximum attempts exceeded");
                break;
            case NO_ERROR: // The program generated the desired result
                System.out.println("The program generated the desired result for the file: " + fileName);
                System.out.println("Chromosomes: " + genetic);
                break;
        }

        System.out.println("\nCrossing chromosome: \n");
        System.out.println(genetic.getChromosome(1));

        System.out.println("\nwith:\n");
        System.out.println(genetic.getChromosome(2));
        System.out.println("\nResult:\n");

        Chromosome result = genetic.crossover(genetic.getChromosome(1), genetic.getChromosome(2));
        System.out.println(result);

        if (result == null) {
            System.out.println("The program can't generate the desired result, reason: Invalid data");
            return;
        }
    }
}
