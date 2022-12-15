package org.example;

import java.util.ArrayList;
import java.util.Random;

import static org.example.Main.*;

public class Solution {
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

    public int cost() {
        return 0;
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

    public Solution randomMovement(Movement movement) {
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
                ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCosts, ringCosts.size());
                star = getStarSolution(starOrdered, ring, ringCosts.size());
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
                starOrdered = setupStarOrdered(starCosts, ringCosts.size());
                star = getStarSolution(starOrdered, ring, ringCosts.size());
            case SWAP_TWO_RING:
                if (ring.size() < 2) {
                    return this;
                }

                source = randomizer.nextInt(ring.size());
                ring.remove(source);

                destination = randomizer.nextInt(ring.size());
                ring.remove(destination);
                break;
        }
        return null;
    }
}
