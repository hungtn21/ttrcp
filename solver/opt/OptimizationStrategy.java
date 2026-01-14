package solver.opt;

import solver.TruckContainerSolver;

/**
 * Strategy interface for optimization algorithms.
 * 
 * This interface allows easy swapping of different meta-heuristic algorithms
 * for optimizing the Truck Container VRP solution.
 * 
 * Example implementations:
 * - Adaptive Large Neighborhood Search (ALNS)
 * - Genetic Algorithm (GA)
 * - Tabu Search
 * - Simulated Annealing
 * - Variable Neighborhood Search (VNS)
 */
public interface OptimizationStrategy {
    
    /**
     * Optimizes the solution for the given solver.
     * 
     * This method should:
     * 1. Take the current solution in solver.XR
     * 2. Apply meta-heuristic algorithm to improve it
     * 3. Update solver.XR with the best solution found
     * 4. Update solver.rejectPickupPoints and solver.rejectDeliveryPoints
     * 5. Write progress/results to the output file
     * 
     * @param solver The TruckContainerSolver instance to optimize
     * @param outputFile Path to the output file for logging results
     */
    void optimize(TruckContainerSolver solver, String outputFile);
}
