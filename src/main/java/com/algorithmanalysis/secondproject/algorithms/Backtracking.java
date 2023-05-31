package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.utils.Measurement;

/**
 * Backtracking algorithm
 *
 * This class is responsible for implementing the backtracking algorithm
 *
 * @author Aaron González
 * @version 3.5
 */
public class Backtracking {

    /**
     * Returns all possible allele combinations of size `combinationSize'
     *
     * @param elements        The elements list
     * @param combinationSize The size of the combinations to yield
     * @return List containing all possible combinations
     */
    public static List<List<Allele>> getCombinations(List<Allele> elements, int combinationSize) {
        Measurement.incrementAssignments(1);
        List<List<Allele>> combinations = new ArrayList<>();
        System.out.println("Generating combinations");
        generateCombinations(elements, combinationSize, new ArrayList<>(), combinations, 0);
        System.out.println("Combinations successfully generated!");
        return combinations;
    }

    /**
     * Generates all possible combinations for `elements` and stores them in
     * `combinations`
     * 
     * @param elements           The elements list
     * @param combinationSize    The size of each combination
     * @param currentCombination The current combination
     * @param combinations       All the combinations that have been generated
     * @param start              The index where we are starting
     */
    private static void generateCombinations(List<Allele> elements, int combinationSize,
            List<Allele> currentCombination,
            List<List<Allele>> combinations, int start) {
        Measurement.incrementComparisons(1);
        if (combinationSize == 0) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        Measurement.incrementAssignments(1);
        for (int i = start; i <= elements.size() - combinationSize; i++) {
            Measurement.incrementComparisons(3);
            Measurement.incrementAssignments(2);
            Allele element = elements.get(i);
            if (!currentCombination.contains(element)
                    && isValidCombination(currentCombination, element)) {
                currentCombination.add(element);
                generateCombinations(elements, combinationSize - 1, currentCombination, combinations, i + 1);
                currentCombination.remove(currentCombination.size() - 1);
            }
        }
    }

    /**
     * Checks on whether or not the current combination is valid (that is, does not
     * include the same course more than once)
     * 
     * @param currentCombination The current combination
     * @param allele             The current allele
     *
     * @return Whether or not the combination was valid
     */
    private static boolean isValidCombination(List<Allele> currentCombination, Allele allele) {

        Measurement.incrementComparisons(currentCombination.size() * 2);

        return currentCombination.stream().map(Allele::getCourse)
                .noneMatch(course -> course.getName().equals(allele.getCourse().getName()))
                && currentCombination.stream().map(Allele::getProfessor)
                        .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                        .values()
                        .stream()
                        .noneMatch(count -> count > 4);
    }

}
