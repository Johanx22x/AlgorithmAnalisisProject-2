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
 * @version 1.0
 */
public class Genetic {
    private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>(); // Final result
    private ArrayList<Allele> population = new ArrayList<Allele>(); // Candidates
    private int populationSize = 0; // Population size, Isn't the same as the ArrayList size, because the ArrayList size is the total of alleles
                            
    /**
     * Create chromosomes
     *
     * This method is responsible for creating the chromosomes with the candidates
     * and the population size, and then shuffle the alleles
     *
     * @param int totalOfProfessors
     * @param int totalOfCourses
     */
    public ErrorCodes createChromosomes(int totalOfProfessors, int totalOfCourses) {
        while (chromosomes.size() < this.populationSize) { // While the chromosomes size is less than the population size
            Chromosome chromosome = new Chromosome(population); // Create a new chromosome with the candidates
            if (chromosome.shuffleAlleles(totalOfProfessors, totalOfCourses) == ErrorCodes.NO_ERROR) { // Shuffle the alleles
                chromosomes.add(chromosome); // Add the chromosome to the chromosomes array
            } else { // If there is an error
                chromosomes.clear(); // Clear the chromosomes array
                return ErrorCodes.ERROR_INCAPABLE; // Return an error
            }
        }

        return ErrorCodes.NO_ERROR; // Return no error
    }

    /**
     * Get candidate population
     *
     * @return ArrayList<Allele> population
     */
    public ArrayList<Allele> getPopulation() {
        return population;
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
     * Get chromosomes
     *
     * @return ArrayList<Chromosome> chromosomes
     */
    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    /**
     * Set chromosomes
     *
     * @param ArrayList<Chromosome> chromosomes
     */
    public void setChromosomes(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
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
