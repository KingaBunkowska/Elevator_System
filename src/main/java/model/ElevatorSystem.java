package model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.abs;

public class ElevatorSystem {
    private final int lowestFloor;
    private final int highestFloor;
    private final List<Elevator> elevators;
    private final Map<Direction, Set<Integer> > requests;
    public ElevatorSystem(int numberOfElevators, int lowestFloor, int highestFloor){
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        this.elevators = new ArrayList<>(numberOfElevators);
        this.requests = new HashMap<>();
        this.requests.put(Direction.UP, new HashSet<>());
        this.requests.put(Direction.DOWN, new HashSet<>());
        for (int i=0; i<numberOfElevators; i++){
            this.elevators.add(i, new Elevator(i, lowestFloor, highestFloor));
        }
    }

    public void makeRequest(Request request) throws WrongFloorException {
        if (request.floor()<lowestFloor || request.floor()>highestFloor){
            throw new WrongFloorException(request.floor());
        }
        requests.get(request.direction()).add(request.floor());

    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?> keyExtractor1, Function<? super T, ?> keyExtractor2) {
        Set<Object> seenKeys1 = ConcurrentHashMap.newKeySet();
        Set<Object> seenKeys2 = ConcurrentHashMap.newKeySet();

        return t -> {
            Object key1 = keyExtractor1.apply(t);
            Object key2 = keyExtractor2.apply(t);

            boolean unique1 = seenKeys1.add(key1);
            boolean unique2 = seenKeys2.add(key2);

            return unique1 && unique2;
        };
    }

    private void handleStop(Elevator elevator){
        try {
            elevator.addStop(elevator.getFloor());
        } catch (WrongFloorException e) {
            System.out.println("Elevator is in invalid floor. It have to be serviced");
            elevator.changeStatus(new Status(elevator.getStatus().elevatorID(), elevator.getFloor(), ElevatorState.SERVICE, Direction.UP));
            // request is also invalid
            requests.get(elevator.getDirection()).remove(elevator.getFloor());
        }
        requests.get(elevator.getDirection()).remove(elevator.getFloor());
    }

    public void stopElevators(){
        elevators.stream()
                .filter(elevator -> elevator.getStatus().state()!=ElevatorState.SERVICE)
                .filter(elevator -> requests.get(elevator.getDirection()).contains(elevator.getStatus().floor()))
                .filter(distinctByKeys(Elevator::getFloor, Elevator::getDirection))
                .forEach(this :: handleStop);
    }

    private Optional<Integer> findNearestRequestedFloor(int floor){

        Optional<Integer> minFloor = Optional.empty();
        Set<Integer> minSet = null;

        for (Set<Integer> request : requests.values()){
            for (Integer requestedFloor : request){
                if (minFloor.isEmpty() || abs(requestedFloor - floor) < minFloor.get()){
                    minFloor = Optional.of(requestedFloor);
                    minSet = request;
                }
            }
        }

        if (minSet != null){
            minSet.remove(minFloor.get());
        }
        return minFloor;
    }

    private void addRequestIfThereIs(Elevator elevator){
        Optional<Integer> floor = findNearestRequestedFloor(elevator.getFloor());
        floor.ifPresent(newFloor -> {
            try {
                elevator.addStop(newFloor);
            } catch (WrongFloorException e) {
                requests.get(Direction.UP).remove(newFloor);
                requests.get(Direction.DOWN).remove(newFloor);
            }
        });
    }

    public void assignElevators() {
        elevators.stream()
                .filter(elevator -> elevator.getStatus().state() == ElevatorState.IDLE)
                .forEach(this::addRequestIfThereIs);

    }

    public void operate() {
       elevators.stream()
            .filter(elevator -> elevator.getStatus().state()!=ElevatorState.SERVICE)
            .forEach(Elevator::operate);
    }

    public void fix(int elevatorID, int floor){
        try{
            elevators.get(elevatorID).changeStatus(new Status(elevatorID, floor, ElevatorState.LOADING, Direction.UP));
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Invalid elevator id. Fixing request ignored");
        }
    }

    public void checkElevators(){
        for (Elevator elevator : elevators){
            if(elevator.getFloor() > highestFloor || elevator.getFloor() < lowestFloor){
                elevator.changeStatus(new Status(elevator.getStatus().elevatorID(), elevator.getFloor(), ElevatorState.SERVICE, Direction.UP));
            }
        }
    }

}
