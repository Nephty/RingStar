package org.example;

import java.util.*;

import static org.example.Main.*;

public class Solution implements Comparable<Solution> {



    public enum Movement {
        ADD_TO_RING,
        REMOVE_FROM_RING,
        SWAP_RING_STAR,
        SWAP_TWO_RING,
        RANDOM
    }

    private Random randomizer = new Random();

    private ArrayList<Integer> ring;
    private ArrayList<Integer[]> star;
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

    public Solution randomMovement() {
        // Choisir la position qu'on va déplacer et sa destination
        //  Aussi choisir si on déplace dans le ring ou dans le star
        // Puis calculer le score de la solution
        // Si le score est meilleur, on garde la solution
        // Sinon, on garde la solution avec une probabilité de exp(-deltaE/T)
        if (ring.size() == 0 && star.size() == 0) {
            return this;
        }

        int source, destination;

        // TODO : throw exception instead of returning this so we can change behavior if our movement was unsuccessful
        Movement movement = Movement.values()[randomizer.nextInt(Movement.values().length)];
        System.out.println("movement = " + movement);
        switch (movement) {
            case ADD_TO_RING:
                if (star.size() == 0) {
                    return this;
                }

                source = randomizer.nextInt(star.size()); // random index in star
                // TODO : how to choose a better index ?
                ring.add(randomizer.nextInt(ring.size()), star.get(source)[0]); // add to the ring at a random index
                star.remove(source); // remove from star at index
                break;
            case REMOVE_FROM_RING:
                // NOTE : inserting in the star at any index has the same result
                if (ring.size() == 0) {
                    return this;
                }

                source = randomizer.nextInt(ring.size()); // random index in ring
                ring.remove(source); // remove from ring at index
                // re-compute the star solution that derives from the new ring
                // TODO : OPTIMISATION : can we not just remove the right entry in star ?
                ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCost, ringCost.length);
                star = getStarSolution(starOrdered, ring, ringCost.length);
                break;
            case SWAP_RING_STAR:
                if (ring.size() == 0 || star.size() == 0) {
                    return this;
                }

                source = randomizer.nextInt(ring.size()); // random index in ring
                destination = randomizer.nextInt(star.size()); // random index in star
                // TODO : how to choose a better index ?
                int starValue = star.get(destination)[0];
                ring.remove(source); // remove from ring at index
                ring.add(source, star.get(destination)[0]); // add to the ring at the index of the removed element
                // re-compute the star solution that derives from the new ring
                // TODO : OPTIMISATION : can we not just remove the right entry in star ?
                starOrdered = setupStarOrdered(starCost, ringCost.length);
                star = getStarSolution(starOrdered, ring, ringCost.length);
            case SWAP_TWO_RING:
                if (ring.size() < 2) {
                    return this;
                }

                source = randomizer.nextInt(ring.size());
                do {
                    destination = randomizer.nextInt(ring.size());
                } while (source == destination);

                Collections.swap(ring, source, destination);
                break;
        }
        return this;
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
