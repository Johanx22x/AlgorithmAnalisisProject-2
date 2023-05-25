package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.algorithmanalysis.secondproject.models.Allele;

public class Backtracking {

    // public static <T> List<List<T>> getCombinations(List<T> elements, int
    // combinationSize) {
    // List<List<T>> combinations = new ArrayList<>();
    // generateCombinations(elements, combinationSize, new ArrayList<>(),
    // combinations, 0);
    // return combinations;
    // }

    // private static <T> void generateCombinations(List<T> elements, int
    // combinationSize, List<T> currentCombination,
    // List<List<T>> combinations, int start) {
    // if (combinationSize == 0) {
    // combinations.add(new ArrayList<>(currentCombination));
    // return;
    // }

    // for (int i = start; i <= elements.size() - combinationSize; i++) {
    // T element = elements.get(i);
    // if (!currentCombination.contains(element)) {
    // currentCombination.add(element);
    // generateCombinations(elements, combinationSize - 1, currentCombination,
    // combinations, i + 1);
    // currentCombination.remove(currentCombination.size() - 1);
    // }
    // }
    // }

    public static List<List<Allele>> getCombinations(List<Allele> elements, int combinationSize) {
        List<List<Allele>> combinations = new ArrayList<>();
        generateCombinations(elements, combinationSize, new ArrayList<>(), combinations, 0);
        return combinations;
    }

    private static void generateCombinations(List<Allele> elements, int combinationSize,
            List<Allele> currentCombination,
            List<List<Allele>> combinations, int start) {
        if (combinationSize == 0) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = start; i <= elements.size() - combinationSize; i++) {
            Allele element = elements.get(i);
            if (!currentCombination.contains(element)
                    && isUniqueGradeCombination(currentCombination, element)) {
                currentCombination.add(element);
                generateCombinations(elements, combinationSize - 1, currentCombination, combinations, i + 1);
                currentCombination.remove(currentCombination.size() - 1);
            }
        }
    }

    private static boolean isUniqueGradeCombination(List<Allele> currentCombination, Allele allele) {
        return currentCombination.stream()
                .map(Allele::getGrade)
                .noneMatch(grade -> grade.equals(allele.getGrade()));
    }

}
