package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;
import com.algorithmanalysis.secondproject.utils.Measurement;
import com.algorithmanalysis.secondproject.App;

public class Dynamic {
    /**
     * Run the dynamic algorithm 
     *
     * @param alleles           The alleles to be used
     * @param totalOfCourses    The total of totalOfCourses 
     * @param totalOfProfessors The total of totalOfProfessors 
     */
    public static ErrorCodes runDynamicAlgorithm(ArrayList<Allele> alleles, int totalOfCourses, int totalOfProfessors) {
        Measurement.incrementAssignments(3); // alleles, totalOfCourses, totalOfProfessors
        // Create the matrix
        List<List<Integer>> transposedMatrix = new ArrayList<>();
        Measurement.incrementAssignments(1); // transposedMatrix = new ArrayList<>();
                                             
        Measurement.incrementAssignments(1); // i = 0
        for (int i = 0; i < totalOfCourses; i++) {
            Measurement.incrementComparisons(1); // i < totalOfCourses
                                                 
            List<Integer> row = new ArrayList<>();
            Measurement.incrementAssignments(1); // row = new ArrayList<>();

            Measurement.incrementAssignments(1); // j = 0
            for (int j = 0; j < totalOfProfessors; j++) {
                Measurement.incrementComparisons(1); // j < totalOfProfessors

                row.add(0);
                Measurement.incrementAssignments(1); // row.add(0);
            }
            Measurement.incrementComparisons(1); // j < totalOfProfessors
                                                 
            transposedMatrix.add(row);
            Measurement.incrementAssignments(1); // transposedMatrix.add(row);
        }
        Measurement.incrementComparisons(1); // i < totalOfCourses

        // Fill the transposedMatrix with the grades
        for (Allele allele : alleles) {
            Measurement.incrementComparisons(1); // allele : alleles
            transposedMatrix.get(allele.getCourse().getIndex()).set(allele.getProfessor().getIndex(), allele.getGrade());
            Measurement.incrementAssignments(1); // transposedMatrix.get(allele.getCourse().getIndex()).set(allele.getProfessor().getIndex(), allele.getGrade());
        }

        List<List<Integer>> possibilitiesMatrix = new ArrayList<>();
        List<List<Integer>> backupMatrix = new ArrayList<>();
        Measurement.incrementAssignments(2); // possibilitiesMatrix = new ArrayList<>(); backupMatrix = new ArrayList<>();

        for (List<Integer> row : transposedMatrix) {
            Measurement.incrementAssignments(1); // row : transposedMatrix
            List<Integer> column = new ArrayList<>();
            List<Integer> backupColumn = new ArrayList<>();
            Measurement.incrementAssignments(2); // column = new ArrayList<>(); backupColumn = new ArrayList<>();

            Measurement.incrementAssignments(1); // i = 0
            for (int i = 0; i < row.size(); i++) {
                Measurement.incrementComparisons(1); // i < row.size()

                column.add(row.get(i));
                backupColumn.add(row.get(i));
                Measurement.incrementAssignments(2); // column.add(row.get(i)); backupColumn.add(row.get(i));
            }
            Measurement.incrementComparisons(1); // i < row.size()
                                                 
            possibilitiesMatrix.add(column);
            backupMatrix.add(backupColumn);
            Measurement.incrementAssignments(2); // possibilitiesMatrix.add(column); backupMatrix.add(backupColumn);
        }
        Measurement.incrementComparisons(1); // row : transposedMatrix

        Measurement.incrementComparisons(1); // i < transposedMatrix.size()
        if (!checkMatrix(transposedMatrix)) {
            return ErrorCodes.ERROR_INCAPABLE;
        }

        List<Integer> bestCombination = new ArrayList<>();
        Measurement.incrementAssignments(1); // bestCombination = new ArrayList<>();

        Measurement.incrementAssignments(1); // i = 0
        for (int i = 0; i < transposedMatrix.size(); i++) {
            Measurement.incrementComparisons(1); // i < transposedMatrix.size()

            bestCombination.add(-1);
            Measurement.incrementAssignments(1); // bestCombination.add(-1);
        }
        Measurement.incrementComparisons(1); // i < transposedMatrix.size()

        Measurement.incrementAssignments(1); // i = 0
        // Check if there is a course with only one teacher available
        // For example: [-1, 8, -1] -> 8
        for (int i = 0; i < transposedMatrix.size(); i++) {
            Measurement.incrementComparisons(1); // i < transposedMatrix.size()

            List<Integer> row = transposedMatrix.get(i);
            Measurement.incrementAssignments(1); // row = transposedMatrix.get(i);

            Measurement.incrementComparisons(1); // Collections.frequency(row, -1) == row.size() - 1
            if (Collections.frequency(row, -1) == row.size() - 1) {
                // Assign the teacher with grade different than -1 to the course
                // For example: [-1, 8, -1] -> 1
                int teacher = row.indexOf(row.stream().filter(g -> g != -1).findFirst().orElse(-1));
                bestCombination.set(i, teacher);
                possibilitiesMatrix.get(i).set(teacher, -1);
                Measurement.incrementAssignments(3); // bestCombination.set(i, teacher); possibilitiesMatrix.get(i).set(teacher, -1); Measurement.incrementAssignments(3); // bestCombination.set(i, teacher); possibilitiesMatrix.get(i).set(teacher, -1);
            }
        }
        Measurement.incrementComparisons(1); // i < transposedMatrix.size()

        List<Integer> difference = calculateDifference(transposedMatrix, bestCombination);
        Measurement.incrementAssignments(1); // difference = calculateDifference(transposedMatrix, bestCombination);

        int count = 0;
        Measurement.incrementAssignments(1); // count = 0
        System.out.println("\nFinding the best combination...");
        while (bestCombination.contains(-1)) {
            Measurement.incrementComparisons(1); // bestCombination.contains(-1)
            if (count % 5 == 0 && App.verbose) {
                System.out.println("\tStage " + count + ": " + bestCombination);
            }

            int index = maxIndex(difference);
            Measurement.incrementAssignments(1); // index = maxIndex(difference);

            List<Integer> row = transposedMatrix.get(index);
            int professor = row.indexOf(Collections.max(row));
            Measurement.incrementAssignments(2); // professor = row.indexOf(Collections.max(row));

            Measurement.incrementComparisons(1); // countOccurrences(bestCombination, professor) < 4
            if (countOccurrences(bestCombination, professor) < 4) {
                bestCombination.set(index, professor);
                possibilitiesMatrix.get(index).set(professor, -1);
                Measurement.incrementAssignments(2); // bestCombination.set(index, professor); possibilitiesMatrix.get(index).set(professor, -1);
            } else {
                row.set(professor, -1);
                possibilitiesMatrix.get(index).set(professor, -1);
                Measurement.incrementAssignments(2); // row.set(professor, -1); possibilitiesMatrix.get(index).set(professor, -1);
            }

            difference = calculateDifference(transposedMatrix, bestCombination);
            count++;
            Measurement.incrementAssignments(2); // difference = calculateDifference(transposedMatrix, bestCombination); count++;
        }
        Measurement.incrementComparisons(1); // bestCombination.contains(-1)

        System.out.println("\tFinal stage: " + bestCombination);

        List<Integer> result = new ArrayList<>();
        int fitness = 0;
        Measurement.incrementAssignments(2); // result = new ArrayList<>(); fitness = 0;

        Measurement.incrementAssignments(1); // i = 0
        for (int i = 0; i < bestCombination.size(); i++) {
            Measurement.incrementComparisons(1); // i < bestCombination.size()
            result.add(backupMatrix.get(i).get(bestCombination.get(i)));
            fitness += backupMatrix.get(i).get(bestCombination.get(i));
            Measurement.incrementAssignments(2); // result.add(backupMatrix.get(i).get(bestCombination.get(i))); fitness += backupMatrix.get(i).get(bestCombination.get(i));
        }
        Measurement.incrementComparisons(1); // i < bestCombination.size()

        System.out.println("Doing interchanges...");
        while (result.contains(-1)) {
            Measurement.incrementComparisons(1); // result.contains(-1)
            bestCombination = interchangeCourses(possibilitiesMatrix, bestCombination, result);
            result.clear();
            fitness = 0;
            Measurement.incrementAssignments(3); // bestCombination = interchangeCourses(possibilitiesMatrix, bestCombination, result); result.clear(); fitness = 0;

            Measurement.incrementAssignments(1); // i = 0
            for (int i = 0; i < bestCombination.size(); i++) {
                Measurement.incrementComparisons(1); // i < bestCombination.size()
                result.add(backupMatrix.get(i).get(bestCombination.get(i)));
                fitness += backupMatrix.get(i).get(bestCombination.get(i));
                Measurement.incrementAssignments(2); // result.add(backupMatrix.get(i).get(bestCombination.get(i))); fitness += backupMatrix.get(i).get(bestCombination.get(i));
            }
            Measurement.incrementComparisons(1); // i < bestCombination.size()
        }
        Measurement.incrementComparisons(1); // result.contains(-1)

        System.out.println("\nBest professors combination: " + bestCombination);
        System.out.println("Best grades combination: " + result);
        System.out.println("\n\tOverall calification (fitness): " + fitness);

        return ErrorCodes.NO_ERROR;
    }

