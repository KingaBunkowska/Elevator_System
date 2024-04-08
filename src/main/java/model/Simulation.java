package model;

import java.util.LinkedList;
import java.util.List;

public class Simulation {

    private final ElevatorSystem elevatorSystem;

    public Simulation(int numberOfElevators, int lowestFloor, int highestFloor){
        this.elevatorSystem = new ElevatorSystem(numberOfElevators, lowestFloor, highestFloor);
    }

    public void step(){
        elevatorSystem.assignElevators();
        elevatorSystem.stopElevators();
        elevatorSystem.getElevators().forEach(Elevator::operate);
    }

    protected ElevatorSystem getElevatorSystem() {
        return elevatorSystem;
    }

    public void makeRequest(Request request){
        elevatorSystem.makeRequest(request);
    }

    public List<Status> getStatus(){
        List<Status> result = new LinkedList<>();
        for (Elevator elevator : elevatorSystem.getElevators()){
            result.add(elevator.getStatus());
        }
        return result;
    }

    public void requestFromInside(int id, int floor){
        Elevator elevator = elevatorSystem.getElevators().get(id);
        elevator.addStop(floor);
    }

}
