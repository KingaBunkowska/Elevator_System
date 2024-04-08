package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
