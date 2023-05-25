package com.algorithmanalysis.secondproject;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.algorithms.Genetic;
import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.utils.TableFormat;

/**
 * Project: Algorithm Analysis Project 2
 * File Name: App.java
 *
 * Purpose: Main class for the project
 */
public class App {
    public static void main(String[] args ) {
        LoadJson loadJson = new LoadJson();
        ArrayList<ArrayList<Allele>> alleles = loadJson.getAlleles(); // returns an ArrayList<ArrayList<Allele>>
        ArrayList<Integer> populationSize = loadJson.getPopulation();
        ArrayList<Integer> totalOfProfessors = loadJson.getTotalOfProfessors();
        ArrayList<Integer> totalOfCourses = loadJson.getTotalOfCourses();

        Genetic genetic = new Genetic();
        genetic.setPopulation(alleles.get(0), populationSize.get(0));
        genetic.createChromosomes(totalOfProfessors.get(0), totalOfCourses.get(0));
        System.out.println(genetic.toString());
    }
}