    public static int maxIndex(List<Integer> array) {
        int maxIndex = 0;
        int maxValue = array.get(0);
        Measurement.incrementAssignments(2); // maxIndex = 0; maxValue = array.get(0);

        Measurement.incrementAssignments(1); // i = 1
        for (int i = 1; i < array.size(); i++) {
            Measurement.incrementComparisons(1); // i < array.size()

            Measurement.incrementComparisons(1); // array.get(i) > maxValue
            if (array.get(i) > maxValue) {
                maxValue = array.get(i);
                maxIndex = i;
                Measurement.incrementAssignments(2); // maxValue = array.get(i); maxIndex = i;
            }
        }
        Measurement.incrementComparisons(1); // i < array.size()

        return maxIndex;
    }

    public static List<Integer> interchangeCourses(List<List<Integer>> possibilitiesMatrix,
                                                   List<Integer> bestCombination,
                                                   List<Integer> result) {
        Measurement.incrementAssignments(3); // possibilitiesMatrix, bestCombination, result
        if (result.contains(-1)) {
            // Get the index of the first -1 value
            int index = result.indexOf(-1);
            Measurement.incrementAssignments(1); // index = result.indexOf(-1);
            // Search the possibilitiesMatrix for a teacher with less than 4 courses to interchange courses
            List<Integer> column = new ArrayList<>();
            Measurement.incrementAssignments(1); // column = new ArrayList<>();

            for (List<Integer> row : possibilitiesMatrix) {
                Measurement.incrementAssignments(1); // row
                column.add(row.get(index));
                Measurement.incrementAssignments(1); // column.add(row.get(index));
            }

            int bestInterchange = maxIndex(column);
            // professor is the professor with the grade -1
            int professor = bestCombination.get(index);
            int professorToInterchange = bestCombination.get(bestInterchange);
            Measurement.incrementAssignments(3); // bestInterchange = maxIndex(column); professor = bestCombination.get(index); professorToInterchange = bestCombination.get(bestInterchange);
                                                 
            Measurement.incrementComparisons(1); // countOccurrences(bestCombination, professorToInterchange) < 5
            // Check if the professor has less than 4 courses
            if (countOccurrences(bestCombination, professorToInterchange) < 5) {
                // Interchange courses
                bestCombination.set(index, professorToInterchange);
                bestCombination.set(bestInterchange, professor);
                Measurement.incrementAssignments(2); // bestCombination.set(index, professorToInterchange); bestCombination.set(bestInterchange, professor);
                System.out.println("\tInterchange found: " + bestCombination);
            } else {
                possibilitiesMatrix.get(bestInterchange).set(professor, -1);
                bestCombination = interchangeCourses(possibilitiesMatrix, bestCombination, result);
                Measurement.incrementAssignments(2); // possibilitiesMatrix.get(bestInterchange).set(professor, -1); bestCombination = interchangeCourses(possibilitiesMatrix, bestCombination, result);
            }
        }
        return bestCombination;
    }

