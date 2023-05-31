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
        int[][] matrix = new int[totalOfCourses][totalOfProfessors];
        for (Allele allele : alleles) { 
            int professorIndex = allele.getProfessor().getIndex();
            int courseIndex = allele.getCourse().getIndex();
            matrix[courseIndex][professorIndex] = allele.getGrade();
        }

        // Get the best combination 
        int[] bestCombination = getBestCombination(matrix);
    }

    /**
     * Get the best combination
     *
     * @param matrix The matrix to be used 
     * @return The best combination 
     */
    public static int[] getBestCombination(int[][] matrix) {
        return null;
    }
}
