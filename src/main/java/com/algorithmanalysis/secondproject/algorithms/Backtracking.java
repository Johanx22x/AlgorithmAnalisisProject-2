package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.App;
import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.utils.Measurement;

/**
 * Backtracking2
 *
 * This class is used to solve the problem using backtracking, but with a
 * different approach.
 *
 * @author Johan Rodriguez
 * @version 2.1
 */
public class Backtracking {
    static int totalOfCombinations = 0;

    /**
     * Run the backtracking algorithm
     *
     * @param alleles           The alleles to use
     * @param totalOfCourses    The total of courses
     * @param totalOfProfessors The total of professors
     */
    public static void runBacktracking(ArrayList<Allele> alleles, int totalOfCourses, int totalOfProfessors) { // O(n!)
        Measurement.incrementAssignments(4);
        totalOfCombinations = 0; // Reset the total of combinations +1

        Measurement.setSize(totalOfCourses * totalOfProfessors);

        // Create the matrix
        int[][] matrix = new int[totalOfCourses][totalOfProfessors]; // Create the matrix +1
        for (Allele allele : alleles) { // Fill the matrix with the grades +mn
            Measurement.incrementAssignments(3);
            int professorIndex = allele.getProfessor().getIndex(); // Get the professor index +1mn
            int courseIndex = allele.getCourse().getIndex(); // Get the course index +1mn
            matrix[courseIndex][professorIndex] = allele.getGrade(); // Set the grade +1mn
        } // +3mn

        // Get the best combination
        int[] bestCombination = getBestCombination(matrix); // O(n!)

        // Print the best combination
        //
        // (3mn + 6)n!
        System.out.println("Best combination: ");
        for (int i = 0; i < bestCombination.length; i++) { // n
            Measurement.incrementAssignments(1);
            Measurement.incrementComparisons(1);
            System.out.println("\tCourse " + i + " with professor " + bestCombination[i] + " with grade "
                    + matrix[i][bestCombination[i]]);
        }
        Measurement.incrementComparisons(2); // la falsa y la de abajo

        System.out.println("\n\tFitness: " + getFitness(bestCombination, matrix)); // + O(n) (2n+3)

        if (App.verbose) {
            System.out.println("\tTotal of combinations applying restrictions: " + totalOfCombinations);
        }
    }

    /**
     * Get the fitness of a combination
     *
     * @param combination The combination to get the fitness from
     * @param matrix      The matrix with the grades
     * @return The fitness of the combination
     */
    public static int getFitness(int[] combination, int[][] matrix) { // +2 O(n)
        Measurement.incrementAssignments(2);
        int fitness = 0; // The fitness of the combination
        for (int i = 0; i < combination.length; i++) { // Iterate over the combination 2n + 2
            Measurement.incrementAssignments(1);
            Measurement.incrementComparisons(1);
            fitness += matrix[i][combination[i]]; // Add the grade to the fitness +1
        } // 2n + 3
        Measurement.incrementComparisons(1);
        return fitness; // Return the fitness
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
            int[] professors) { // +5 ====> O(n!)
        Measurement.incrementAssignments(1);
        int totalOfProfessors = matrix[0].length; // Get the total of professors +1

        Measurement.incrementComparisons(1);
        if (index == matrix.length) { // If the index is equal to the length of the matrix +1
            totalOfCombinations++; // Increase the total of combinations +1
            // ^^^^^^^^^^^^^^^^^^^
            Measurement.incrementAssignments(2);
            // \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/
            int fitness = getFitness(combination, matrix); // Get the fitness of the combination +1
            Measurement.incrementComparisons(1);
            if (fitness > getFitness(bestCombination, matrix)) { // If the fitness is greater than the fitness of the +1
                                                                 // best combination
                System.arraycopy(combination, 0, bestCombination, 0, combination.length); // Copy the combination to the
                                                                                          // +1
                                                                                          // best combination
                Measurement.incrementAssignments(1);
            }
            return;
        } // O(n)

        Measurement.incrementAssignments(1);
        for (int i = 0; i < totalOfProfessors; i++) { // Iterate over the professors +5 * (10n + 10) * n!
            Measurement.incrementAssignments(1);
            Measurement.incrementComparisons(2);
            if (professors[i] < 4 && matrix[index][i] != -1) { // +1 If the professor has less than 4 courses and the
                                                               // grade
                                                               // is not -1
                Measurement.incrementAssignments(4);
                combination[index] = i; // Set the professor to the combination +1
                professors[i]++; // Increase the total of courses of the professor +1

                generateCombination(matrix, index + 1, combination, bestCombination, professors); // n! Generate the
                                                                                                  // combination

                professors[i]--; // Decrease the total of courses of the professor +1
                combination[index] = -1; // Set the professor to -1 +1
            }
        }

        // 10n * n! + 21n!
    }

    /**
     * Get the best combination
     *
     * @param matrix The matrix with the grades
     * @return The best combination
     */
    public static int[] getBestCombination(int[][] matrix) { // Get the best combination O(n!)
        Measurement.incrementAssignments(3);
        int[] bestCombination = new int[matrix.length]; // The best combination
        int[] combination = new int[matrix.length]; // The combination
        int[] professors = new int[matrix[0].length]; // The professors +3

        generateCombination(matrix, 0, combination, bestCombination, professors); // Generate the combination ...
        // 10n * n! + 21n!

        return bestCombination; // Return the best combination
    }
}
