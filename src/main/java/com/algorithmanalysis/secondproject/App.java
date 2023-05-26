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


        // Genetic algorithm
        
        String fileName = "data/data0.json"; // File name
        Genetic genetic = new Genetic(); // Create a new genetic object
        ErrorCodes error = runGenetic(genetic, fileName); // Run the genetic algorithm
        if (error == ErrorCodes.NO_ERROR) { // If there is no error
            System.out.println("The result is: " + genetic.getResult()); // Print the result
        } else { // If there is an error
            System.out.println("The program can't generate the desired result, reason: " + error); // Print the error
        }
    }

    /**
     * Run the genetic algorithm 
     *
     * @param genetic The genetic object
     * @param fileName The file name
     * @return The error code
     */
    private static ErrorCodes runGenetic(Genetic genetic, String fileName) throws IOException, ParseException {
        ParsedData parsedData = LoadJson.fromFile(fileName); // Load the data from the file

        genetic.setPopulation(parsedData.alleles); // Set the population
        genetic.setPopulationSize(parsedData.population); // Set the population size
        genetic.setTotalOfProfessors(parsedData.alleles.size() / parsedData.courses); // Set the total of professors
        genetic.setTotalOfCourses(parsedData.courses); // Set the total of courses

        // Check if the chromosomes were created successfully
        ErrorCodes error = genetic.createChromosomes();
        if (error != ErrorCodes.NO_ERROR) {
            return error;
        }

        int totalOfGenerations = genetic.getTotalOfGenerations(); // Get the total of generations

        // Start the genetic algorithm
        while (totalOfGenerations > 0) {

            // Algorithm logic here

            totalOfGenerations--; // Decrease the total of generations
        }

        Chromosome parent1 = genetic.selection();
        System.out.println("Parent 1: " + parent1);

        Chromosome parent2 = genetic.selection();
        System.out.println("Parent 2: " + parent2);

        // Perform crossover with the selected parents
        Chromosome offspring = genetic.crossover(parent1, parent2);
        System.out.println("Offspring: " + offspring);

        if (offspring != null) {
            System.out.println("The result is valid according to the requirements");

            // Perform mutation with the offspring 
            Chromosome mutatedOffspring = genetic.mutation(offspring);
            
            if (mutatedOffspring != null) {
                System.out.println("Mutated offspring: " + mutatedOffspring);
            } else {
                System.out.println("The offspring couldn't be mutated");
            }
        }
        
        return ErrorCodes.NO_ERROR;
    }
}
