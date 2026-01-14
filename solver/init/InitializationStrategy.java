package solver.init;

import solver.TruckContainerSolver;

/**
 * Strategy interface for initialization algorithms.
 * 
 * This interface allows easy swapping of different initialization algorithms
 * for the Truck Container VRP solver without modifying the solver code.
 * 
 * Example implementations:
 * - First Possible Insertion with Unscheduled Set (FPIUS)
 * - Greedy initialization
 * - Random initialization
 * - Nearest neighbor heuristic
 */
public interface InitializationStrategy {
    
    /**
     * Initializes a solution for the given solver.
     * 
     * This method should:
     * 1. Create initial routes by inserting requests
     * 2. Populate solver.rejectPickupPoints and solver.rejectDeliveryPoints
     *    with requests that couldn't be inserted
     * 3. Ensure all constraints are satisfied
     * 
     * @param solver The TruckContainerSolver instance to initialize
     */
    void initialize(TruckContainerSolver solver);
}
