
package org.example;

public class Triplet<A, B, C> {
    private A value0;
    private B value1;
    private C value2;


    public Triplet(A value0, B value1, C value2) {
        this.value0 = value0;
        this.value1 = value1;
        this.value2 = value2;
    }

    public A getValue0() {
        return value0;
    }

    public void setValue0(A value0) {
        this.value0 = value0;
    }

    public B getValue1() {
        return value1;
    }

    public void setValue1(B value1) {
        this.value1 = value1;
    }

    public C getValue2() {
        return value2;
    }

    public void setValue2(C value2) {
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "value0=" + value0 +
                ", value1=" + value1 +
                ", value2=" + value2 +
                '}';
    }
}