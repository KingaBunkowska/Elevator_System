package project.model;

import project.presenter.ElevatorSimulationPresenter;

import java.util.LinkedList;
import java.util.List;

public class Simulation {

    private final ElevatorSystem elevatorSystem;
    int numberOfElevator;
    int highestFloor;
    int lowestFloor;
    private boolean run = false;

    private SimulationListener simulationListner;

    public Simulation(int numberOfElevators, int lowestFloor, int highestFloor){
        this.elevatorSystem = new ElevatorSystem(numberOfElevators, lowestFloor, highestFloor);
        this.numberOfElevator = numberOfElevators;
        this.highestFloor = highestFloor;
        this.lowestFloor = lowestFloor;
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

    public int getNumberOfElevators(){
        return numberOfElevator;
    }

    public void makeRequestFromInside(int id, int floor){
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

    public void checkElevators(){
        elevatorSystem.checkElevators();
    }

    public void fixElevator(int elevatorID, int floor){
        elevatorSystem.fix(elevatorID, floor);
    }

    public int getHighestFloor() {
        return highestFloor;
    }

    public int getLowestFloor(){
        return lowestFloor;
    }

    public List<Elevator> getElevators(){
        return elevatorSystem.getElevators();
    }

    public void startSimulation() {
        Thread simulationThread = new Thread(() -> {
            while (run) {
                step();
                makeChange();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        simulationThread.start();
    }

    public void pauseSimulation() {
        run = false;
    }

    public void runSimulation(){
        run = true;
        startSimulation();
    }

    public void addListener(ElevatorSimulationPresenter elevatorSimulationPresenter) {
        simulationListner = elevatorSimulationPresenter;
    }

    public void makeChange(){
        if (simulationListner!=null){
            simulationListner.simulationChanged();
        }
    }
}
