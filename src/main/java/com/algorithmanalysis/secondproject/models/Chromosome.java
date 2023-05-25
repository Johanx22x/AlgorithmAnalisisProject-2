package com.algorithmanalysis.secondproject.models;

import java.io.Serializable;
import java.util.ArrayList;

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
    ArrayList<Allele> alleles = new ArrayList<>();

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
        String chromosome = "";
        for (Allele allele : alleles) {
            chromosome += "(" + allele.toString() + ")";
        }
        return chromosome;
    }

    /**
     * Shuffle the alleles of the chromosome 
     *
     * @param totalOfProfessors Total of professors 
     * @param totalOfCourses Total of courses
     */
    public void shuffleAlleles(int totalOfProfessors, int totalOfCourses) { 
        ArrayList<Allele> newAlleles = new ArrayList<>(); // New alleles

        for (int i = 0; i < totalOfCourses; i++) {
            // Get a random number between 0 and totalOfProfessors
            int random = (int) (Math.random() * totalOfProfessors);

            // Add the allele to the new alleles
            newAlleles.add(alleles.get(random*totalOfCourses + i));
        }

        alleles = newAlleles; // Set the new alleles
    }

    /**
     * Fitness function
     *
     * @return Fitness of the chromosome
     */
    public int fitness() { // TODO: fix
        int fitness = 0; // Fitness of the chromosome

        for (Allele allele : alleles) {
            Professor professor = allele.getProfessor();
            Course course = allele.getCourse();
            int grade = allele.getGrade();

            // Look up the performance measure for the professor-grade combination
            double performance = getPerformanceMeasure(professor, course, grade);

            // Add the performance measure to the fitness score
            fitness += performance;
        }

        return fitness;
    }
}
