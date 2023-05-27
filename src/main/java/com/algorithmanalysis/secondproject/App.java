package com.algorithmanalysis.secondproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
        ArrayList<String> files = getFiles();

        ParsedData data = LoadJson.fromFile("data/data0.json");
        List<List<Allele>> combinations = Backtracking.getCombinations(data.alleles, data.courses);
        Optional<List<Allele>> filteredData = combinations.stream()
                .filter(combination -> {

                    HashMap<String, Integer> timesSeen = new HashMap<>();
                    HashMap<String, Integer> courseSeen = new HashMap<>();
                    for (Allele allele : combination) {
                        timesSeen.putIfAbsent(allele.getProfessor().getName(), 1);
                        timesSeen.computeIfPresent(allele.getProfessor().getName(), (k, a) -> a + 1);
                        courseSeen.putIfAbsent(allele.getCourse().getName(), 1);
                        courseSeen.computeIfPresent(allele.getCourse().getName(), (k, a) -> a + 1);
                    }

                    int max = Collections.max(timesSeen.values());
                    int maxCourse = Collections.max(courseSeen.values());

                    return combination.size() == data.courses && max <= 4 && maxCourse == 1;
                })
                .sorted(Comparator.comparingInt(combination -> -combination.stream().mapToInt(Allele::getGrade).sum()))
                .findFirst();
                // .limit(data.population)
                // .collect(Collectors.toList());
                
        if (filteredData.isPresent()) {
            Chromosome chromosome = new Chromosome(filteredData.get());
            System.out.println(chromosome);
            System.out.println(chromosome.fitness());
        } else {
            System.out.println("No solution found");
        }

        // Genetic algorithm
        System.out.println("\nGenetic algorithm:");
        
        for (String file : files) {
            Chromosome bestResult = null; // The overall best result
            System.out.println("\nFile: " + file); // Print the file name

            int count = 0;
            while (count < 5) { // Try 5 times to get the best result
                Genetic genetic = new Genetic(); // Create a new genetic object
                ErrorCodes error = runGenetic(genetic, file); // Run the genetic algorithm

                if (error == ErrorCodes.NO_ERROR) { // If there is no error
                    System.out.print("\tTry " + (int)(count+1) + ":"); // Print the try number
                                                                       
                    if (genetic.getResult() == null) { // If there is no result
                        printError(ErrorCodes.ERROR_INCAPABLE); // Print the error
                        continue;
                    }

                    System.out.println("\tThe fitness is: " + genetic.getFitness()); // Print the fitness
                                                                                     
                    if (bestResult == null || genetic.getFitness() > bestResult.fitness()) { // If the fitness is better than the best result
                        bestResult = genetic.getResult(); // Set the best result
                    }
                } else { // If there is an error
                    printError(error); // Print the error
                }

                count++; // Increase the try number
            }

            // Print the best result
            System.out.println("\nThe best result is: " + bestResult);
            System.out.println("\n\tThe best fitness is: " + bestResult.fitness()); // Print the best fitness
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
            // Selection
            Chromosome parent1 = genetic.selection();
            Chromosome parent2 = genetic.selection();

            // Crossover
            Chromosome offspring = genetic.crossover(parent1, parent2);

            if (offspring == null) {
                totalOfGenerations--;
                continue;
            }

            // Mutation
            offspring = genetic.mutation(offspring);

            // Evaluate fitness
            int offspringFitness = offspring.fitness(); // Or use a custom fitness evaluation method

            // Update population
            int leastFitIndex = 0;
            int leastFitFitness = Integer.MAX_VALUE;

            for (int i = 0; i < genetic.getPopulationSize(); i++) {
                Chromosome chromosome = genetic.getChromosome(i);
                int chromosomeFitness = chromosome.fitness(); // Or use a custom fitness evaluation method

                if (chromosomeFitness < leastFitFitness) {
                    leastFitFitness = chromosomeFitness;
                    leastFitIndex = i;
                }
            }

            if (offspringFitness > leastFitFitness) {
                genetic.getChromosome(leastFitIndex).setAlleles(offspring.getAlleles());
            }

            totalOfGenerations--; // Decrease the total of generations
        }


        genetic.setResult(genetic.getChromosome(0));
        for (int i = 1; i < genetic.getChromosomes().size(); i++) {
            if (genetic.getChromosome(i).fitness() > genetic.getChromosome(i-1).fitness()) {
                genetic.setResult(genetic.getChromosome(i));
            }
        }
        
        return ErrorCodes.NO_ERROR;
    }

    /**
     * Get the files in the data folder
     *
     * @return The files in the data folder
     */
    private static ArrayList<String> getFiles() {
        File folder = new File("data"); // Get the data folder
        File[] listOfFiles = folder.listFiles(); // Get the files in the data folder

        ArrayList<String> files = new ArrayList<>(); // Create a list of files
        for (File file : listOfFiles) { // For each file
            if (file.isFile()) { // If the file is a file
                // Add the full path of the file to the list of files 
                files.add(file.getAbsolutePath());
            }
        }

        return files; // Return the list of files
    }

    /**
     * Print ErrorCodes
     *
     * @param error The error code
     */
    static void printError(ErrorCodes error) {
        System.out.println("The program can't generate the desired result, reason: " + error);
    }
}
