package org.example;

public class Tuple<T extends Comparable<T>> implements Comparable<Tuple<T>> {
    private T a;
    private T b;

    public Tuple(T a, T b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return this.a;
    }

    public T getB() {
        return this.b;
    }

    public void setA(T a) {
        this.a = a;
    }

    public void setB(T b) {
        this.b = b;
    }

    public String toString() {
        return "a: " + this.a + " b: " + this.b;
    }

    public int compareTo(Tuple<T> t) {
        return this.b.compareTo(t.getB());
    }
}
