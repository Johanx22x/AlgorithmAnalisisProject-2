package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;

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
