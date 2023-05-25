package com.algorithmanalysis.secondproject.algorithms;

import java.util.ArrayList;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;

/**
 * Genetic algorithm 
 *
 * This class is responsible for implementing the genetic algorithm 
 *
 * @author Johan Rodriguez
 * @version 1.0
 */
public class Genetic {
    ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>(); // Final result
    ArrayList<Allele> population = new ArrayList<Allele>(); // Candidates
    int populationSize = 0; // Population size, Isn't the same as the ArrayList size,
                            // because the ArrayList size is the total of alleles

    /**
     * Create chromosomes 
     *
     * This method is responsible for creating the chromosomes with the candidates 
     * and the population size, and then shuffle the alleles
     *
     * @param int totalOfProfessors
     * @param int totalOfCourses
     */
    public void createChromosomes(int totalOfProfessors, int totalOfCourses) {
        // Do a loop until the program generate the desired result
        
        while (chromosomes.size() < this.populationSize) {
            Chromosome chromosome = new Chromosome(population);
            chromosome.shuffleAlleles(totalOfProfessors, totalOfCourses);
            chromosomes.add(chromosome);
        }
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
     * Set candidate population and population size 
     *
     * @param ArrayList<Allele> population 
     * @param int populationSize
     */
    public void setPopulation(ArrayList<Allele> population, int populationSize) {
        this.population = population;
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