    public static boolean checkMatrix(List<List<Integer>> tMatrix) {
        Measurement.incrementAssignments(1); // tMatrix

        for (List<Integer> row : tMatrix) {
            Measurement.incrementAssignments(1); // row

            Measurement.incrementComparisons(1); // Collections.frequency(row, -1) == row.size()
            if (Collections.frequency(row, -1) == row.size()) {
                return false;
            }
        }

        for (List<Integer> row : tMatrix) {
            Measurement.incrementAssignments(1); // row

            Measurement.incrementComparisons(1); // Collections.frequency(row, -1) == row.size()
            if (Collections.frequency(row, -1) == row.size()) {
                return false;
            }
        }

        Measurement.incrementComparisons(1); // tMatrix.get(0).size() * 4 < tMatrix.size()
        if (tMatrix.get(0).size() * 4 < tMatrix.size()) {
            return false;
        }

        return true;
    }

    public static List<Integer> calculateDifference(List<List<Integer>> matrix, List<Integer> bestCombination) {
        Measurement.incrementAssignments(2); // matrix, bestCombination

        List<Integer> difference = new ArrayList<>();
        Measurement.incrementAssignments(1); // difference = new ArrayList<>();

        Measurement.incrementAssignments(1); // i = 0
        for (int i = 0; i < matrix.size(); i++) {
            Measurement.incrementComparisons(1); // i < matrix.size()

            Measurement.incrementComparisons(1); // bestCombination.get(i) == -1
            if (bestCombination.get(i) == -1) {
                List<Integer> row = matrix.get(i);
                row = new ArrayList<>(row);
                row.sort(Collections.reverseOrder());
                int maxGrade = row.get(0);
                int secondMaxGrade = row.get(1);
                difference.add((maxGrade - secondMaxGrade) * Math.abs(maxGrade * secondMaxGrade));
                Measurement.incrementAssignments(6);
            } else {
                difference.add(-1);
                Measurement.incrementAssignments(1);
            }
        }
        return difference;
    }

    public static int countOccurrences(List<Integer> list, int element) {
        Measurement.incrementAssignments(2); // list, element
                                             
        int count = 0;
        Measurement.incrementAssignments(1); // count = 0;

        for (int i : list) {
            Measurement.incrementComparisons(1); // i == element
            if (i == element) {
                count++;
                Measurement.incrementAssignments(1); // count++
            }
        }
        return count;
    }
}
