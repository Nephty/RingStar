package org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Grasp {
    private final double ALPHA;

    private final int[][] starCost;

    private final int[][] ringCost;

    public final int SIZE;

    private final ArrayList<ArrayList<Tuple<Integer>>> starOrdered;

    public int[][] getStarCost() {
        return starCost;
    }

    /**
     * Get method for the starOrdered attribute.
     * <p>
     * The star ordered is a matrix of tuple (cost, j) which are the costs of going from i to j in the star.
     * It is ordered to have the cheapest cost at the beginning of the list so if we want to find the nearest node to i,
     * we select the list at index i and take the first element that lies in the ring.
     *
     * @return The starOrdered.
     */
    public ArrayList<ArrayList<Tuple<Integer>>> getStarOrdered() {
        return starOrdered;
    }

    public int[][] getRingCost() {
        return ringCost;
    }

    /**
     * Main constructor of the grasp algorithm for the ring star problem.
     *
     * @param alpha    The alpha parameter.
     * @param ringCost The cost matrix for the ring.
     * @param starCost The cost matrix for the star.
     * @param size     The size of the problem. (number of nodes)
     */
    public Grasp(double alpha, int[][] ringCost, int[][] starCost, int size) {
        this.ALPHA = alpha;
        this.starCost = starCost;
        this.ringCost = ringCost;
        this.SIZE = size;
        this.starOrdered = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Tuple<Integer>> tmp = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                tmp.add(new Tuple<>(j, starCost[i][j]));
            }
            tmp.sort(Tuple::compareTo);
            this.starOrdered.add(tmp);
        }
    }

    /**
     * Find a solution to the ring star problem using the GRASP meta-heuristic.
     *
     * @param maxTime Time to run the algorithm (in ms)
     * @return The best solution found.
     * @see Solution
     */
    public Solution findSolution(long maxTime) {
        Instant start = Instant.now();
        Solution bestSolution = new Solution(this);
        ArrayList<Solution> solutions = new ArrayList<>();
        int j = 0;
        do {
            for (int i = 0; i < 100; i++) {
                // Construct a greedy randomised solution and do a local search to find a local minimum
                Solution solution = localSearch(constructSolution());
                if (solution.getCost() < bestSolution.getCost()) {
                    bestSolution = solution;
                    solutions.add(solution);
                    System.out.println("Iteration " + (i + j * 100) + " bestCost: " + bestSolution.getCost());
                }
            }
            j++;
        } while (Instant.now().toEpochMilli() - start.toEpochMilli() < maxTime);
        System.out.println("maxIter = " + j * 100);
        System.out.println("Best solution = " + solutions.stream().min(Solution::compareTo).get().getCost());
        return solutions.stream().min(Solution::compareTo).get();
    }


    /**
     * Heuristic used to estimate the cost of adding a node to the ring.
     * <p>
     * We return the cost of adding the node at the best place of the ring.
     *
     * @param node The node to add.
     * @return A table of size 2 containing the cost ([0]) and the index of the best place to add the node ([1]}.
     */
    public int[] estimation(int node, Solution solution) {
        return minIncrement(node, solution);
    }


    /**
     * Seek for the best place to add a node to the ring.
     * The best place is where (dist_{left} + dist_{right}) is minimal.
     * <p>
     * Distance left (resp. right) is the distance between the node and the node on its "left" (resp. right) in the ring.
     *
     * @param node The node to add. (number of the node, not the index in the ring)
     * @return A table of size 2 containing the cost ([0]) and the index of the best place to add the node ([1]}.
     */
    public int[] minIncrement(int node, Solution solution) {
        ArrayList<Integer> solutionRing = solution.getRing();
        int ringSize = solutionRing.size();
        // The initial value is the cost of adding the node at the end of the ring.
        int rightIndex = 0;
        int leftIndex = ringSize - 1;
        int min = this.ringCost[node - 1][solutionRing.get(leftIndex) - 1]
                + this.ringCost[solutionRing.get(rightIndex) - 1][node - 1];

        for (int i = 1; i < ringSize; i++) {
            int cost = this.ringCost[solutionRing.get(i) - 1][node - 1]
                    + this.ringCost[node - 1][solutionRing.get((i + 1) % ringSize) - 1];
            if (cost < min) {
                min = cost;
                rightIndex = (i + 1) % ringSize;
            }
        }

        return new int[]{min, rightIndex};
    }

    /**
     * Build the Restricted Candidate List (RCL)
     *
     * @param solution The initial solution.
     * @return The RCL which is a list of tuple (a, b) where a is the node and b is the index of the best place to add it.
     */
    public ArrayList<Tuple<Integer>> computeRestrictedCandidateList(Solution solution) {
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;

        ArrayList<Triplet<Integer, Integer, Integer>> estimationList = new ArrayList<>();
        // Contains the triplets (a, b, c) where a is the node, b is its estimation and c is the index of the best place to add it.

        for (int i = 1; i < SIZE; i++) { // We start at 1 because 0 is the depot
            int node = i + 1;
            if (solution.isStarNode(node)) { //If the node is already in the ring, we don't add it to the RCL
                int[] estimation = estimation(node, solution);
                estimationList.add(new Triplet<>(node, estimation[0], estimation[1]));
                if (estimation[0] < min) {
                    min = estimation[0];
                }
                if (estimation[0] > max) {
                    max = estimation[0];
                }
            }
        }

        float maxBoundary = (float) (min + ALPHA * (max - min));


        // We keep only the nodes with an estimation lower than the maxBoundary.
        return (ArrayList<Tuple<Integer>>) estimationList.stream()
                .filter(triplet -> triplet.getValue1() <= maxBoundary)
                .map(triplet -> new Tuple<>(triplet.getValue0(), triplet.getValue2()))
                .collect(Collectors.toList());
    }

    /**
     * Construct a solution using the GRASP meta-heuristic.
     *
     * @return The constructed solution.
     */
    private Solution constructSolution() {


        Solution tmpSolution = new Solution(this);
        boolean searching = true;
        while (searching) {
            ArrayList<Tuple<Integer>> restrictedCandidateList = computeRestrictedCandidateList(tmpSolution);
            if (restrictedCandidateList.isEmpty()) {
                searching = false;
            } else {
                // We have the list of tuple (a, b) where a is the node and b the index of the best place to add it.
                // and we choose randomly a node in the RCL.
                Tuple<Integer> node = restrictedCandidateList.get((int) (Math.random() * restrictedCandidateList.size()));

                ArrayList<Integer> newRing = new ArrayList<>(tmpSolution.getRing());
                if (node.getB() == 0) {
                    newRing.add(node.getA());
                } else {
                    newRing.add(node.getB(), node.getA());
                }
                Solution newSolution = new Solution(newRing, this);


                if (newSolution.getCost() < tmpSolution.getCost()) {
                    tmpSolution = newSolution;
                } else {
                    while (restrictedCandidateList.size() > 1 && newSolution.getCost() > tmpSolution.getCost()) {
                        restrictedCandidateList.remove(node);
                        node = restrictedCandidateList.get((int) (Math.random() * restrictedCandidateList.size()));
                        newRing = new ArrayList<>(tmpSolution.getRing());
                        if (node.getB() == 0) {
                            newRing.add(node.getA());
                        } else {
                            newRing.add(node.getB(), node.getA());
                        }
                        newSolution = new Solution(newRing, this);

                    }
                    if (newSolution.getCost() < tmpSolution.getCost()) {
                        tmpSolution = newSolution;
                    } else {
                        searching = false;
                    }
                }
            }
        }

        return tmpSolution;
    }

    /**
     * Movement:
     * -add a node to the ring
     * -remove a node from the ring
     * -swap two node in the ring
     * - swap a node in the star and in the ring
     *
     * @param solution a solution
     * @return Best neighbour if none is better, then the entered solution
     */
    private Solution localSearch(Solution solution) {
        Solution bestNeighbour = solution;
        do {
            // in the case were we found a better neighbour in the previous loop cycle we change
            // the current solution to that neighbour and repeat
            if (bestNeighbour.getCost() < solution.getCost()) {
                solution = bestNeighbour;
            }

            for (Solution neighbour : solution.addNodeNeighbourhood()) {
                if (neighbour.getCost() < bestNeighbour.getCost()) {
                    solution = neighbour;
                }

            }
            for (Solution neighbour : solution.removeNodeNeighbourhood()) {
                if (neighbour != null) {    // if the solution is null, it means that the node is not removable
                    if (neighbour.getCost() < bestNeighbour.getCost()) {
                        solution = neighbour;
                    }
                }
            }
            for (Solution neighbour : solution.swapRingNodeNeighbourhood()) {
                if (neighbour.getCost() < bestNeighbour.getCost()) {
                    solution = neighbour;
                }
            }
            for (Solution neighbour : solution.swapStarRingNodeNeighbourhood()) {
                if (neighbour != null) {
                    if (neighbour.getCost() < bestNeighbour.getCost()) {
                        solution = neighbour;
                    }
                }
            }

        } while (bestNeighbour.getCost() < solution.getCost());

        return solution;
    }
}
