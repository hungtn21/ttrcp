package solver.opt;

import solver.TruckContainerSolver;

public interface OptimizationStrategy {
    void optimize(TruckContainerSolver solver, String outputFile);
}
