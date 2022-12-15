package org.example;

import java.util.ArrayList;

public class Grasp {
    private final double ALPHA;
    private final int maxIterations;

    private final int[][] starCost;

    private final int[][] ringCost;

    public final int SIZE;

    private final ArrayList<ArrayList<Tuple<Integer>>> starOrdered;

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
            }

            System.out.println("Iteration " + i + " bestCost: " + bestSolution.getCost());
        }
        return bestSolution;

    }


    /**
     * Heuristique permettant d'estimer le coût d'ajout d'un nœud à une solution.
     * → voir explication de la construction du LCR (Liste des candidats restrainte)
     *
     * @param node Le nœud à estimer
     * @return Le coût estimé
     */
    public double estimation(int node, Solution solution) {
        // Implement the estimation function
        return minIncrement(node, solution)[0];
    }

    /**
     * Cherche le plus petit coût pour ajouter le nœud dans le cycle.
     * (on veut trouver l'endroit où (dist_{before} + dist_{after}) est minimum.)
     *
     * @param node Le nœud à ajouter (numéro du noeud pas l'indice dans le tableau)
     * @return La valeur du plus petit coût ajouté
     */
    public int[] minIncrement(int node, Solution solution) {
        // Implement the minIncrement function
        ArrayList<Integer> solutionRing = solution.getRing();
        int ringSize = solutionRing.size();
        // On prend comme valeur initiale le coût de l'ajouter à la fin.
        int rightIndex = 0;
        int leftIndex = ringSize - 1;
        int min = Main.ringCost[node - 1][solutionRing.get(leftIndex) - 1]
                + Main.ringCost[solutionRing.get(rightIndex) - 1][node - 1];

        for (int i = 1; i < ringSize; i++) {
            int cost = Main.ringCost[solutionRing.get(i) - 1][node - 1]
                    + Main.ringCost[node - 1][solutionRing.get((i + 1) % ringSize) - 1];
            if (cost < min) {
                min = cost;
                rightIndex = (i + 1) % ringSize;
                leftIndex = i;

            }
        }

        // Utiliser ça ou alors on ajoute simplement à la fin ?
        return new int[]{min, leftIndex, rightIndex};
    }

    /**
     * Construit la liste des candidats restreints (LCR)
     *
     * @param solution La solution en cours de construction
     * @return La liste des candidats restreints
     */
    public int[] computeRestrictedCandidateList(Solution solution) {
        // TODO : tester si ça marche bien
        // Parcourir chaque noeud du star et calculer son estimation.
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;

        ArrayList<Tuple<Double>> estimationList = new ArrayList<>();
        // Contient les tuples (a, b) où a = le numéro du noeud et b = son estimation.

        for (int i = 1; i < SIZE; i++) { // On commence à 1 parce que le premier noeud est toujours dans le ring (depot)
            int node = i + 1;
            if (!solution.getIsRing()[node - 1]) { // On veut faire le test que sur les noeuds qui ne sont pas déjà dans le ring
                double estimation = estimation(node, solution);
                estimationList.add(new Tuple<>((double) node, estimation));
                if (estimation < min) {
                    min = estimation;
                }
                if (estimation > max) {
                    max = estimation;
                }
            }
        }

        float maxBoundary = (float) (max - ALPHA * (max - min));


        // Retourne le minIncrement max et min
        return estimationList.stream().filter(tuple -> tuple.getB() <= maxBoundary)
                .map(tuple -> tuple.getA().intValue()).mapToInt(i -> i).toArray();

    }

    // TODO : Ajouter à l'estimation du meilleur endroit pour lui dans le cycle.
    private Solution constructSolution() {
        final int MaxIter = 50; // Nombre d'itérations maximum pour la construction de la solution
        Solution bestSolution = new Solution(this);


        for (int i = 0; i < MaxIter; i++) {
            Solution tmpSolution = new Solution(this);
            int[] restrictedCandidateList = computeRestrictedCandidateList(tmpSolution);
            int node = restrictedCandidateList[(int) (Math.random() * restrictedCandidateList.length)];
            // TODO : Créer une solution avec le noeud choisi à la fin (Avant future amélioration)


            // TODO : Vérifier le coût de la solution et si elle est meilleure que la meilleure solution, la remplacer.
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
        Solution bestNeighbour = null;
        for (Solution neighbour : solution.addNodeNeighbourhood()) {
            if (neighbour.getCost() < solution.getCost()) {
                bestNeighbour = neighbour;
            }
        }
        for (Solution neighbour : solution.removeNodeNeighbourhood()) {
            if (neighbour.getCost() < solution.getCost()) {
                bestNeighbour = neighbour;
            }
        }
        for (Solution neighbour : solution.swapNodeNeighbourhood()) {
            if (neighbour.getCost() < solution.getCost()) {
                bestNeighbour = neighbour;
            }
        }
        return bestNeighbour == null ? solution : bestNeighbour;
    }


}
