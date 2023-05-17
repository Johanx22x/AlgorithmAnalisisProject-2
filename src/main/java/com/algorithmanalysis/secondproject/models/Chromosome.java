package com.algorithmanalysis.secondproject.models;

import java.util.ArrayList;

/**
 * Chromosome class
 *
 * This class represents a chromosome of the genetic algorithm.
 *
 * @author: Johan Rodriguez
 * @version: 1.0
 */
public class Chromosome {
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
}
