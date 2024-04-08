package model;

public record Status(int elevatorID, int floor, ElevatorState state, Direction direction) {
    Status changeStateTo(ElevatorState newState){
        return new Status(elevatorID, floor, newState, direction);
    }
}
