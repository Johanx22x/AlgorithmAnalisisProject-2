package com.algorithmanalysis.secondproject;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.algorithms.Genetic;
import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.utils.TableFormat;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;

/**
 * Project: Algorithm Analysis Project 2
 * File Name: App.java
 *
 * Purpose: Main class for the project
 */
public class App {
    public static void main(String[] args) {
        LoadJson loadJson = new LoadJson();
        ArrayList<ArrayList<Allele>> alleles = loadJson.getAlleles(); // returns an ArrayList<ArrayList<Allele>>
        ArrayList<Integer> populationSize = loadJson.getPopulation();
        ArrayList<Integer> totalOfProfessors = loadJson.getTotalOfProfessors();
        ArrayList<Integer> totalOfCourses = loadJson.getTotalOfCourses();
        System.out.println(totalOfCourses);
        ArrayList<String> files = loadJson.getFiles();

        Genetic genetic = new Genetic(); // Create a new genetic object
        for (ArrayList<Allele> allele : alleles) {
            genetic.setPopulation(allele); // Set the population
            genetic.setPopulationSize(populationSize.get(alleles.indexOf(allele))); // Set the population size
            genetic.createChromosomes(totalOfProfessors.get(alleles.indexOf(allele)), totalOfCourses.get(alleles.indexOf(allele))); // Create the chromosomes

            // Check if the chromosomes were created successfully
            ErrorCodes error = genetic.createChromosomes(totalOfProfessors.get(alleles.indexOf(allele)), totalOfCourses.get(alleles.indexOf(allele)));
            switch (error) {
                case ERROR_INCAPABLE: // The program can't generate the desired result
                    System.out.println("The program can't generate the desired result");
                    break;
                case MAX_ATTEMPTS_EXCEEDED: // The program couldn't generate the desired result in the maximum attempts
                    System.out.println("The program can't generate the desired result");
                    break;
                case NO_ERROR: // The program generated the desired result
                    System.out.println("Chromosomes generated successfully for file: " + files.get(alleles.indexOf(allele)));
                    System.out.println("Chromosomes: " + genetic);
                    break;
            }
        }
    }
}
