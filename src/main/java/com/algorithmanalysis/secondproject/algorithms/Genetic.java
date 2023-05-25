package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;
import com.algorithmanalysis.secondproject.utils.ReturnOption;

/**
 * Genetic algorithm
 *
 * This class is responsible for implementing the genetic algorithm
 *
 * @author Johan Rodriguez
 * @version 1.1
 */
public class Genetic {
    private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>(); // Final result
    private ArrayList<Allele> population = new ArrayList<Allele>(); // Candidates
    private int populationSize = 0; // Population size, Isn't the same as the ArrayList size, because the ArrayList size is the total of alleles
    private int totalOfProfessors = 0; // Total of professors
    private int totalOfCourses = 0; // Total of courses

    // PMX variables
    private int pmxPoint1 = 0; // PMX point 1 
    private int pmxPoint2 = 0; // PMX point 2
                            
    /**
     * Create chromosomes
     *
     * This method is responsible for creating the chromosomes with the candidates
     * and the population size, and then shuffle the alleles
     *
     * @param int totalOfProfessors
     * @param int totalOfCourses
     */
    public ErrorCodes createChromosomes() {
        // Set PMX points
        this.pmxPoint1 = this.totalOfCourses / 3; // PMX point 1
        this.pmxPoint2 = this.totalOfCourses * 2 / 3; // PMX point 2

        while (chromosomes.size() < this.populationSize) { // While the chromosomes size is less than the population size
            Chromosome chromosome = new Chromosome(this.population); // Create a new chromosome with the candidates
            if (chromosome.generateRandom(this.totalOfProfessors, this.totalOfCourses) == ErrorCodes.NO_ERROR) { // Shuffle the alleles
                chromosomes.add(chromosome); // Add the chromosome to the chromosomes array
            } else { // If there is an error
                chromosomes.clear(); // Clear the chromosomes array
                return ErrorCodes.ERROR_INCAPABLE; // Return an error
            }
        }

        return ErrorCodes.NO_ERROR; // Return no error
    }

    /**
     * Crossover
     *
     * This method is responsible for crossing the chromosomes
     *
     * @return {@link Chromosome} result
     */
    public Chromosome crossover(Chromosome chromosome1, Chromosome chromosome2) {
        ArrayList<Allele> alleles = new ArrayList<>(); // Alleles of the new chromosome
        ArrayList<Allele> alleles1 = chromosome1.getAlleles(); // Alleles of the first chromosome
        ArrayList<Allele> alleles2 = chromosome2.getAlleles(); // Alleles of the second chromosome
        int maxAttempts = 100; // Max attempts to create a valid chromosome

        // Iterate through the alleles
        for (int i = 0; i < alleles1.size();i++) {
            if (i < pmxPoint1 || i > pmxPoint2) { // If the index is less than the PMX point 1 or greater than the PMX point 2
                alleles.add(alleles1.get(i)); // Add the allele of the first chromosome
            } else { // If the index is between the PMX point 1 and the PMX point 2
                alleles.add(alleles2.get(i)); // Add the allele of the second chromosome
            }

            // Check if the new chromosome is valid
            ReturnOption returnOptionCourse = Chromosome.isValidCourse(alleles.get(i), alleles);
            ReturnOption returnOptionProfessor = Chromosome.isValidProfessor(alleles.get(i), alleles, alleles);

            if (returnOptionCourse == ReturnOption.INVALID && returnOptionProfessor == ReturnOption.INVALID || returnOptionCourse == ReturnOption.NOT_FOUND) {
                return null; // Abort the crossover
            } else if (returnOptionProfessor == ReturnOption.BACKTRACK) {
                if (maxAttempts == 0) {
                    return null; // Abort the crossover
                }
                i = -1;
                alleles.clear();
                maxAttempts--;
            }
        }

        return new Chromosome(alleles); // Return the new chromosome
    }

//     /**
//      * Show differences between chromosomes 
//      *
//      * This method is responsible for showing the differences between two chromosomes 
//      *
//      * @param {@link Chromosome} chromosome1
//      * @param {@link Chromosome} chromosome2
//      * @return String result
//      */
//     public String showDifferences(Chromosome chromosome1, Chromosome chromosome2) {
//         String result = ""; // Result

//         ArrayList<Allele> alleles1 = chromosome1.getAlleles(); // Alleles of the first chromosome
//         ArrayList<Allele> alleles2 = chromosome2.getAlleles(); // Alleles of the second chromosome

//         // Iterate through the alleles
//         for (int i = 0; i < alleles1.size(); i++) {
//             if (alleles1.get(i).getCourse() != alleles2.get(i).getCourse()) { // If the course is different
//                 result += "Course " + alleles1.get(i).getCourse() + " is different\n"; // Add the course to the result
//             }
//         }

//         return result; // Return the result
//     }

    /**
     * Set candidate population
     *
     * @param ArrayList<Allele> population
     */
    public void setPopulation(ArrayList<Allele> population) {
        this.population = population;
    }

    /**
     * Set population size
     *
     * @param int populationSize
     */
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    /**
     * Set total of professors 
     *
     * @param int totalOfProfessors
     */
    public void setTotalOfProfessors(int totalOfProfessors) {
        this.totalOfProfessors = totalOfProfessors;
    }

    /**
     * Set total of courses
     *
     * @param int totalOfCourses
     */
    public void setTotalOfCourses(int totalOfCourses) {
        this.totalOfCourses = totalOfCourses;
    }

    /**
     * Get chromosome at index 
     *
     * @param int index 
     * @return {@link Chromosome} chromosome
     */
    public Chromosome getChromosome(int index) {
        return chromosomes.get(index);
    }

    /**
     * toString method
     *
     * @return String result
     */
    public String toString() {
        String result = "";
        for (Chromosome chromosome : chromosomes) {
            result += chromosome.toString() + "\n";
        }
        return result;
    }
}
