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
    private int totalOfGenerations = 0; // Total of generations

    // PMX variables
    private int pmxPoint1 = 0; // PMX point 1 
    private int pmxPoint2 = 0; // PMX point 2

    // Result
    private Chromosome result = null; // Result
                            
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

        totalOfGenerations = chromosomes.size() * 2; // Set the total of generations

        return ErrorCodes.NO_ERROR; // Return no error
    }

    /**
     * Get random chromosome 
     *
     * This method is responsible for selecting a random chromosome from the population 
     *
     * @return {@link Chromosome} chromosome
     */
    public Chromosome getRandomChromosome() {
        return chromosomes.get((int) (Math.random() * chromosomes.size())); // Return a random chromosome 
    }

    /**
     * Selection 
     *
     * This method is responsible for selecting the best chromosomes 
     */
    public Chromosome selection() {
        int tournamentSize = 2; // Tournament size
        Chromosome winner = null; // Best chromosome 

        // Iterate through the chromosomes
        for (int i = 0; i < tournamentSize; i++) {
        Chromosome contender = getRandomChromosome(); // Get a random chromosome
            
            if (winner == null || contender.fitness() > winner.fitness()) {
                winner = contender;
            }
        }
        
        return winner;
    }

    /**
     * Mutation 
     *
     * This method is responsible for mutating the chromosomes
     *
     * @param Chromosome chromosome 
     * @return {@link Chromosome} result
     */
    public Chromosome mutation(Chromosome chromosome) {
        // Define maxAttempts
        int maxAttempts = 100;

        // Get a random allele from population
        Allele allele = this.population.get((int) (Math.random() * (this.totalOfCourses * this.totalOfProfessors)));

        while (chromosome.isOptimalAllele(allele) && maxAttempts > 0) { // While the allele is optimal and maxAttempts is greater than 0
            allele = this.population.get((int) (Math.random() * (this.totalOfCourses * this.totalOfProfessors)));
            maxAttempts--;
        }

        if (maxAttempts == 0) { // If maxAttempts is equal to 0
            // Abort the mutation
            System.out.println("Mutation aborted");
            return chromosome; // Return the chromosome
        }

        // Change the result allele on the same course index
        for (int i = 0; i < chromosome.getAlleles().size(); i++) {
            if (chromosome.getAlleles().get(i).getCourse().getIndex() == allele.getCourse().getIndex()) {
                chromosome.getAlleles().set(i, allele);
            }
        }


        return chromosome; // Return the chromosome
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
     * Get population 
     *
     * @return ArrayList<Allele> population
     */
    public ArrayList<Allele> getPopulation() {
        return population;
    }

    /**
     * Get result
     *
     * @return {@link Chromosome} result
     */
    public Chromosome getResult() {
        return result;
    }

    /**
     * Get total of generations 
     *
     * @return int totalOfGenerations
     */
    public int getTotalOfGenerations() {
        return totalOfGenerations;
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
