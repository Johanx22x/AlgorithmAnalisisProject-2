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
    private static ArrayList<Chromosome> bestChromosomes = new ArrayList<Chromosome>(); // Best 5 chromosomes (different
                                                                                        // from each other)
    private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>(); // Final result
    private ArrayList<Allele> population = new ArrayList<Allele>(); // Candidates
    private int populationSize = 0; // Population size, Isn't the same as the ArrayList size, because the ArrayList
                                    // size is the total of alleles
    private int totalOfProfessors = 0; // Total of professors
    private int totalOfCourses = 0; // Total of courses
    private int totalOfGenerations = 0; // Total of generations

    // PMX variables
    private int pmxPoint1 = 0; // PMX point 1
    private int pmxPoint2 = 0; // PMX point 2

    // Result
    private Chromosome result = null; // Result

    private String crossoverBuffer = ""; // Crossover buffer
    private String mutationBuffer = ""; // Mutation buffer

    /**
     * Run the genetic algorithm
     *
     * @param genetic  The genetic object
     * @param fileName The file name
     * @return The error code
     */
    // O([nm]²)
    public static ErrorCodes runGenetic(Genetic genetic, String fileName) throws IOException, ParseException { // +2
        bestChromosomes.clear(); // Clear the best chromosomes +1
        ParsedData parsedData = LoadJson.fromFile(fileName); // Load the data from the file +1

        genetic.setPopulation(parsedData.alleles); // Set the population
        genetic.setPopulationSize(parsedData.population); // Set the population size
        genetic.setTotalOfProfessors(parsedData.alleles.size() / parsedData.courses); // Set the total of professors
        genetic.setTotalOfCourses(parsedData.courses); // Set the total of courses +4
        Measurement.incrementAssignments(4); // Increment the assignments by 4 for the previous 4 lines

        Measurement.setSize(parsedData.alleles.size()); // Set the size of the population

        // Check if the chromosomes were created successfully
        ErrorCodes error = genetic.createChromosomes(); // +1
        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
        if (error != ErrorCodes.NO_ERROR) { // +1
            return error;
        }

        int totalOfGenerations = genetic.getTotalOfGenerations(); // Get the total of generations +1 =
                                                                  // totalOfGenerations es n
        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

        // Start the genetic algorithm
        while (totalOfGenerations > 0) { // +n
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration
            // Iterate over the chromosomes to select, crossover, mutate and evaluate the
            // fitness
            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the next line
            for (int i = 0; i < genetic.getChromosomes().size(); i++) { // m
                Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the next line
                for (int j = 0; j < genetic.getChromosomes().size(); j++) { // m
                    Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

                    Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                    if (i == j) { // +1
                        continue;
                    }
                    // Selection
                    Chromosome parent1 = genetic.selection(i); // +1
                    Chromosome parent2 = genetic.selection(j); // +1
                    Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines

                    // Crossover
                    Chromosome offspring = genetic.crossover(parent1, parent2); // +1
                    Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

                    genetic.crossoverBuffer += "Parent 1: " + parent1.toString() + "\n"; // Add the parent 1 to the
                                                                                         // crossover buffer
                    genetic.crossoverBuffer += "Parent 2: " + parent2.toString() + "\n"; // Add the parent 2 to the
                                                                                         // crossover buffer
                                                                                         // +2

                    Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                    if (offspring == null) { // +1
                        totalOfGenerations--; // +1
                        genetic.crossoverBuffer += "Crossover failed\n\n"; // Add the message to the crossover buffer +1
                        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
                        continue; // +1
                    } else {
                        genetic.crossoverBuffer += "Offspring: " + offspring.toString() + "\n\n"; // Add the offspring
                                                                                                  // to the crossover
                                                                                                  // buffer
                                                                                                  // +1
                    }

                    genetic.mutationBuffer += "Original chromosome: " + offspring.toString() + "\n"; // Add the original
                                                                                                     // chromosome to
                                                                                                     // the mutation
                                                                                                     // buffer
                                                                                                     // +1
                    int offspringFitness = offspring.fitness(); // Or use a custom fitness evaluation method +1

                    // Mutation
                    Chromosome offspringMutated = genetic.mutation(offspring); // +1
                    Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

                    genetic.mutationBuffer += "Mutated chromosome: " + offspringMutated.toString() + "\n"; // Add the
                                                                                                           // mutated
                                                                                                           // chromosome
                                                                                                           // to the
                                                                                                           // mutation
                                                                                                           // buffer +1

                    genetic.mutationBuffer += "Original chromosome fitness: " + offspringFitness + "\n"; // Add the
                                                                                                         // original
                                                                                                         // chromosome
                                                                                                         // fitness to
                                                                                                         // the mutation
                                                                                                         // buffer +1
                    genetic.mutationBuffer += "Mutated chromosome fitness: " + offspringMutated.fitness() + "\n"; // Add
                                                                                                                  // the
                                                                                                                  // mutated
                                                                                                                  // chromosome
                                                                                                                  // fitness
                                                                                                                  // to
                                                                                                                  // the
                                                                                                                  // mutation
                                                                                                                  // buffer
                                                                                                                  // +1

                    Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                    if (offspringMutated.fitness() > offspringFitness) { // +1
                        offspring = offspringMutated; // +1
                        genetic.mutationBuffer += "The mutated chromosome is better than the original chromosome\n\n"; // Add
                                                                                                                       // the
                                                                                                                       // message
                                                                                                                       // to
                                                                                                                       // the
                                                                                                                       // mutation
                                                                                                                       // buffer]
                        // +1
                        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
                    } else {
                        genetic.mutationBuffer += "The mutated chromosome is worse than the original chromosome\n\n"; // Add
                                                                                                                      // the
                                                                                                                      // message
                                                                                                                      // to
                                                                                                                      // the
                                                                                                                      // mutation
                                                                                                                      // buffer
                        // +1
                    }

                    // Evaluate fitness
                    offspringFitness = offspring.fitness(); // Or use a custom fitness evaluation method
                    // +1
                    Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

                    // Update population
                    int leastFitIndex = 0; // +1
                    int leastFitFitness = Integer.MAX_VALUE; // +1
                    Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines
                    // 20m

                    for (int k = 0; k < genetic.getChromosomes().size(); k++) { // 2n + 2
                        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

                        Chromosome chromosome = genetic.getChromosome(k); // +1
                        int chromosomeFitness = chromosome.fitness(); // Or use a custom fitness evaluation method +1
                        Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines

                        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                        if (chromosomeFitness < leastFitFitness) { // +1
                            leastFitFitness = chromosomeFitness; // +1
                            leastFitIndex = k; // +1
                            Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2
                                                                 // lines
                        }
                    } // 4(2n+2)
                    Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement

                    Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                                                         //
                    if (offspringFitness > leastFitFitness) { // +1
                        genetic.getChromosome(leastFitIndex).setAlleles(offspring.getAlleles()); // +1
                        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

                        // If there is a chromosome with the same fitness, don't add it
                        boolean add = true; // +1
                        for (Chromosome chromosome : bestChromosomes) { // m
                            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if
                                                                 // statement
                            if (chromosome.fitness() == offspring.fitness()) { // +1
                                add = false; // +1
                                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous
                                                                     // line
                                break; // +1
                            }
                        } // 3m

                        // 20m+11n+10
                        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                        if (!add) { // +1
                            continue;
                        }

                        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                        if (bestChromosomes.size() >= 5) { // +1
                            bestChromosomes.remove(0); // +1
                            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
                        }

                        // Append it ascendingly
                        int index = 0; // +1
                        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

                        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next while
                                                             // statement
                        while (index < bestChromosomes.size()
                                && bestChromosomes.get(index).fitness() < offspringFitness) { // m
                            index++; // +1
                            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
                        } // m

                        bestChromosomes.add(index, offspring); // +1
                        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
                    }

                    // m(21m+11n+15)
                } // -> 21m² + 11nm + 15m
                Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last for iteration
            } // -> 21m² + 11nm + 15m
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last for iteration

            totalOfGenerations--; // Decrease the total of generations +1
            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
        } // n(21m² + 11nm + 15m) -> 21nm² + 11mn² + 15nm + 1
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last while iteration

        genetic.setResult(genetic.getChromosome(0));
        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

        for (int i = 1; i < genetic.getChromosomes().size(); i++) { // 2m + 2
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (genetic.getChromosome(i).fitness() > genetic.getChromosome(i - 1).fitness()) {
                genetic.setResult(genetic.getChromosome(i));
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
            }
        }
        // 21nm² + 11mn² + 15nm + 4m + 5
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last for iteration

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
        Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines

        while (chromosomes.size() < this.populationSize) { // While the chromosomes size is less than the population
                                                           // size
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

            Chromosome chromosome = new Chromosome(this.population); // Create a new chromosome with the candidates
            Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (chromosome.generateRandom(this.totalOfProfessors, this.totalOfCourses) == ErrorCodes.NO_ERROR) { // Shuffle
                                                                                                                 // the
                                                                                                                 // alleles
                chromosomes.add(chromosome); // Add the chromosome to the chromosomes array
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
            } else { // If there is an error
                chromosomes.clear(); // Clear the chromosomes array
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
                return ErrorCodes.ERROR_INCAPABLE; // Return an error
            }
        }
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last failed iteration

        // Calculate the total of generations (using a linear function calculated based
        // on the first file population size and the last file population size)
        // To approximate the desired total of generations, we use the following
        // formula: (19700/17) * (populationSize) - (54000/17)
        // totalOfGenerations = (int) ((int)(19700/17) * (this.populationSize) -
        // (int)(54000/17)); // Calculate the total of generations
        totalOfGenerations = this.populationSize;
        System.out.println("Total of generations: " + totalOfGenerations); // Print the total of generations
        Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line

        return ErrorCodes.NO_ERROR; // Return no error
    }

    /**
     * Get random chromosome
     *
     * This method is responsible for selecting a random chromosome from the
     * population
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
     * Check if a chromosome contains a specific allele
     *
     * This method is responsible for checking if a chromosome contains a specific
     * allele
     *
     * @param Chromosome chromosome
     * @param Allele     Allele
     * @return boolean result
     */
    public boolean containsAllele(Chromosome chromosome, Allele allele) {
        for (Allele a : chromosome.getAlleles()) { // For each allele in the chromosome
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (a.getProfessor().getName().equals(allele.getProfessor().getName()) && // If the professor name is equal
                    a.getCourse().getName().equals(allele.getCourse().getName())) { // If the course name is equal
                return true; // Return true
            }
        }
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last for iteration

        return false; // Return false
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
        Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines

        while (!chromosome.isOptimalAllele(allele) || containsAllele(chromosome, allele)) { // While the allele is
                                                                                            // optimal and maxAttempts
                                                                                            // is greater than 0 and the
                                                                                            // chromosome does not
                                                                                            // contain the allele
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (maxAttempts == 0) { // If maxAttempts is equal to 0
                // Abort the mutation
                return chromosome; // Return the chromosome
            }

            allele = this.population.get((int) (Math.random() * (this.totalOfCourses * this.totalOfProfessors)));
            maxAttempts--;
            Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines
        }
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last failed iteration

        // Change the result allele on the same course index
        for (int i = 0; i < chromosome.getAlleles().size(); i++) {
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (chromosome.getAlleles().get(i).getCourse().getIndex() == allele.getCourse().getIndex()) {
                chromosome.getAlleles().set(i, allele);
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
            }
        }
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last failed iteration

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
        Measurement.incrementAssignments(4); // Increment the assignments by 4 for the previous 4 lines

        // Iterate through the alleles
        for (int i = 0; i < alleles1.size(); i++) {
            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for each iteration

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (i < pmxPoint1 || i > pmxPoint2) { // If the index is less than the PMX point 1 or greater than the PMX
                                                  // point 2
                alleles.add(alleles1.get(i)); // Add the allele of the first chromosome
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
            } else { // If the index is between the PMX point 1 and the PMX point 2
                alleles.add(alleles2.get(i)); // Add the allele of the second chromosome
                Measurement.incrementAssignments(1); // Increment the assignments by 1 for the previous line
            }

            // Check if the new chromosome is valid
            ReturnOption returnOptionCourse = Chromosome.isValidCourse(alleles.get(i), alleles);
            ReturnOption returnOptionProfessor = Chromosome.isValidProfessor(alleles.get(i), alleles, alleles);
            Measurement.incrementAssignments(2); // Increment the assignments by 2 for the previous 2 lines

            Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
            if (returnOptionCourse == ReturnOption.INVALID && returnOptionProfessor == ReturnOption.INVALID
                    || returnOptionCourse == ReturnOption.NOT_FOUND) {
                return null; // Abort the crossover
            } else if (returnOptionProfessor == ReturnOption.BACKTRACK) {
                Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the previous if statement

                Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the next if statement
                if (maxAttempts == 0) {
                    return null; // Abort the crossover
                }
                alleles.clear(); // Clear the alleles array
                i = -1; // Reset the index
                maxAttempts--;
                Measurement.incrementAssignments(3); // Increment the assignments by 3 for the previous 3 lines
            }
        }
        Measurement.incrementComparisons(1); // Increment the comparisons by 1 for the last failed iteration

        return new Chromosome(alleles); // Return the new chromosome
    }

    /**
     * Return the best 5 chromosomes
     *
     * @return ArrayList<Chromosome> bestChromosomes
     */
    public static ArrayList<Chromosome> getBestChromosomes() {
        return bestChromosomes;
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
     * Get crossover buffer
     *
     * @return {@link String} crossoverBuffer
     */
    public String getCrossoverBuffer() {
        String result = crossoverBuffer;
        crossoverBuffer = "";
        return result;
    }

    /**
     * Get mutation buffer
     *
     * @return {@link String} mutationBuffer
     */
    public String getMutationBuffer() {
        String result = mutationBuffer;
        mutationBuffer = "";
        return result;
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
