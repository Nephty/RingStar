package org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Grasp {
    public int i = 0;
    private final double ALPHA;
    private final int maxIterations;

    private final int[][] starCost;

    private final int[][] ringCost;

    public final int SIZE;

    private final ArrayList<ArrayList<Tuple<Integer>>> starOrdered;

    public long movementCount;

    public int[][] getStarCost() {
        return starCost;
    }

    public ArrayList<ArrayList<Tuple<Integer>>> getStarOrdered() {
        return starOrdered;
    }

    public int[][] getRingCost() {
        return ringCost;
    }


    public Grasp(int maxIter, double alpha, int[][] ringCost, int[][] starCost, int size) {
        this.ALPHA = alpha;
        this.maxIterations = maxIter;
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

    public Solution findSolution() {
        // Implement the grasp algorithm for the ring star problem
        Solution bestSolution = new Solution(this);
        int bestCost = Integer.MAX_VALUE;
        for (int i = 0; i < maxIterations; i++) {
            Solution solution = constructSolution(); // Construct a greedy randomised solution
            Solution solution_local = localSearch(solution); // Recherche localement autour de la solution
            int cost = solution_local.getCost();
            if (cost < bestCost) {
                bestCost = cost;
                bestSolution = solution;
                System.out.println("Iteration " + i + " bestCost: " + bestSolution.getCost());
            }


        }
        return bestSolution;

    }

    public Solution findSolution(long maxTime) {
        Instant start = Instant.now();
        Solution bestSolution = new Solution(this);
        int bestCost = Integer.MAX_VALUE;
        ArrayList<Solution> solutions = new ArrayList<>();
        do {
            for (int i = 0; i < 1000; i++) {
                Solution solution = constructSolution(); // Construct a greedy randomised solution
                Solution solution_local = localSearch(solution); // Recherche localement autour de la solution
                int cost = solution_local.getCost();
                if (cost < bestCost) {
                    bestCost = cost;
                    bestSolution = solution;
                    solutions.add(solution);
                    System.out.println("Iteration " + i + " bestCost: " + bestSolution.getCost());
                }


            }
        }while (Instant.now().toEpochMilli() - start.toEpochMilli() < maxTime);

        return solutions.stream().min(Solution::compareTo).get();

    }


    /**
     * Heuristique permettant d'estimer le coût d'ajout d'un nœud à une solution.
     * → voir explication de la construction du LCR (Liste des candidats restrainte)
     *
     * @param node Le nœud à estimer
     * @return Le coût estimé
     */
    public int[] estimation(int node, Solution solution) {
        // Implement the estimation function
        return minIncrement(node, solution);
    }

    /**
     * Cherche le plus petit coût pour ajouter le nœud dans le cycle.
     * (on veut trouver l'endroit où (dist_{before} + dist_{after}) est minimum.)
     *
     * @param node Le nœud à ajouté (numéro du noeud pas l'indice dans le tableau)
     * @return La valeur du plus petit coût ajouté
     */
    public int[] minIncrement(int node, Solution solution) {
        // Implement the minIncrement function
        ArrayList<Integer> solutionRing = solution.getRing();
        int ringSize = solutionRing.size();
        // On prend comme valeur initiale le coût de l'ajouter à la fin.
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
                leftIndex = i;

            }
        }

        // Utiliser ça ou alors on ajoute simplement à la fin ?
        return new int[]{min, rightIndex};
    }

    /**
     * Construit la liste des candidats restreints (LCR)
     *
     * @param solution La solution en cours de construction
     * @return La liste des candidats restreints
     */
    public ArrayList<Tuple<Integer>> computeRestrictedCandidateList(Solution solution) {
        // TODO : tester si ça marche bien
        // Parcourir chaque noeud du star et calculer son estimation.
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;

        ArrayList<Triplet<Integer,Integer,Integer>> estimationList = new ArrayList<>();
        // Contient les triplets (a, b, c) où a = le numéro du noeud et b = son estimation et c l'indice du noeud de droite dans le ring

        for (int i = 1; i < SIZE; i++) { // On commence à 1 parce que le premier noeud est toujours dans le ring (depot)
            int node = i + 1;
            if (!solution.getIsRing()[node - 1]) { // On veut faire le test que sur les noeuds qui ne sont pas déjà dans le ring
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


        // Retourne le minIncrement max et min
        return (ArrayList<Tuple<Integer>>)  estimationList.stream().filter(triplet -> triplet.getValue1() <= maxBoundary)
                .map(triplet -> new Tuple<>(triplet.getValue0(), triplet.getValue2())).collect(Collectors.toList());

    }

    // TODO : Ajouter à l'estimation du meilleur endroit pour lui dans le cycle.
    private Solution constructSolution() {
        final int MaxIter = 50; // Nombre d'itérations maximum pour la construction de la solution
        Solution bestSolution = new Solution(this);


        for (int i = 0; i < MaxIter; i++) {
            Solution tmpSolution = new Solution(this);
            boolean searching = true;
            while (searching){
                ArrayList<Tuple<Integer>> restrictedCandidateList = computeRestrictedCandidateList(tmpSolution);
                if (restrictedCandidateList.isEmpty()) {
                    searching = false;
                } else {
                    // On a donc la liste de tuple (a, b) où a = le numéro du noeud et b l'indice du noeud de droite dans le ring.
                    Tuple<Integer> node = restrictedCandidateList.get((int) (Math.random() * restrictedCandidateList.size())); // TODO Index out of bound here
                    // TODO : Créer une solution avec le noeud choisi à la fin (Avant future amélioration)

                    ArrayList<Integer> newRing = new ArrayList<>(tmpSolution.getRing());
                    if (node.getB() == 0) {
                        newRing.add(node.getA());
                    } else {
                        newRing.add(node.getB(), node.getA());
                    }
                    Solution newSolution = new Solution(newRing, this);

                    if (newSolution.getCost() < tmpSolution.getCost()) {
                        tmpSolution = newSolution;
                    } else if (newRing.size() > 3) { // Pas très sûr de ça mais on va tester
                        // TODO : améliorer ce paramètre
                        searching = false;
                    }
                }
            }

            if (tmpSolution.getCost() < bestSolution.getCost()){
                bestSolution = tmpSolution;
            }
        }


        return bestSolution;
    }

    /**
     * Movement:
     * -add a node to the ring
     * -remove a node from the ring
     * -swap two node in the ring
     *
     * @param solution a solution
     * @return
     */
    private Solution localSearch(Solution solution) {
        for (Solution neighbour : solution.addNodeNeighbourhood()) {
            if (neighbour.getCost() < solution.getCost()) {
                solution = neighbour;
            }

        }

        for (Solution neighbour : solution.removeNodeNeighbourhood()) {
            if (neighbour.getCost() < solution.getCost()) {
                solution = neighbour;
            }
        }
        for (Solution neighbour : solution.swapNodeNeighbourhood()) {
            if (neighbour.getCost() < solution.getCost()) {
                solution = neighbour;
            }
        }
        movementCount += SIZE + solution.ringSize();
        return solution;
    }


}