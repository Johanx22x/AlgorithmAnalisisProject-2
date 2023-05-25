package com.algorithmanalysis.secondproject.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import com.algorithmanalysis.secondproject.utils.ErrorCodes;

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
 * @version 1.0
 */
public class Chromosome implements Serializable {
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
     * Get the alleles of the chromosome
     *
     * @return Alleles of the chromosome
     */
    public ArrayList<Allele> getAlleles() {
        return alleles;
    }

    /**
     * Set the alleles of the chromosome
     *
     * @param alleles Alleles of the chromosome
     */
    public void setAlleles(ArrayList<Allele> alleles) {
        this.alleles = alleles;
    }

    /**
     * Get the chromosome
     *
     * @return Chromosome
     */
    public Chromosome getChromosome() {
        return this;
    }

    /**
     * Set the chromosome
     *
     * @param chromosome Chromosome
     */
    public void setChromosome(Chromosome chromosome) {
        this.alleles = chromosome.getAlleles();
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

    /**
     * Shuffle the alleles of the chromosome 
     *
     * @param totalOfProfessors Total of professors 
     * @param totalOfCourses Total of courses
     * @return ErrorCodes enum
     */
    public ErrorCodes shuffleAlleles(int totalOfProfessors, int totalOfCourses) { 
        ArrayList<Allele> newAlleles = new ArrayList<>(); // New alleles
        int attempts = 100; // Max attempts

        for (int i = 0; i < totalOfCourses;) {
            // Get a random number between 0 and totalOfProfessors
            int random = new Random().nextInt(totalOfProfessors);

            Allele allele = alleles.get(random*totalOfCourses + i); // Get the allele

            ReturnOption returnOptionCourse = isValidAlleleCourse(allele); // Check if the allele is valid

            if (returnOptionCourse == ReturnOption.VALID) { // If the allele is valid
                // Add the allele to the new alleles
                newAlleles.add(allele);
                i++; // Increase i
            } else if (returnOptionCourse == ReturnOption.NOT_FOUND) { // If the allele is not found
                return ErrorCodes.ERROR_INCAPABLE; // Return error
            }

            ReturnOption returnOptionProfessor = isValidAlleleProfessor(allele, newAlleles); // Check if the allele is valid

            if (returnOptionProfessor == ReturnOption.INVALID) { // If the allele is invalid
                newAlleles.remove(allele); // Remove the allele from the new alleles
                i--; // Decrease i
            } else if (returnOptionProfessor == ReturnOption.BACKTRACK) { // The chromosome must be shuffled again
                if (attempts == 0) {
                    return ErrorCodes.ERROR_INCAPABLE; // Return error
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
    public ReturnOption isValidAlleleCourse(Allele allele) {
        // Check if the grade is valid, couldn't be -1
        if (allele.getGrade() != -1) {
            return ReturnOption.VALID;
        }

        // Check if there is another allele with a valid grade among other professors 
        for (Allele otherAllele : alleles) {
            if (otherAllele.getCourse().getName().equals(allele.getCourse().getName()) && otherAllele.getGrade() != -1) {
                return ReturnOption.INVALID;
            }
        }

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
    public ReturnOption isValidAlleleProfessor(Allele allele, ArrayList<Allele> newAlleles) {
        int totalOfCourses = 0; // Total of courses

        // Count the total of courses of the professor
        for (Allele otherAllele : newAlleles) {
            if (otherAllele.getProfessor().getName().equals(allele.getProfessor().getName())) {
                totalOfCourses++;
            }
        }

        // Check if the total of courses is less than 4
        if (totalOfCourses <= 4) {
            return ReturnOption.VALID;
        }


        int professors = 0; // Total of professors that can take the course
        ArrayList<String> professorsNames = new ArrayList<>(); // Professors names
        // Check if there is another professor that can take the course 
        // The same professor is not counted
        // The course grade of the other professor mustn't be -1
        for (Allele otherAllele : alleles) {
            if (otherAllele.getCourse().getName().equals(allele.getCourse().getName()) && !otherAllele.getProfessor().getName().equals(allele.getProfessor().getName()) && otherAllele.getGrade() != -1) {
                professors++;
                professorsNames.add(otherAllele.getProfessor().getName());
            }
        }

        if (professors > 0) {
            return ReturnOption.INVALID;
        }

        return ReturnOption.BACKTRACK;
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
}
