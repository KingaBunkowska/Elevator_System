package model;

public enum ElevatorState {
    UP(1),
    DOWN(-1),
    LOADING(0),
    IDLE(0);

    final private int direction;

    private ElevatorState(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
