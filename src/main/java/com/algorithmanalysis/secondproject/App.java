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

        for (ArrayList<Allele> allele : alleles) {
            Genetic genetic = new Genetic();
            genetic.setPopulation(allele, populationSize.get(alleles.indexOf(allele)));
            genetic.createChromosomes(totalOfProfessors.get(alleles.indexOf(allele)), totalOfCourses.get(alleles.indexOf(allele)));
            ErrorCodes error = genetic.createChromosomes(totalOfProfessors.get(alleles.indexOf(allele)), totalOfCourses.get(alleles.indexOf(allele)));
            switch (error) {
                case ERROR_INCAPABLE:
                    System.out.println("The program can't generate the desired result");
                    break;
                case MAX_ATTEMPTS_EXCEEDED:
                    System.out.println("The program can't generate the desired result");
                    break;
                case NO_ERROR:
                    System.out.println("Chromosomes generated successfully for file: " + files.get(alleles.indexOf(allele)));
                    System.out.println("Chromosomes: " + genetic);
                    break;
            }
        }
    }
}
