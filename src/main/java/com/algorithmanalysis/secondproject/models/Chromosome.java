package com.algorithmanalysis.secondproject.models;

import java.util.ArrayList;
import java.util.Random;

import com.algorithmanalysis.secondproject.utils.ErrorCodes;

/**
 * ReturnOption enum 
 *
 * This enum is used to decide what to do with the chromosome 
 * during the generation of the random chromosome
 *
 * @author Johan Rodriguez
 * @version 1.0
 */
enum ReturnOption {
    VALID, INVALID, NOT_FOUND, BACKTRACK
}

/**
 * Chromosome class
 *
 * This class represents a chromosome of the genetic algorithm.
 *
 * @author Johan Rodriguez
 * @author Aaron González
 * @version 1.2
 */
public class Chromosome {
    private ArrayList<Allele> alleles = new ArrayList<>();

    /**
     * Constructor
     *
     * @param alleles Alleles of the chromosome
     */
    public Chromosome(ArrayList<Allele> alleles) {
        this.alleles = alleles;
    }

    /**
     * Generate a random chromosome
     *
     * Requirements:
     * - The professor can't have more than 4 courses per chromosome 
     * - Each course can only be assigned once
     * - Each course must be assigned to a professor
     * - A professor can be left without courses
     * - A professor with a grade of -1 means that he can't teach that course
     *
     * @param totalOfProfessors Total of professors 
     * @param totalOfCourses Total of courses
     * @return ErrorCodes enum
     */
    public ErrorCodes generateRandom(int totalOfProfessors, int totalOfCourses) { 
        ArrayList<Allele> newAlleles = new ArrayList<>(); // The new random generated alleles
        int attempts = 100; // Max attempts, if the chromosome can't be generated in 100 attempts, return error
                            
        // Iterate through the total of courses
        for (int i = 0; i < totalOfCourses;) {
            // Get a random professor
            int randomProfessor = new Random().nextInt(totalOfProfessors);

            // Use the total of courses as an offset to get the alleles of the professor
            // Add i to get the next course
            Allele allele = alleles.get(randomProfessor*totalOfCourses + i);

            // Check if the allele is valid according to the course
            ReturnOption returnOptionCourse = isValidCourse(allele);

            // Check if the allele is valid according to the professor
            ReturnOption returnOptionProfessor = isValidProfessor(allele, newAlleles); // Check if the allele is valid

            if (returnOptionCourse == ReturnOption.VALID && returnOptionProfessor == ReturnOption.VALID) { // If the allele is valid
                newAlleles.add(allele); // Add the allele to the new alleles
                i++; // Increase i
            } else if (returnOptionCourse == ReturnOption.NOT_FOUND) { // If the allele is not found
                return ErrorCodes.ERROR_INCAPABLE; // Return error
            } else if (returnOptionProfessor == ReturnOption.BACKTRACK) { // If the allele is not valid in terms of bad generation
                if (attempts == 0) { // If the attempts are 0
                    return ErrorCodes.MAX_ATTEMPTS_EXCEEDED; // Return error
                }
                newAlleles.clear(); // Clear the new alleles
                i = 0; // Set i to 0
                attempts--; // Decrease attempts
            }
        }

        alleles = newAlleles; // Set the new alleles
        return ErrorCodes.NO_ERROR; // Return no error
    }

    /**
     * Check if the allele is valid for the course
     *
     * @param allele Allele
     * @return ReturnOption enum
     */
    public ReturnOption isValidCourse(Allele allele) {
        // Check if the grade is valid, couldn't be -1
        // Requirement: A professor with a grade of -1 means that he can't teach that course
        if (allele.getGrade() != -1) {
            return ReturnOption.VALID; 
        }

        // Check if there is another allele with a valid grade among other professors 
        for (Allele otherAllele : alleles) {
            if (otherAllele.getCourse().getName().equals(allele.getCourse().getName()) && otherAllele.getGrade() != -1) {
                return ReturnOption.INVALID; // There is another allele with a valid grade, so return invalid
            }
        }

        // If there is no allele with a valid grade
        // Requirement: Each course must be assigned to a professor
        return ReturnOption.NOT_FOUND;
    }

    /**
     * Check if the allele is valid for the professor 
     *
     * The professor can't have more than 4 courses per chromosome 
     *
     * @param allele Allele 
     * @param newAlleles New alleles
     * @return ReturnOption enum
     */
    public ReturnOption isValidProfessor(Allele allele, ArrayList<Allele> newAlleles) {
        int totalOfProfessorCourses = 0; // Total of courses of the professor

        // Count the total of courses of the professor
        for (Allele otherAllele : newAlleles) {
            if (otherAllele.getProfessor().getName().equals(allele.getProfessor().getName())) { // If the professor is the same
                totalOfProfessorCourses++;
            }
        }

        // Check if the total of courses is less or equal than 4
        // Requirement: The professor can't have more than 4 courses per chromosome
        if (totalOfProfessorCourses <= 4) {
            return ReturnOption.VALID; // Return valid
        }

        int availableProfessors = 0; // Available professors to take the course
                                     
        // Check if there is another professor that can take the course 
        // The same professor is not counted
        // The course grade of the other professor mustn't be -1
        for (Allele otherAllele : this.alleles) {
            // If the course is the same and the professor is different and the grade is valid
            if (otherAllele.getCourse().getName().equals(allele.getCourse().getName()) && 
                    !otherAllele.getProfessor().getName().equals(allele.getProfessor().getName()) && 
                    otherAllele.getGrade() != -1) {
                availableProfessors++;
            }
        }

        // If there is another professor that can take the course
        if (availableProfessors > 0) {
            return ReturnOption.INVALID; // The professor can't take the course, so return invalid
        }

        // If there is no other professor that can take the course
        // Error: Bad generation, so backtrack and try again
        return ReturnOption.BACKTRACK;
    }

    /**
     * Get the chromosome as a string
     *
     * @return Chromosome as a string
     */
    public String toString() {
        String chromosome = "[";
        for (Allele allele : alleles) {
            chromosome += " (" + allele.toString() + ")";
        }
        chromosome += " ]";
        return chromosome;
    }
}

//     /**
//      * Fitness function
//      *
//      * @return Fitness of the chromosome
//      */
//     public int fitness() { // TODO: fix
//         int fitness = 0; // Fitness of the chromosome

//         for (Allele allele : alleles) {
//             Professor professor = allele.getProfessor();
//             Course course = allele.getCourse();
//             int grade = allele.getGrade();

//             // Look up the performance measure for the professor-grade combination
//             // double performance = getPerformanceMeasure(professor, course, grade);

//             // Add the performance measure to the fitness score
//             // fitness += performance;
//         }

//         return fitness;
//     }
