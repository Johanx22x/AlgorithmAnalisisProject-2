package com.algorithmanalysis.secondproject.utils;

/**
 * This class represents a measurement of a certain algorithm.
 *
 * It contains the size of the input and the time it took to run the algorithm.
 * Counts assignments and comparisons.
 * Also memory usage.
 *
 * @author Johan Rodriguez
 * @version 1.0
 */
public class Measurement {
    private static int size = 0; // Size of the input
    private static int assignments = 0; // Number of assignments
    private static int comparisons = 0; // Number of comparisons
    // Total of instructions (assignments + comparisons)
    private static int memoryUsage = 0; // Memory usage in bits
                                        
    /**
     * Resets the measurements.
     */
    public static void reset() {
        size = 0;
        assignments = 0;
        comparisons = 0;
        memoryUsage = 0;
    }

    /**
     * Sets the size of the input.
     *
     * @param size Size of the input
     */
    public static void setSize(int size) {
        Measurement.size = size;
    }

    /**
     * Increments the number of assignments.
     *
     * @param n Number of assignments to increment
     */
    public static void incrementAssignments(int n) {
        assignments += n;
    }

    /**
     * Increments the number of comparisons.
     *
     * @param n Number of comparisons to increment
     */
    public static void incrementComparisons(int n) {
        comparisons += n;
    }

    /**
     * Increments the memory usage.
     *
     * @param n Memory usage to increment
     */
    public static void incrementMemoryUsage(int n) {
        memoryUsage += n;
    }

    /**
     * Getter of the size of the input.
     *
     * @return Size of the input
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Getter of the number of assignments.
     *
     * @return Number of assignments
     */
    public static int getAssignments() {
        return assignments;
    }

    /**
     * Getter of the number of comparisons.
     *
     * @return Number of comparisons
     */
    public static int getComparisons() {
        return comparisons;
    }

    /**
     * Getter of the total of instructions.
     *
     * @return Total of instructions
     */
    public static int getTotalOfInstructions() {
        return assignments + comparisons;
    }

    /**
     * Getter of the memory usage.
     *
     * @return Memory usage
     */
    public static int getMemoryUsage() {
        return memoryUsage;
    }

    /**
     * Measurement to string.
     *
     * @return {@link String} representation of the measurement
     */
    public static String getMeasurement() {
        return "Measurement:\n\tSize: \t\t\t" + size + " alleles\n\tAssignments: \t\t" + assignments + "\n\tComparisons: \t\t" + comparisons + 
            "\n\tTotal instructions: \t" + (int)(assignments+comparisons) + "\n\tMemory usage: \t\t" + memoryUsage + " bytes";
    }
}
