package model;

import java.util.HashSet;
import java.util.Set;

public class Elevator {
    private static int currentID = 0;
    private int currentFloor = 0;
    private final int id;
    private ElevatorState state;
    private Direction direction = Direction.UP;
    private final Set<Integer> stops;
    private int stepsInLoading = 0;

    public Elevator(){
        this.id = currentID;
        currentID++;
        this.state = ElevatorState.IDLE;
        this.stops = new HashSet<>();
    }

    public Status getStatus(){
        return new Status(id, currentFloor, state, direction);
    }

    protected void changeStatus(Status newStatus){
        state = newStatus.state();
        currentFloor = newStatus.floor();
        this.direction = newStatus.direction();
    }

    protected void operate(){
        this.state = calculateState();
        switch (this.state){
            case UP -> currentFloor += 1;
            case DOWN -> currentFloor -= 1;
            case LOADING -> {
                stops.removeIf(stop -> stop == currentFloor);
                stepsInLoading++;
            }
        }
    }

    private boolean needToChangeDirection(){
        for (Integer stop : stops){
            if ((stop - currentFloor) * direction.value() < 0){
                return true;
            }
        }
        return false;
    }


    private ElevatorState calculateState(){
        if (stops.contains(currentFloor)){
            stops.remove(currentFloor);
            return ElevatorState.LOADING;
        }
        else if (needToChangeDirection()){
            direction = direction.opposite();
            return (direction==Direction.UP)?ElevatorState.UP:ElevatorState.DOWN;
        }
        else if (direction == Direction.UP){
            return ElevatorState.UP;
        }
        else if (direction == Direction.DOWN){
            return ElevatorState.DOWN;
        }
        return ElevatorState.IDLE;
    }

    protected void addStop(int floor){
        stops.add(floor);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloor() {
        return this.currentFloor;
    }
}
