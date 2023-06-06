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
    private static long memoryUsage = 0; // Memory usage in bits
    private static long currentTime;
    private static long size = 0; // Size of the input
    private static long assignments = 0; // Number of assignments
    private static long comparisons = 0; // Number of comparisons
    // Total of instructions (assignments + comparisons)

    /**
     * Resets the measurements.
     */
    public static void reset() {
        size = 0;
        assignments = 0;
        comparisons = 0;
        memoryUsage = 0;
    }

    public static void beginTime() {
        currentTime = System.nanoTime();
    }

    public static void endTime() {
        currentTime = System.nanoTime() - currentTime;
    }

    /**
     * Sets the size of the input.
     *
     * @param size Size of the input
     */
    public static void setSize(long size) {
        Measurement.size = size;
    }

    /**
     * Increments the number of assignments.
     *
     * @param n Number of assignments to increment
     */
    public static void incrementAssignments(long n) {
        assignments += n;
    }

    /**
     * Increments the number of comparisons.
     *
     * @param n Number of comparisons to increment
     */
    public static void incrementComparisons(long n) {
        comparisons += n;
    }

    /**
     * Increments the memory usage.
     *
     * @param n Memory usage to increment
     */
    public static void incrementMemoryUsage(long n) {
        memoryUsage += n;
    }

    public static void incrementMemoryUsage(int n) {
        memoryUsage += n;
    }

    /**
     * Getter of the size of the input.
     *
     * @return Size of the input
     */
    public long getSize() {
        return size;
    }

    /**
     * Getter of the number of assignments.
     *
     * @return Number of assignments
     */
    public static long getAssignments() {
        return assignments;
    }

    /**
     * Getter of the number of comparisons.
     *
     * @return Number of comparisons
     */
    public static long getComparisons() {
        return comparisons;
    }

    /**
     * Getter of the total of instructions.
     *
     * @return Total of instructions
     */
    public static long getTotalOfInstructions() {
        return assignments + comparisons;
    }

    /**
     * Getter of the memory usage.
     *
     * @return Memory usage
     */
    public static long getMemoryUsage() {
        return memoryUsage;
    }

    /**
     * Measurement to string.
     *
     * @return {@link String} representation of the measurement
     */
    public static String getMeasurement() {
        return "Measurement:\n\tSize: \t\t\t" + size + " (elements)\n\tAssignments: \t\t" + assignments
                + "\n\tComparisons: \t\t" + comparisons +
                "\n\tTotal instructions: \t" + (long) (assignments + comparisons) + "\n\tMemory usage: \t\t"
                + Math.abs(memoryUsage / 1e+6) + " (MB)" + "\n\tTime: \t\t\t" + currentTime / 1000 + " (ms)";
    }
}
