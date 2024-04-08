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
        elevatorSystem.operate();
    }

    protected ElevatorSystem getElevatorSystem() {
        return elevatorSystem;
    }

    public void makeRequest(Request request){
        try{
            elevatorSystem.makeRequest(request);
        }
        catch (WrongFloorException e){
            System.out.println(e.getMessage());
            System.out.println("Latest request was ignored");
        }
    }

    public List<Status> getStatus(){
        List<Status> result = new LinkedList<>();
        for (Elevator elevator : elevatorSystem.getElevators()){
            result.add(elevator.getStatus());
        }
        return result;
    }

    public void requestFromInside(int id, int floor){
        try {
            Elevator elevator = elevatorSystem.getElevators().get(id);
            elevator.addStop(floor);
        }
        catch (WrongFloorException e){
            System.out.println(e.getMessage());
            System.out.println("Request was ignored");
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Requested elevator do not exist.\n Request was ignored");
        }
    }

}
