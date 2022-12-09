package org.example;

public class Tuple {
    private int index;
    private int value;

    public Tuple(int index, int value){
        this.index = index;
        this.value = value;
    }

    public int getIndex(){
        return this.index;
    }

    public int getValue(){
        return this.value;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString(){
        return "index: " + this.index + " value: " + this.value;
    }

    public int compareTo(Tuple t){
        if(this.value < t.value){
            return -1;
        }
        else if(this.value > t.value){
            return 1;
        }
        else{
            return 0;
        }
    }
}
