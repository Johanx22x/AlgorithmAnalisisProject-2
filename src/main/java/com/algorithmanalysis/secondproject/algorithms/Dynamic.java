package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.models.Allele;

public class Dynamic {
    /**
     * Run the dynamic algorithm 
     *
     * @param alleles           The alleles to be used
     * @param totalOfCourses    The total of totalOfCourses 
     * @param totalOfProfessors The total of totalOfProfessors 
     */
    public static void runDynamicAlgorithm(ArrayList<Allele> alleles, int totalOfCourses, int totalOfProfessors) {
        // Create the matrix
        int[][] matrix = new int[totalOfCourses][totalOfProfessors]; // [course][professor]
        for (Allele allele : alleles) { 
            int professorIndex = allele.getProfessor().getIndex();
            int courseIndex = allele.getCourse().getIndex();
            matrix[courseIndex][professorIndex] = allele.getGrade();
        }

        // Get the best combination
        int[] bestCombination = getBestCombination(matrix);

        // Print the best combination
        System.out.println("\nBest combination: ");
        int fitness = getFitness(bestCombination, matrix);
        for (int i = 0; i < bestCombination.length; i++) {
            int professorIndex = bestCombination[i];
            if (professorIndex != -1) {
                System.out.println("\tCourse " + i + " -> Professor " + professorIndex + " (Grade: " + matrix[i][professorIndex] + ")");
            }
        }

        // Print the fitness
        System.out.println("\n\tFitness: " + fitness);
    }

    /**
     * Get the best combination 
     *
     * @param matrix The matrix to be used 
     * @return The best combination 
     */
    private static int[] getBestCombination(int[][] matrix) {
        // Create the variables
        int[] bestCombination = new int[matrix.length];
        int[] combination = new int[matrix.length];
        int[] professors = new int[matrix[0].length];

        // Generate the combinations
        generateCombinations(matrix, 0, combination, bestCombination, professors);

        // Return the best combination
        return bestCombination;
    }

    /**
     * Generate the combinations 
     *
     * @param matrix        The matrix to be used 
     * @param courseIndex   The course index 
     * @param combination   The combination to be used 
     * @param bestCombination   The best combination to be used 
     * @param professors    The professors to be used 
     */
    private static void generateCombinations(int[][] matrix, int courseIndex, int[] combination, int[] bestCombination, int[] professors) {
        int totalOfProfessors = matrix[0].length; // Set the total of professors
        if (courseIndex == matrix.length) { // If the course index is equal to the matrix length
            int fitness = getFitness(combination, matrix); // Get the fitness
            int bestFitness = getFitness(bestCombination, matrix); // Get the best fitness
            if (fitness > bestFitness) { // If the fitness is greater than the best fitness
                System.arraycopy(combination, 0, bestCombination, 0, combination.length); // Copy the combination to the best combination
            }
            return; // Return
        }

        for (int professorIndex = 0; professorIndex < totalOfProfessors; professorIndex++) { // For each professor
            if (professors[professorIndex] < 4 && matrix[courseIndex][professorIndex] != -1) { // If the professor is not busy and the grade is not -1
                combination[courseIndex] = professorIndex; // Set the combination
                professors[professorIndex]++; // Increment the professor
                                              
                // Recursive call
                generateCombinations(matrix, courseIndex + 1, combination, bestCombination, professors); // Generate the combinations

                // Backtracking
                professors[professorIndex]--; // Decrement the professor
                combination[courseIndex] = -1; // Set the combination
            }
        }
    }

    /**
     * Get the fitness 
     *
     * @param combination   The combination to be used 
     * @param matrix        The matrix to be used 
     * @return The fitness 
     */
    private static int getFitness(int[] combination, int[][] matrix) {
        int fitness = 0; // Set the fitness
        for (int i = 0; i < combination.length; i++) { // For each combination
            int professorIndex = combination[i]; // Get the professor index
            int grade = matrix[i][professorIndex]; // Get the grade
            fitness += grade; // Increment the fitness
        }
        return fitness; // Return the fitness
    }
}

