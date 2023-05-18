package com.algorithmanalysis.secondproject;

import java.util.ArrayList;

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
        // loadJson.getPopulation() // returns an ArrayList<String>
        
        for (ArrayList<Allele> allele : alleles) {
            System.out.println(TableFormat.getTableCSV(allele));
        }
    }
}
