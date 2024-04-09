package project.presenter;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import project.model.Direction;
import project.model.ElevatorState;
import project.model.Request;
import project.model.Simulation;

public class RequestButton extends Polygon {
    private final Direction direction;
    private final int floor;
    private Simulation simulation;

    public RequestButton(Polygon polygon, Direction direction, int floor, Simulation simulation){
        super(polygon.getPoints().stream().mapToDouble(Double::doubleValue).toArray());
        setFill(Color.GRAY);
        this.simulation = simulation;
        this.direction = direction;
        this.floor = floor;

        this.setOnMouseClicked(event -> {
            activate();
        });
    }

    public void activate(){
        simulation.makeRequest(new Request(floor, direction));
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloor() {
        return floor;
    }
}
