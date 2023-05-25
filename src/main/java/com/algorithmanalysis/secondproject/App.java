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
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.storage.LoadJson.ParsedData;

import org.json.simple.parser.ParseException;

import com.algorithmanalysis.secondproject.algorithms.Genetic;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.storage.LoadJson.ParsedData;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;

/**
 * Project: Algorithm Analysis Project 2
 * File Name: App.java
 *
 * Purpose: Main class for the project
 */
public class App {
    public static void main(String[] args) throws IOException, ParseException {

        ParsedData data = LoadJson.fromFile("data/data1.json");
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
        ParsedData parsedData = LoadJson.fromFile("data/data0.json"); // Load the data from the json file

        Genetic genetic = new Genetic(); // Create a new genetic object
        genetic.setPopulation(parsedData.alleles); // Set the population
        genetic.setPopulationSize(parsedData.population); // Set the population size
        int totalOfProfessors = parsedData.alleles.size() / parsedData.courses; // Calculate the total of professors
        int totalOfCourses = parsedData.courses; // Calculate the total of courses
        genetic.createChromosomes(totalOfProfessors, totalOfCourses); // Create the chromosomes

        // Check if the chromosomes were created successfully
        ErrorCodes error = genetic.createChromosomes(totalOfProfessors, totalOfCourses);
        switch (error) {
            case ERROR_INCAPABLE: // The program can't generate the desired result
                System.out.println("The program can't generate the desired result");
                break;
            case MAX_ATTEMPTS_EXCEEDED: // The program couldn't generate the desired result in the maximum attempts
                System.out.println("The program can't generate the desired result");
                break;
            case NO_ERROR: // The program generated the desired result
                System.out.println("Chromosomes generated successfully for file: " + "data.json");
                System.out.println("Chromosomes: " + genetic);
                break;
        }
    }
}
