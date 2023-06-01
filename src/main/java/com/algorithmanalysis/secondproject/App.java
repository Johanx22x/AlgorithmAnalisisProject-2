package com.algorithmanalysis.secondproject;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.json.simple.parser.ParseException;

import com.algorithmanalysis.secondproject.algorithms.Backtracking;
import com.algorithmanalysis.secondproject.algorithms.Backtracking2;
import com.algorithmanalysis.secondproject.algorithms.Dynamic;
import com.algorithmanalysis.secondproject.algorithms.Genetic;
import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.storage.LoadJson.ParsedData;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;
import com.algorithmanalysis.secondproject.utils.Measurement;

import guru.nidi.graphviz.engine.Format;
import moe.leer.codeflowcore.CodeFlow;

/**
 * Project: Algorithm Analysis Project 2
 * File Name: App.java
 *
 * Purpose: Main class for the project
 *
 * @version 2.1
 */
public class App {
    public static boolean verbose = true; // Verbose mode

    public static void main(String[] args) throws IOException, ParseException {
        // Get the files names
        ArrayList<String> files = getFiles();

        // Genetic algorithm
        System.out.println("\nGenetic algorithm:");

        for (String file : files) {
            Measurement.reset(); // Reset the measurements
                                 
            Chromosome bestResult = null; // The overall best result
            System.out.println("\nFile: " + file); // Print the file name
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each try

            Genetic genetic = new Genetic(); // Create a new genetic object
            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the genetic object
                                                 
            ErrorCodes error = Genetic.runGenetic(genetic, file); // Run the genetic algorithm
            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the error code

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (error == ErrorCodes.NO_ERROR) { // If there is no error

                Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                if (genetic.getResult() == null) { // If there is no result
                    System.out.println();
                    printError(ErrorCodes.ERROR_INCAPABLE); // Print the error
                    System.out.println();
                    continue; // Continue to the next file
                }
                
                Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                if (bestResult == null || genetic.getFitness() > bestResult.fitness()) { // If the fitness is better
                                                                                         // than the best result
                    bestResult = genetic.getResult(); // Set the best result
                    Measurement.incrementAssignments(1); // Increment the assignments by 1 for the best result
                }
            } else { // If there is an error
                System.out.println();
                printError(error); // Print the error
                System.out.println();
                continue; // Continue to the next file
            }

            // Write crossoverBuffer and mutationBuffer to file fileName+"_crossoverBuffer.txt" and fileName+"_mutationBuffer.txt"
            String crossoverBufferFile = genetic.getCrossoverBuffer();
            String mutationBufferFile = genetic.getMutationBuffer();
            String crossoverBufferFileName = file.substring(0, file.length() - 5) + "_crossoverBuffer.txt";
            String mutationBufferFileName = file.substring(0, file.length() - 5) + "_mutationBuffer.txt";
            System.out.println("\n\tWriting crossover buffer to file: " + crossoverBufferFileName);
            System.out.println("\tWriting mutation buffer to file: " + mutationBufferFileName);
            // If the file not exists, create it 
            File crossoverBufferFileObject = new File(crossoverBufferFileName);
            File mutationBufferFileObject = new File(mutationBufferFileName);

            // Create files
            crossoverBufferFileObject.createNewFile();
            mutationBufferFileObject.createNewFile();

            // Write the crossover buffer to the file
            FileWriter crossoverBufferFileWriter = new FileWriter(crossoverBufferFileObject);
            crossoverBufferFileWriter.write(crossoverBufferFile);
            crossoverBufferFileWriter.close();

            // Write the mutation buffer to the file 
            FileWriter mutationBufferFileWriter = new FileWriter(mutationBufferFileObject);
            mutationBufferFileWriter.write(mutationBufferFile);
            mutationBufferFileWriter.close();

            // Print the best result
            System.out.println("\nThe best result is: " + bestResult);
            System.out.println("\n\tThe best fitness is: " + bestResult.fitness()); // Print the best fitness

            // Print the best 5 results 
            if (verbose) {
                System.out.println("\n\tThe best 5 results are: ");
                for (Chromosome chromosome : Genetic.getBestChromosomes()) {
                    System.out.println("\t\t" + chromosome);
                    System.out.println("\t\tFitness: " + chromosome.fitness());
                }
            }
            
            // Print the measurements
            System.out.println("\n" + Measurement.getMeasurement());
            System.out.println();
        }
        
        // Dynamic algorithm
        System.out.println("\nDynamic algorithm:");
        for (String file : files) {
            Measurement.reset(); // Reset the measurements
            System.out.println("\nFile: " + file); // Print the file name

            // Load the data from the file
            ParsedData parsedData = LoadJson.fromFile(file);
            
            Measurement.setSize(parsedData.alleles.size()); // Set the size of the matrix

            // Create a matrix with the data
            ErrorCodes error = Dynamic.runDynamicAlgorithm(parsedData.alleles, parsedData.courses,
                    parsedData.alleles.size() / parsedData.courses); // Run the genetic algorithm

            if (!(error == ErrorCodes.NO_ERROR)) { // If there is an error
                System.out.println();
                printError(error); // Print the error
                System.out.println();
                continue; // Continue to the next file
            }

            // Print the measurements
            System.out.println("\n" + Measurement.getMeasurement());
            System.out.println();
        }

        // Backtracking algorithm
        // System.out.println("\nBacktracking algorithm");
        // // for (String file : files) {
        // Measurement.reset(); // Reset the measurements
        // // System.out.println("\nFile: " + file); // Print the file name
                                               
        // String file = "data/data1.json";
        // ParsedData data = LoadJson.fromFile(file);
        // Optional<List<Allele>> filteredData = Backtracking.getCombinations(data.alleles,
        //         data.courses).stream()
        //         .sorted(Comparator.comparingInt(combination -> -combination.stream().mapToInt(Allele::getGrade).sum()))
        //         .findFirst();

        // System.out.println(filteredData);

        // if (filteredData.isPresent()) {
        //     Chromosome chromosome = new Chromosome(filteredData.get());
        //     System.out.println(chromosome);
        //     System.out.println(chromosome.fitness());
        // } else {
        //     System.out.println("No solution found");
        // }

        // // Print the measurements
        // System.out.println("\n" + Measurement.getMeasurement());
        // System.out.println();
        // }
        
        // Backtracking algorithm (second approach)
        // System.out.println("\nBacktracking algorithm (second approach):");
        // for (String file : files) {
            // String file = "data/data0.json";
            // Measurement.reset(); // Reset the measurements
            // System.out.println("\nFile: " + file); // Print the file name

            // // Load the data from the file
            // ParsedData parsedData = LoadJson.fromFile(file);

            // // Create a matrix with the data
            // Backtracking2.runBacktracking(parsedData.alleles, parsedData.courses,
            //         parsedData.alleles.size() / parseprofessors dData.courses); // Run the genetic algorithm

            // // Print the measurements
            // System.out.println("\n" + Measurement.getMeasurement());
            // System.out.println();
        // }
    }

    /**
     * Get the files in the data folder
     *
     * @return The files in the data folder
     */
    private static ArrayList<String> getFiles() {
        File folder = new File("data"); // Get the data folder
        // Get file with extension .json
        FilenameFilter jsonFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        };
        File[] listOfFiles = folder.listFiles(jsonFilter); // Get the files in the data folder

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
