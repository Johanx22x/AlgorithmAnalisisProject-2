package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.App;
import com.algorithmanalysis.secondproject.models.Allele;

/**
 * Backtracking2
 *
 * This class is used to solve the problem using backtracking, but with a different approach.
 *
 * @author Johan Rodriguez
 * @version 2.1
 */
public class Backtracking2 {
    static int totalOfCombinations = 0;

    /**
     * Run the backtracking algorithm 
     *
     * @param alleles The alleles to use 
     * @param totalOfCourses The total of courses 
     * @param totalOfProfessors The total of professors 
     */
    public static void runBacktracking(ArrayList<Allele> alleles, int totalOfCourses, int totalOfProfessors) {
        totalOfCombinations = 0; // Reset the total of combinations

        // Create the matrix
        int[][] matrix = new int[totalOfCourses][totalOfProfessors]; // Create the matrix
        for (Allele allele : alleles) {  // Fill the matrix with the grades
            int professorIndex = allele.getProfessor().getIndex(); // Get the professor index
            int courseIndex = allele.getCourse().getIndex(); // Get the course index
            matrix[courseIndex][professorIndex] = allele.getGrade(); // Set the grade
        }

        // Get the best combination 
        int[] bestCombination = getBestCombination(matrix);

        // Print the best combination 
        System.out.println("Best combination: ");
        for (int i = 0; i < bestCombination.length; i++) {
            System.out.println("\tCourse " + i + " with professor " + bestCombination[i] + " with grade " + matrix[i][bestCombination[i]]);
        }

        System.out.println("\n\tFitness: " + getFitness(bestCombination, matrix));

        if (App.verbose) {
            System.out.println("\tTotal of combinations applying restrictions: " + totalOfCombinations);
        }
    }

    /**
     * Get the fitness of a combination 
     *
     * @param combination The combination to get the fitness from
     * @param matrix The matrix with the grades
     * @return The fitness of the combination
     */
    public static int getFitness(int[] combination, int[][] matrix) {
        int fitness = 0; // The fitness of the combination
        for (int i = 0; i < combination.length; i++) { // Iterate over the combination
            fitness += matrix[i][combination[i]]; // Add the grade to the fitness
        }
        return fitness; // Return the fitness
    }

    /**
     * Generate the combination 
     *
     * @param matrix The matrix with the grades 
     * @param index The index of the combination 
     * @param combination The combination to generate 
     * @param bestCombination The best combination 
     * @param professors The professors 
     */
    public static void generateCombination(int[][] matrix, int index, int[] combination, int[] bestCombination, int[] professors) {
        int totalOfProfessors = matrix[0].length; // Get the total of professors

        if (index == matrix.length) { // If the index is equal to the length of the matrix
            totalOfCombinations++; // Increase the total of combinations

            int fitness = getFitness(combination, matrix); // Get the fitness of the combination
            if (fitness > getFitness(bestCombination, matrix)) { // If the fitness is greater than the fitness of the best combination
                System.arraycopy(combination, 0, bestCombination, 0, combination.length); // Copy the combination to the best combination
            }
            return;
        }

        for (int i = 0; i < totalOfProfessors; i++) { // Iterate over the professors
            if (professors[i] < 4 && matrix[index][i] != -1) { // If the professor has less than 4 courses and the grade is not -1
                combination[index] = i; // Set the professor to the combination
                professors[i]++; // Increase the total of courses of the professor

                generateCombination(matrix, index + 1, combination, bestCombination, professors); // Generate the combination

                professors[i]--; // Decrease the total of courses of the professor
                combination[index] = -1; // Set the professor to -1
            }
        }
    }

    /**
     * Get the best combination 
     *
     * @param matrix The matrix with the grades 
     * @return The best combination 
     */
    public static int[] getBestCombination(int[][] matrix) { // Get the best combination
        int[] bestCombination = new int[matrix.length]; // The best combination
        int[] combination = new int[matrix.length]; // The combination
        int[] professors = new int[matrix[0].length]; // The professors

        generateCombination(matrix, 0, combination, bestCombination, professors); // Generate the combination

        return bestCombination; // Return the best combination
    }
}
