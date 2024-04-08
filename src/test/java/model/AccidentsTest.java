package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccidentsTest {

    @Test
    public void wrongFloorExceptionTest(){
        Simulation simulation = new Simulation(1, 0, 4);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 2, ElevatorState.IDLE, Direction.UP));
        try {
            simulation.getElevatorSystem().makeRequest(new Request(4, Direction.DOWN));
        }
        catch (WrongFloorException e){
            fail("Unexpected exception");
        }

        try {
            simulation.getElevatorSystem().makeRequest(new Request(0, Direction.DOWN));
        }
        catch (WrongFloorException e){
            fail("Unexpected exception");
        }

        assertThrows(WrongFloorException.class, () -> simulation.getElevatorSystem().makeRequest( new Request(5, Direction.UP)));

        // check if system still operates despite exception
        simulation.step();

        List<Status> expected = List.of(
                new Status(0, 1, ElevatorState.DOWN, Direction.DOWN)
        );

        assertEquals(expected, simulation.getStatus());
    }

    @Test
    public void wrongFloorExceptionFromInsideTest(){
        Simulation simulation = new Simulation(1, 1, 6);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 2, ElevatorState.LOADING, Direction.DOWN));
        try {
            simulation.getElevatorSystem().getElevators().getFirst().addStop(6);
        }
        catch (WrongFloorException e){
            fail("Unexpected exception");
        }

        try {
            simulation.getElevatorSystem().getElevators().getFirst().addStop(1);
        }
        catch (WrongFloorException e){
            fail("Unexpected exception");
        }

        assertThrows(WrongFloorException.class, () -> simulation.getElevatorSystem().getElevators().getFirst().addStop(0));

        // check if system still operates despite exception
        simulation.step();

        List<Status> expected = List.of(
                new Status(0, 1, ElevatorState.DOWN, Direction.DOWN)
        );

        assertEquals(expected, simulation.getStatus());
    }

    @Test
    public void elevatorOnInvalidFloorTest(){
        Simulation simulation = new Simulation(1, 1, 6);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 0, ElevatorState.LOADING, Direction.DOWN));

        simulation.getElevatorSystem().checkElevators();

        assertEquals(List.of(new Status(0, 0, ElevatorState.SERVICE, Direction.UP)), simulation.getStatus());

        // invalid elevator id
        simulation.getElevatorSystem().fix(1, 1);
        assertEquals(List.of(new Status(0, 0, ElevatorState.SERVICE, Direction.UP)), simulation.getStatus());

        // check if serviced elevator would not get requests
        simulation.makeRequest(new Request(1, Direction.UP));
        simulation.step();
        assertEquals(List.of(new Status(0, 0, ElevatorState.SERVICE, Direction.UP)), simulation.getStatus());


        simulation.getElevatorSystem().fix(0, 1);
        assertEquals(List.of(new Status(0, 1, ElevatorState.LOADING, Direction.UP)), simulation.getStatus());

    }

}
