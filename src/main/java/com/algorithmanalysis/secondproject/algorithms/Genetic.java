package com.algorithmanalysis.secondproject.algorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.storage.LoadJson.ParsedData;
import com.algorithmanalysis.secondproject.utils.ErrorCodes;
import com.algorithmanalysis.secondproject.utils.Measurement;
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
     * Run the genetic algorithm
     *
     * @param genetic  The genetic object
     * @param fileName The file name
     * @return The error code
     */
    public static ErrorCodes runGenetic(Genetic genetic, String fileName) throws IOException, ParseException {
        ParsedData parsedData = LoadJson.fromFile(fileName); // Load the data from the file

        genetic.setPopulation(parsedData.alleles); // Set the population
        genetic.setPopulationSize(parsedData.population); // Set the population size
        genetic.setTotalOfProfessors(parsedData.alleles.size() / parsedData.courses); // Set the total of professors
        genetic.setTotalOfCourses(parsedData.courses); // Set the total of courses

        // Check if the chromosomes were created successfully
        ErrorCodes error = genetic.createChromosomes();
        if (error != ErrorCodes.NO_ERROR) {
            return error;
        }

        int totalOfGenerations = genetic.getTotalOfGenerations(); // Get the total of generations

        // Start the genetic algorithm
        while (totalOfGenerations > 0) {
            // Iterate over the chromosomes to select, crossover, mutate and evaluate the
            // fitness
            for (int i = 0; i < genetic.getChromosomes().size(); i++) {
                for (int j = 0; j < genetic.getChromosomes().size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    // Selection
                    Chromosome parent1 = genetic.selection(i);
                    Chromosome parent2 = genetic.selection(j);

                    // Crossover
                    Chromosome offspring = genetic.crossover(parent1, parent2);

                    if (offspring == null) {
                        totalOfGenerations--;
                        continue;
                    }

                    // Mutation
                    Chromosome offspringMutated = genetic.mutation(offspring);

                    if (offspringMutated.fitness() > offspring.fitness()) {
                        offspring = offspringMutated;
                    }

                    // Evaluate fitness
                    int offspringFitness = offspring.fitness(); // Or use a custom fitness evaluation method

                    // Update population
                    int leastFitIndex = 0;
                    int leastFitFitness = Integer.MAX_VALUE;

                    for (int k = 0; k < genetic.getChromosomes().size(); k++) {
                        Chromosome chromosome = genetic.getChromosome(k);
                        int chromosomeFitness = chromosome.fitness(); // Or use a custom fitness evaluation method

                        if (chromosomeFitness < leastFitFitness) {
                            leastFitFitness = chromosomeFitness;
                            leastFitIndex = k;
                        }
                    }

                    if (offspringFitness > leastFitFitness) {
                        genetic.getChromosome(leastFitIndex).setAlleles(offspring.getAlleles());
                    }
                }
            }

            totalOfGenerations--; // Decrease the total of generations
        }

        genetic.setResult(genetic.getChromosome(0));
        for (int i = 1; i < genetic.getChromosomes().size(); i++) {
            if (genetic.getChromosome(i).fitness() > genetic.getChromosome(i - 1).fitness()) {
                genetic.setResult(genetic.getChromosome(i));
            }
        }

        return ErrorCodes.NO_ERROR;
    }

                            
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

        // Calculate the total of generations (using a linear function calculated based on the first file population size and the last file population size)
        // To approximate the desired total of generations, we use the following formula: (19700/17) * (populationSize) - (54000/17)
        totalOfGenerations = (int) ((int)(19700/17) * (this.populationSize) - (int)(54000/17)); // Calculate the total of generations

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
     * @return {@link Chromosome} result
     */
    public Chromosome selection(int index) {
        return chromosomes.get(index); // Return the chromosome
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
                alleles.clear(); // Clear the alleles array 
                i = -1; // Reset the index
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
     * Get chromosomes 
     *
     * @return ArrayList<Chromosome> chromosomes
     */
    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
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
     * Get population size 
     *
     * @return int populationSize
     */
    public int getPopulationSize() {
        return populationSize;
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
     * Set result 
     *
     * @param {@link Chromosome} result
     */
    public void setResult(Chromosome result) {
        this.result = result;
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
     * Get fitness
     *
     * @return int fitness
     */
    public int getFitness() {
        return this.result.fitness();
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
