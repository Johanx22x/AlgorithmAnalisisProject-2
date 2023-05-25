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
    }
}
