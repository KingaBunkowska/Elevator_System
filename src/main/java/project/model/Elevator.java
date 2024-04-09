package project.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Elevator {
    private int currentFloor = 0;
    private final int id;
    private ElevatorState state;
    private Direction direction = Direction.UP;
    private final Set<Integer> stops;
    private final int lowestFloor;
    private final int highestFloor;

    public Elevator(int id, int lowestFloor, int highestFloor) {
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        this.id = id;
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
        System.out.println("operating "+state);
        switch (this.state){
            case UP -> currentFloor += 1;
            case DOWN -> currentFloor -= 1;
            case LOADING -> {
                stops.removeIf(stop -> stop == currentFloor);
            }
        }
    }

    private boolean needToChangeDirection(){
        if (stops.isEmpty()){
            return false;
        }

        for (Integer stop : stops){
            if ((stop - currentFloor) * direction.value() >= 0){
                return false;
            }
        }
        return true;
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
        else if (direction == Direction.UP && !stops.isEmpty()){
            return ElevatorState.UP;
        }
        else if (direction == Direction.DOWN && !stops.isEmpty()){
            return ElevatorState.DOWN;
        }
        return ElevatorState.IDLE;
    }

    protected void addStop(int floor) throws WrongFloorException {
        if (floor>highestFloor || floor<lowestFloor){
            throw new WrongFloorException(floor);
        }
        stops.add(floor);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloor() {
        return this.currentFloor;
    }

}
