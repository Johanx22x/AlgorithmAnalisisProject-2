package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.App;
import com.algorithmanalysis.secondproject.models.Allele;

/**
 * Backtracking2
 *
 * This class is used to solve the problem using backtracking, but with a
 * different approach.
 *
 * @author Johan Rodriguez
 * @version 2.1
 */
public class Backtracking2 {
    static int totalOfCombinations = 0;

    /**
     * Run the backtracking algorithm
     *
     * @param alleles           The alleles to use
     * @param totalOfCourses    The total of courses
     * @param totalOfProfessors The total of professors
     */
    public static void runBacktracking(ArrayList<Allele> alleles, int totalOfCourses, int totalOfProfessors) {
        totalOfCombinations = 0;
        int[][] matrix = new int[totalOfCourses][totalOfProfessors];
        for (Allele allele : alleles) {
            int professorIndex = allele.getProfessor().getIndex();
            int courseIndex = allele.getCourse().getIndex();
            matrix[courseIndex][professorIndex] = allele.getGrade();
        }

        int[] bestCombination = getBestCombination(matrix);
    }

    /**
     * Get the fitness of a combination
     *
     * @param combination The combination to get the fitness from
     * @param matrix      The matrix with the grades
     * @return The fitness of the combination
     */
    public static int getFitness(int[] combination, int[][] matrix) {
        int fitness = 0;
        for (int i = 0; i < combination.length; i++) {

            fitness += matrix[i][combination[i]];
        }
        return fitness;
    }

    /**
     * Generate the combination
     *
     * @param matrix          The matrix with the grades
     * @param index           The index of the combination
     * @param combination     The combination to generate
     * @param bestCombination The best combination
     * @param professors      The professors
     */
    public static void generateCombination(int[][] matrix, int index, int[] combination, int[] bestCombination,
            int[] professors) {
        int totalOfProfessors = matrix[0].length;
        if (index == matrix.length) {
            totalOfCombinations++;
            int fitness = getFitness(combination, matrix);
            if (fitness > getFitness(bestCombination, matrix)) {
                System.arraycopy(combination, 0, bestCombination, 0, combination.length);
            }
            return;
        }
        for (int i = 0; i < totalOfProfessors; i++) {
            if (professors[i] < 4 && matrix[index][i] != -1) {
                combination[index] = i;
                professors[i]++;
                generateCombination(matrix, index + 1, combination, bestCombination, professors);
                professors[i]--;
                combination[index] = -1;
            }
        }
    }

    /**
     * Get the best combination
     *
     * @param matrix The matrix with the grades
     * @return The best combination
     */
    public static int[] getBestCombination(int[][] matrix) {
        int[] bestCombination = new int[matrix.length];
        int[] combination = new int[matrix.length];
        int[] professors = new int[matrix[0].length];
        generateCombination(matrix, 0, combination, bestCombination, professors);
        return bestCombination;
    }
}
