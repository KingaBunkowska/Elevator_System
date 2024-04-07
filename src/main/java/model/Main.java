package model;

public class Main {
    public static void main(String[] args) {
        Elevator elevator = new Elevator();
        System.out.println(elevator.getStatus());
        elevator.changeStatus(new Status(4, 4, ElevatorState.UP));
        System.out.println(elevator.getStatus());
        }
    }