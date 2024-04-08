package model;

public enum Direction {
    UP (1),
    DOWN (-1);

    private final int value;
    private Direction(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }

    public Direction opposite() {
        return Direction.values()[(this.ordinal()+1)%2];
    }
}
