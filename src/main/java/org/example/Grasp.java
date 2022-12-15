package org.example;

import org.example.Solution;

public class Grasp {
    private float ALPHA;
    private int maxIterations;

    public Grasp(int maxIter, float alpha) {
        this.ALPHA = alpha;
        this.maxIterations = maxIter;
    }

    public Solution findSolution() {
        // Implement the grasp algorithm for the ring star problem
        Solution bestSolution = new Solution();
        int bestCost = Integer.MAX_VALUE;
        for (int i = 0; i < maxIterations; i++) {
            Solution solution = constructSolution(); // Construct a greedy randomised solution
            Solution solution_local = localSearch(solution); // Recherche localement autour de la solution
            int cost = solution_local.cost();
            if (cost < bestCost) {
                bestCost = cost;
                bestSolution = solution;
            }

            System.out.println("Iteration " + i + " bestCost: " + bestSolution.cost());
        }
        return bestSolution;

    }


    /**
     * Heuristique permettant d'estimer le coût d'ajout d'un noeud à une solution.
     * -> voir explication de la construction du LCR (Liste des candidats restrainte)
     * @param node Le noeud à estimer
     * @return Le coût estimé
     */
    public int estimation(int node){
        // Implement the estimation function
        return minIncrement(node) + meanAllStar(node);
    }

    /**
     * Cherche le plus petit coût pour ajouter le noeud dans le cycle.
     * (on veut trouver l'endroit où (dist_{before} + dist_{after}) est minimum.)
     * @param node Le noeud à ajouter
     * @return La valeur du plus petit coût ajouté
     */
    public int minIncrement(int node) {
        // Implement the minIncrement function

        // Utiliser ça ou alors on ajoute simplement à la fin ?
        return 0;
    }

    /**
     * Retourne la moyenne de tous les coûts des chemins du star vers le noeud.
     * @param node Le noeud à ajouter
     * @return La moyenne des coûts des chemins du star vers le noeud
     */
    public int meanAllStar(int node) {
        // Implement the meanAllStar


        return 0;
    }

    private Solution constructSolution() {
        Solution bestSolution = null;

        return null;
    }

    private Solution localSearch(Solution solution) {
        return null;
    }
}
