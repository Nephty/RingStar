package org.example;


import java.util.*;

import static org.example.Maint_methode.*;

public class Solution implements Comparable<Solution> {
    private int[][]ringCost;
    private int[][]starCost;

    public ArrayList<Solution> getNeighboors(){ //Todo faire la fonction avec celle d'Augustin
        ArrayList<Solution> neighboors = new ArrayList<Solution>();
        return neighboors;
    }


    public enum Movement {
        ADD_TO_RING,
        REMOVE_FROM_RING,
        SWAP_RING_STAR,
        SWAP_TWO_RING
    }

    private Random randomizer = new Random();

    public ArrayList<Integer> ring;
    public ArrayList<Integer[]> star;
    public Solution(ArrayList<Integer> ring, ArrayList<Integer[]> star) {
        this.ring = ring;
        this.star = star;
    }

    /**
     * Calcule le coût d'une solution en faisan la somme des coûts des chemins entre les noeuds du ring
     * + somme des chemins entre les noeuds du star.
     *
     * @return Le coùt de la solution
     */


    public int cost() {
        int cost = 0;

        for (int i = 0; i < ring.size(); i++) {
            cost += ringCost[ring.get(i) - 1][ring.get((i + 1) % ring.size()) - 1];
        }

        ArrayList<Integer[]> starSolution = getStarSolution(starOrdered, ring, size);
        for (Integer[] i : starSolution) {
            cost += starCost[i[0] - 1][i[1] - 1];
            // System.out.println("Star cost ("+i[0]+","+i[1]+"): " + starCost[i[0]-1][i[1]-1]);
        }

        return cost;
    }

    public ArrayList<Integer> getRing() {
        return ring;
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
    }

    public ArrayList<Integer[]> getStar() {
        return star;
    }

    public void setStar(ArrayList<Integer[]> star) {
        this.star = star;
    }

    public Solution randomMovement() throws Exception {
        // copy ring and star because otherwise it would modify the solution upon which we call the method
        ArrayList<Integer> localRing = new ArrayList<>(ring);
        ArrayList<Integer[]> localStar = new ArrayList<>(star);

        if (localRing.size() == 0 && localStar.size() == 0) {
            return new Solution(localRing, localStar);
        }

        int source, destination;

        // TODO : throw exception instead of returning this so we can change behavior if our movement was unsuccessful
        Movement movement = Movement.values()[randomizer.nextInt(Movement.values().length)];
        switch (movement) {
            case ADD_TO_RING:
                if (localStar.size() == 0) {
                    throw new Exception("This movement is impossible : no nodes in the star");
                }

                source = randomizer.nextInt(localStar.size()); // random index in star
                // TODO : how to choose a better index ?
                localRing.add(randomizer.nextInt(localRing.size()), localStar.get(source)[0]); // add to the ring at a random index
                localStar.remove(source); // remove from star at index
                break;

            case REMOVE_FROM_RING:
                // NOTE : inserting in the star at any index has the same result
                if (localRing.size() <= 1) { // at least 1 node in the ring
                    throw new Exception("This movement is impossible : at least 1 node in the ring");
                }

                source = randomizer.nextInt(localRing.size()); // random index in ring
                localRing.remove(source); // remove from ring at index
                // re-compute the star solution that derives from the new ring
                // TODO : OPTIMISATION : can we not just remove the right entry in star ?
                ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCost, ringCost.length);
                localStar = getStarSolution(starOrdered, localRing, ringCost.length);
                break;

            case SWAP_RING_STAR:
                if (localRing.size() == 1 || localStar.size() == 0) {
                    throw new Exception("This movement is impossible : either the ring will be empty or there are no node in stars");
                }

                source = randomizer.nextInt(localRing.size()); // random index in ring
                destination = randomizer.nextInt(localStar.size()); // random index in star
                localRing.remove(source); // remove from ring at index
                // TODO : how to choose a better index ?
                localRing.add(source, localStar.get(destination)[0]); // add to the ring at the index of the removed element
                // re-compute the star solution that derives from the new ring
                // TODO : OPTIMISATION : can we not just remove the right entry in star ?
                starOrdered = setupStarOrdered(starCost, ringCost.length);
                localStar = getStarSolution(starOrdered, localRing, ringCost.length);

            case SWAP_TWO_RING:
                if (localRing.size() < 2) {
                    throw new Exception("This movement is impossible : there must be at least two node in the ring to swap");
                }

                source = randomizer.nextInt(localRing.size());
                do {
                    destination = randomizer.nextInt(localRing.size());
                } while (source == destination);

                Collections.swap(localRing, source, destination);
                break;
        }
        return new Solution(localRing, localStar);
    }

    @Override
    public int compareTo(Solution o) {
        return Integer.compare(this.cost(), o.cost());
    }

    @Override
    public String toString() {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append("Ring: ");
        for (Integer i : ring) {
            bobTheBuilder.append(i);
            bobTheBuilder.append(" ");
        }
        bobTheBuilder.append("\nStar: ");
        for (Integer[] i : star) {
            bobTheBuilder.append(i[0]);
            bobTheBuilder.append(" ");
        }
        bobTheBuilder.append("\nCost: ");
        bobTheBuilder.append(cost());
        return bobTheBuilder.toString();
    }
}





