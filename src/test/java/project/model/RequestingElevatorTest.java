package project.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestingElevatorTest {
    @Test
    public void idleRequest(){
        Simulation simulation = new Simulation(2, 0, 4);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 0, ElevatorState.IDLE, Direction.UP));
        simulation.getElevatorSystem().getElevators().getLast().changeStatus(new Status(0, 2, ElevatorState.IDLE, Direction.UP));

        simulation.makeRequest(new Request(0, Direction.UP));
        simulation.makeRequest(new Request(1, Direction.DOWN));

        simulation.step();

        List<Status> expected = Arrays.asList(
            new Status(0, 0, ElevatorState.LOADING, Direction.UP),
            new Status(1, 1, ElevatorState.DOWN, Direction.DOWN)
        );

        assertEquals(expected, simulation.getStatus());

    }

    @Test
    public void sameDirectionRequest(){
        Simulation simulation = new Simulation(1, 0, 4);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 0, ElevatorState.IDLE, Direction.UP));

        simulation.makeRequest(new Request(3, Direction.DOWN));
        simulation.step();

        List<Status> expected = List.of(
                new Status(0, 1, ElevatorState.UP, Direction.UP)
        );

        assertEquals(expected, simulation.getStatus());

        simulation.makeRequest(new Request(1, Direction.UP));

        simulation.step();
        expected = List.of( new Status(0, 1, ElevatorState.LOADING, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        // request from inside an elevator
        simulation.makeRequestFromInside(0, 2);

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.LOADING, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 3, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 3, ElevatorState.LOADING, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.makeRequestFromInside(0, 2);

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.DOWN, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.LOADING, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.IDLE, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.IDLE, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

    }

    @Test
    public void oppositeDirectionRequest(){
        Simulation simulation = new Simulation(1, 0, 4);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 0, ElevatorState.IDLE, Direction.UP));

        simulation.makeRequest(new Request(2, Direction.DOWN));
        simulation.step();

        List<Status> expected = List.of(
                new Status(0, 1, ElevatorState.UP, Direction.UP)
        );

        assertEquals(expected, simulation.getStatus());

        simulation.makeRequest(new Request(1, Direction.DOWN));

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.LOADING, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.makeRequestFromInside(0, 0);

        simulation.step();
        expected = List.of( new Status(0, 1, ElevatorState.DOWN, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 1, ElevatorState.LOADING, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 0, ElevatorState.DOWN, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 0, ElevatorState.LOADING, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 0, ElevatorState.IDLE, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

    }

    @Test
    public void invalidUserRequest(){
        Simulation simulation = new Simulation(1, 0, 4);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 0, ElevatorState.IDLE, Direction.UP));

        simulation.makeRequest(new Request(2, Direction.DOWN));
        simulation.step();

        List<Status> expected = List.of(
                new Status(0, 1, ElevatorState.UP, Direction.UP)
        );

        assertEquals(expected, simulation.getStatus());

        simulation.makeRequest(new Request(1, Direction.DOWN));

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.LOADING, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.makeRequestFromInside(0, 0);

        simulation.step();
        expected = List.of( new Status(0, 1, ElevatorState.DOWN, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 1, ElevatorState.LOADING, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        // invalid request, should be handled after going down
        simulation.makeRequestFromInside(0, 3);

        simulation.step();
        expected = List.of( new Status(0, 0, ElevatorState.DOWN, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 0, ElevatorState.LOADING, Direction.DOWN));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 1, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 2, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 3, ElevatorState.UP, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 3, ElevatorState.LOADING, Direction.UP));
        assertEquals(expected, simulation.getStatus());

        simulation.step();
        expected = List.of( new Status(0, 3, ElevatorState.IDLE, Direction.UP));
        assertEquals(expected, simulation.getStatus());
    }

    @Test
    public void sameFloorRequest(){
        Simulation simulation = new Simulation(2, 0, 4);
        simulation.getElevatorSystem().getElevators().getFirst().changeStatus(new Status(0, 2, ElevatorState.IDLE, Direction.UP));
        simulation.getElevatorSystem().getElevators().getLast().changeStatus(new Status(1, 2, ElevatorState.IDLE, Direction.UP));
        simulation.makeRequest(new Request(2, Direction.DOWN));
        simulation.step();

        List<Status> expected = List.of(
                new Status(0, 2, ElevatorState.LOADING, Direction.UP),
                new Status(1, 2, ElevatorState.IDLE, Direction.UP)
        );

        assertEquals(expected, simulation.getStatus());
    }
}
