package model;

public class Elevator {
    private static int currentID = 0;
    private int currentFloor = 0;
    private final int id;
    private ElevatorState state;
    public Elevator(){
        id = currentID;
        currentID+=1;
        state = ElevatorState.IDLE;
    }

    public Status getStatus(){
        return new Status(id, currentFloor, state);
    }

    protected void changeStatus(Status newStatus){
        state = newStatus.state();
        currentFloor = newStatus.floor();
    }
}
