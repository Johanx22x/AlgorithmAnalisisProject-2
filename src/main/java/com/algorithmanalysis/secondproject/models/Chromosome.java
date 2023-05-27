package com.algorithmanalysis.secondproject.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.algorithmanalysis.secondproject.utils.ErrorCodes;
import com.algorithmanalysis.secondproject.utils.ReturnOption;

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
     * Constructor for List 
     *
     * @param List<Allele> alleles Alleles of the chromosome
     */
    public Chromosome(List<Allele> alleles) {
        this.alleles = new ArrayList<>(alleles);
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
     * @param   totalOfProfessors   Total of professors 
     * @param   totalOfCourses      Total of courses
     * @return  ErrorCodes          enum
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
            ReturnOption returnOptionCourse = isValidCourse(allele, this.alleles);

            // Check if the allele is valid according to the professor
            ReturnOption returnOptionProfessor = isValidProfessor(allele, newAlleles, this.alleles); // Check if the allele is valid

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
     * Check is an allele is valid from mutation in a chromosome
     *
     * @param allele Allele 
     * @return Boolean
     */
    public boolean isOptimalAllele(Allele allele) {
        // Check the grade of the allele 
        if (allele.getGrade() == -1) { // If the grade is -1
            return false; // Return false
        }

        // Check if the professor has more than 4 courses 
        int totalOfProfessorCourses = 0; // Total of courses of the professor 
        for (Allele otherAllele : alleles) {
            if (otherAllele.getProfessor().getName().equals(allele.getProfessor().getName())) { // If the professor is the same
                totalOfProfessorCourses++; // Increase the total of courses
            }
        }

        if (totalOfProfessorCourses > 4) { // If the total of courses is greater than 4
            return false; // Return false
        }

        return false; // Return false
    }

    /**
     * Check if the allele is valid for the course
     *
     * @param allele Allele
     * @return ReturnOption enum
     */
    public static ReturnOption isValidCourse(Allele allele, ArrayList<Allele> alleles) {
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
    public static ReturnOption isValidProfessor(Allele allele, ArrayList<Allele> newAlleles, ArrayList<Allele> alleles) {
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
        for (Allele otherAllele : alleles) {
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
     * Fitness function 
     *
     * The fitness of the chromosome is the sum of the grades of the alleles
     * The grade of the allele is the grade of the professor for the course
     *
     * The best fitness is the maximum grade of the professors
     *
     * @return Fitness of the chromosome
     */
    public int fitness() {
        int fitness = 0; // Fitness of the chromosome
                         
        // Iterate through the alleles
        for (Allele allele : alleles) {
            fitness += allele.getGrade(); // Add the grade to the fitness
        }

        return fitness; // Return the fitness
    }

    /**
     * Get Alleles 
     *
     * @return Alleles
     */
    public ArrayList<Allele> getAlleles() {
        return alleles;
    }

    /**
     * Set Alleles
     *
     * @param alleles Alleles
     */
    public void setAlleles(ArrayList<Allele> alleles) {
        this.alleles = alleles;
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
