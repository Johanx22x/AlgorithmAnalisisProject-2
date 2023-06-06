package com.algorithmanalysis.secondproject.algorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Chromosome;
import com.algorithmanalysis.secondproject.storage.LoadJson;
import com.algorithmanalysis.secondproject.storage.LoadJson.ParsedData;
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
    private static ArrayList<Chromosome> bestChromosomes = new ArrayList<Chromosome>();

    private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();
    private ArrayList<Allele> population = new ArrayList<Allele>();
    private int populationSize = 0;

    private int totalOfProfessors = 0;
    private int totalOfCourses = 0;
    private int totalOfGenerations = 0;

    private int pmxPoint1 = 0;
    private int pmxPoint2 = 0;

    private Chromosome result = null;

    private String crossoverBuffer = "";
    private String mutationBuffer = "";

    /**
     * Run the genetic algorithm
     *
     * @param genetic  The genetic object
     * @param fileName The file name
     * @return The error code
     */

    public static ErrorCodes runGenetic(Genetic genetic, String fileName) throws IOException, ParseException {
        bestChromosomes.clear();
        ParsedData parsedData = LoadJson.fromFile(fileName);

        genetic.setPopulation(parsedData.alleles);
        genetic.setPopulationSize(parsedData.population);
        genetic.setTotalOfProfessors(parsedData.alleles.size() / parsedData.courses);
        genetic.setTotalOfCourses(parsedData.courses);

        ErrorCodes error = genetic.createChromosomes();

        if (error != ErrorCodes.NO_ERROR) {
            return error;
        }

        int totalOfGenerations = genetic.getTotalOfGenerations();

        while (totalOfGenerations > 0) {
            for (int i = 0; i < genetic.getChromosomes().size(); i++) {
                for (int j = 0; j < genetic.getChromosomes().size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    Chromosome parent1 = genetic.selection(i);
                    Chromosome parent2 = genetic.selection(j);
                    Chromosome offspring = genetic.crossover(parent1, parent2);
                    genetic.crossoverBuffer += "Parent 1: " + parent1.toString() + "\n";
                    genetic.crossoverBuffer += "Parent 2: " + parent2.toString() + "\n";
                    if (offspring == null) {
                        totalOfGenerations--;
                        genetic.crossoverBuffer += "Crossover failed\n\n";
                        continue;
                    } else {
                        genetic.crossoverBuffer += "Offspring: " + offspring.toString() + "\n\n";
                    }
                    genetic.mutationBuffer += "Original chromosome: " + offspring.toString() + "\n";
                    int offspringFitness = offspring.fitness();
                    Chromosome offspringMutated = genetic.mutation(offspring);
                    genetic.mutationBuffer += "Mutated chromosome: " + offspringMutated.toString() + "\n";
                    genetic.mutationBuffer += "Original chromosome fitness: " + offspringFitness + "\n";
                    genetic.mutationBuffer += "Mutated chromosome fitness: " + offspringMutated.fitness() + "\n";
                    if (offspringMutated.fitness() > offspringFitness) {
                        offspring = offspringMutated;
                        genetic.mutationBuffer += "The mutated chromosome is better than the original chromosome\n\n";
                    } else {
                        genetic.mutationBuffer += "The mutated chromosome is worse than the original chromosome\n\n";
                    }
                    offspringFitness = offspring.fitness();
                    int leastFitIndex = 0;
                    int leastFitFitness = Integer.MAX_VALUE;
                    for (int k = 0; k < genetic.getChromosomes().size(); k++) {
                        Chromosome chromosome = genetic.getChromosome(k);
                        int chromosomeFitness = chromosome.fitness();
                        if (chromosomeFitness < leastFitFitness) {
                            leastFitFitness = chromosomeFitness;
                            leastFitIndex = k;
                        }
                    }
                    if (offspringFitness > leastFitFitness) {
                        genetic.getChromosome(leastFitIndex).setAlleles(offspring.getAlleles());
                        boolean add = true;
                        for (Chromosome chromosome : bestChromosomes) {
                            if (chromosome.fitness() == offspring.fitness()) {
                                add = false;
                                break;
                            }
                        }
                        if (!add) {
                            continue;
                        }
                        if (bestChromosomes.size() >= 5) {
                            bestChromosomes.remove(0);
                        }
                        int index = 0;
                        while (index < bestChromosomes.size()
                                && bestChromosomes.get(index).fitness() < offspringFitness) {
                            index++;
                        }
                        bestChromosomes.add(index, offspring);
                    }
                }
            }
            totalOfGenerations--;
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

        this.pmxPoint1 = this.totalOfCourses / 3;
        this.pmxPoint2 = this.totalOfCourses * 2 / 3;

        while (chromosomes.size() < this.populationSize) {

            Chromosome chromosome = new Chromosome(this.population);

            if (chromosome.generateRandom(this.totalOfProfessors, this.totalOfCourses) == ErrorCodes.NO_ERROR) {

                chromosomes.add(chromosome);

            } else {
                chromosomes.clear();

                return ErrorCodes.ERROR_INCAPABLE;
            }
        }

        totalOfGenerations = this.populationSize;
        System.out.println("Total of generations: " + totalOfGenerations);

        return ErrorCodes.NO_ERROR;
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
        return chromosomes.get((int) (Math.random() * chromosomes.size()));
    }

    /**
     * Selection
     *
     * @return {@link Chromosome} result
     */
    public Chromosome selection(int index) {
        return chromosomes.get(index);
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
        for (Allele a : chromosome.getAlleles()) {

            if (a.getProfessor().getName().equals(allele.getProfessor().getName()) &&
                    a.getCourse().getName().equals(allele.getCourse().getName())) {
                return true;
            }
        }

        return false;
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

        int maxAttempts = 100;

        Allele allele = this.population.get((int) (Math.random() * (this.totalOfCourses * this.totalOfProfessors)));

        while (!chromosome.isOptimalAllele(allele) || containsAllele(chromosome, allele)) {

            if (maxAttempts == 0) {

                return chromosome;
            }

            allele = this.population.get((int) (Math.random() * (this.totalOfCourses * this.totalOfProfessors)));
            maxAttempts--;

        }

        for (int i = 0; i < chromosome.getAlleles().size(); i++) {

            if (chromosome.getAlleles().get(i).getCourse().getIndex() == allele.getCourse().getIndex()) {
                chromosome.getAlleles().set(i, allele);

            }
        }

        return chromosome;
    }

    /**
     * Crossover
     *
     * This method is responsible for crossing the chromosomes
     *
     * @return {@link Chromosome} result
     */
    public Chromosome crossover(Chromosome chromosome1, Chromosome chromosome2) {
        ArrayList<Allele> alleles = new ArrayList<>();
        ArrayList<Allele> alleles1 = chromosome1.getAlleles();
        ArrayList<Allele> alleles2 = chromosome2.getAlleles();
        int maxAttempts = 100;

        for (int i = 0; i < alleles1.size(); i++) {

            if (i < pmxPoint1 || i > pmxPoint2) {

                alleles.add(alleles1.get(i));

            } else {
                alleles.add(alleles2.get(i));

            }

            ReturnOption returnOptionCourse = Chromosome.isValidCourse(alleles.get(i), alleles);
            ReturnOption returnOptionProfessor = Chromosome.isValidProfessor(alleles.get(i), alleles, alleles);

            if (returnOptionCourse == ReturnOption.INVALID && returnOptionProfessor == ReturnOption.INVALID
                    || returnOptionCourse == ReturnOption.NOT_FOUND) {
                return null;
            } else if (returnOptionProfessor == ReturnOption.BACKTRACK) {

                if (maxAttempts == 0) {
                    return null;
                }
                alleles.clear();
                i = -1;
                maxAttempts--;

            }
        }

        return new Chromosome(alleles);
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
