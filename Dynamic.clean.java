package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;
import com.algorithmanalysis.secondproject.App;

/**
 * Dynamic
 * 
 * This class is used to solve the problem using dynamic programming.
 * 
 * @author Johan Rodriguez
 * @version 1.2
 */
public class Dynamic {
    /**
     * Run the dynamic algorithm
     *
     * 14n² + 45nm + 65n + 4m + 39 -> O(n²)
     * 
     * @param alleles           The alleles to be used
     * @param totalOfCourses    The total of totalOfCourses
     * @param totalOfProfessors The total of totalOfProfessors
     */
    public static ErrorCodes runDynamicAlgorithm(ArrayList<Allele> alleles, int totalOfCourses, int totalOfProfessors) {
        List<List<Integer>> transposedMatrix = new ArrayList<>();
        for (int i = 0; i < totalOfCourses; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < totalOfProfessors; j++) {
                row.add(0);
            }
            transposedMatrix.add(row);
        }
        for (Allele allele : alleles) {
            transposedMatrix.get(allele.getCourse().getIndex()).set(allele.getProfessor().getIndex(),
                    allele.getGrade());
        }
        List<List<Integer>> possibilitiesMatrix = new ArrayList<>();
        List<List<Integer>> backupMatrix = new ArrayList<>();
        for (List<Integer> row : transposedMatrix) {
            List<Integer> column = new ArrayList<>();
            List<Integer> backupColumn = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                column.add(row.get(i));
                backupColumn.add(row.get(i));
            }
            possibilitiesMatrix.add(column);
            backupMatrix.add(backupColumn);
        }
        if (!checkMatrix(transposedMatrix)) {
            return ErrorCodes.ERROR_INCAPABLE;
        }
        List<Integer> bestCombination = new ArrayList<>();
        for (int i = 0; i < transposedMatrix.size(); i++) {
            bestCombination.add(-1);
        }
        for (int i = 0; i < transposedMatrix.size(); i++) {
            List<Integer> row = transposedMatrix.get(i);
            if (Collections.frequency(row, -1) == row.size() - 1) {
                int teacher = row.indexOf(row.stream().filter(g -> g != -1).findFirst().orElse(-1));
                bestCombination.set(i, teacher);
                possibilitiesMatrix.get(i).set(teacher, -1);
            }
        }
        List<Integer> difference = calculateDifference(transposedMatrix, bestCombination);
        int count = 0;
        System.out.println("\nFinding the best combination...");
        while (bestCombination.contains(-1)) {
            if (count % 5 == 0 && App.verbose) {
                System.out.println("\tStage " + count + ": " + bestCombination);
            }
            int index = maxIndex(difference);
            List<Integer> row = transposedMatrix.get(index);
            int professor = row.indexOf(Collections.max(row));
            if (countOccurrences(bestCombination, professor) < 4) {
                bestCombination.set(index, professor);
                possibilitiesMatrix.get(index).set(professor, -1);
            } else {
                row.set(professor, -1);
                possibilitiesMatrix.get(index).set(professor, -1);
            }
            difference = calculateDifference(transposedMatrix, bestCombination);
            count++;
        }
        System.out.println("\tFinal stage: " + bestCombination);
        List<Integer> result = new ArrayList<>();
        int fitness = 0;
        for (int i = 0; i < bestCombination.size(); i++) {
            result.add(backupMatrix.get(i).get(bestCombination.get(i)));
            fitness += backupMatrix.get(i).get(bestCombination.get(i));
        }
        System.out.println("Doing interchanges...");
        while (result.contains(-1)) {
            bestCombination = interchangeCourses(possibilitiesMatrix, bestCombination, result);
            result.clear();
            fitness = 0;
            for (int i = 0; i < bestCombination.size(); i++) {
                result.add(backupMatrix.get(i).get(bestCombination.get(i)));
                fitness += backupMatrix.get(i).get(bestCombination.get(i));
            }
        }
        System.out.println("\nBest professors combination: " + bestCombination);
        System.out.println("Best grades combination: " + result);
        System.out.println("\n\tOverall calification (fitness): " + fitness);
        return ErrorCodes.NO_ERROR;
    }

    public static int maxIndex(List<Integer> array) {
        int maxIndex = 0;
        int maxValue = array.get(0);

        for (int i = 1; i < array.size(); i++) {

            if (array.get(i) > maxValue) {
                maxValue = array.get(i);
                maxIndex = i;

            }
        }

        return maxIndex;
    }

    public static List<Integer> interchangeCourses(List<List<Integer>> possibilitiesMatrix,
            List<Integer> bestCombination,
            List<Integer> result) {

        if (result.contains(-1)) {

            int index = result.indexOf(-1);

            List<Integer> column = new ArrayList<>();

            for (List<Integer> row : possibilitiesMatrix) {

                column.add(row.get(index));

            }

            int bestInterchange = maxIndex(column);

            int professor = bestCombination.get(index);
            int professorToInterchange = bestCombination.get(bestInterchange);

            if (countOccurrences(bestCombination, professorToInterchange) < 5) {

                bestCombination.set(index, professorToInterchange);
                bestCombination.set(bestInterchange, professor);

                System.out.println("\tInterchange found: " + bestCombination);
            } else {
                possibilitiesMatrix.get(bestInterchange).set(professor, -1);
                bestCombination = interchangeCourses(possibilitiesMatrix, bestCombination, result);

            }
        }
        return bestCombination;
    }

    public static boolean checkMatrix(List<List<Integer>> tMatrix) {

        for (List<Integer> row : tMatrix) {

            if (Collections.frequency(row, -1) == row.size()) {
                return false;
            }
        }

        for (List<Integer> row : tMatrix) {

            if (Collections.frequency(row, -1) == row.size()) {
                return false;
            }
        }

        if (tMatrix.get(0).size() * 4 < tMatrix.size()) {
            return false;
        }

        return true;
    }

    public static List<Integer> calculateDifference(List<List<Integer>> matrix, List<Integer> bestCombination) {

        List<Integer> difference = new ArrayList<>();

        for (int i = 0; i < matrix.size(); i++) {

            if (bestCombination.get(i) == -1) {
                List<Integer> row = matrix.get(i);
                row = new ArrayList<>(row);
                row.sort(Collections.reverseOrder());
                int maxGrade = row.get(0);
                int secondMaxGrade = row.get(1);
                difference.add((maxGrade - secondMaxGrade) * Math.abs(maxGrade * secondMaxGrade));

            } else {
                difference.add(-1);

            }
        }
        return difference;
    }

    public static int countOccurrences(List<Integer> list, int element) {

        int count = 0;

        for (int i : list) {

            if (i == element) {
                count++;

            }
        }
        return count;
    }
}
