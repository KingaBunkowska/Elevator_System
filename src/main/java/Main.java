import model.Direction;
import model.Request;
import model.Simulation;

public class Main {
    public static void main(String[] args) {

        Simulation simulation = new Simulation(4, 0, 4);

        simulation.makeRequest(new Request(0, Direction.UP));
        simulation.makeRequest(new Request(2, Direction.DOWN));
        simulation.step();
        System.out.println(simulation.getStatus());
        simulation.step();
        System.out.println(simulation.getStatus());
    }
}