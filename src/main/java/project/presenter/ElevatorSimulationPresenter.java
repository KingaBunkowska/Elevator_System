package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import project.model.Direction;
import project.model.Elevator;
import project.model.Simulation;

import java.util.LinkedList;
import java.util.List;

import static project.model.Direction.UP;


public class ElevatorSimulationPresenter {
    @FXML
    public Button start;
    @FXML
    private GridPane gridPane;

    @FXML
    private Spinner<Integer> floor;
    @FXML
    private Spinner<Integer> id;

    static final int CELL_WIDTH = 35;
    static final int CELL_HEIGHT = 35;

    private List<RequestButton> buttons = new LinkedList<>();


    private Simulation simulation;

    public Polygon triangleUp(){
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 0.0,
                30.0, 0.0,
                15.0, -30.0
        );
        triangle.setStrokeWidth(0);
        return triangle;
    }

    public Polygon triangleDown(){
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 0.0,
                30.0, 0.0,
                15.0, 30.0
        );
        triangle.setStrokeWidth(0);
        return triangle;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        postponedInitialize();
    }

    public void postponedInitialize(){
        int width = simulation.getNumberOfElevators()-1 + 2;
        int height = simulation.getHighestFloor() - simulation.getLowestFloor();

        for (int i=1; i<height + 2; i++){
            Polygon triangle = triangleUp();
            RequestButton requestButton = new RequestButton(triangle, UP, simulation.getHighestFloor()-i+1, simulation);
            buttons.add(requestButton);

            triangle = triangleDown();
            requestButton = new RequestButton(triangle, Direction.DOWN, simulation.getHighestFloor()-i+1, simulation);
            buttons.add(requestButton);

        }

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                simulation.getLowestFloor(), simulation.getHighestFloor(), simulation.getLowestFloor());

        floor.setValueFactory(valueFactory);

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, simulation.getNumberOfElevators() - 1, 0);

        id.setValueFactory(valueFactory);
    }

    public void drawMap() {
        clearGrid();

        int width = simulation.getNumberOfElevators()-1 + 2;
        int height = simulation.getHighestFloor() - simulation.getLowestFloor();
        int curr_row = simulation.getHighestFloor();
        int curr_col = 0;

        Label label = new Label("y\\x");
        GridPane.setHalignment(label, HPos.CENTER);

        for (int i=0; i< height + 2; i++){
            gridPane.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i=0; i<width+2; i++){
            gridPane.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }

        for (RequestButton button : buttons){
            if (button.getDirection() == UP){
                gridPane.add(button, width, simulation.getHighestFloor() - button.getFloor() + 1);
            }
            else{
                gridPane.add(button, width+1, simulation.getHighestFloor() - button.getFloor() + 1);
            }
        }


        for (int row = 1; row < height + 2; row++){

            label = new Label(curr_row+"");
            curr_row -= 1;
            gridPane.add(label, 0, row);
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (int col = 1; col < width + 2; col++){
            label = new Label(curr_col+"");
            curr_col += 1;
            gridPane.add(label, col, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (Elevator elevator : simulation.getElevators()){
            Rectangle rectangle = new Rectangle(30, 30);
            rectangle.setFill(Color.GRAY);
            gridPane.add(rectangle, elevator.getStatus().elevatorID()+1, simulation.getHighestFloor() - elevator.getFloor() + 1);
            System.out.println(elevator.getStatus());
        }
    }

    private void clearGrid(){
        gridPane.getChildren().retainAll(gridPane.getChildren().get(0));
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
    }

    @FXML
    private void onSimulationStep() {
        clearGrid();
        drawMap();
        simulation.step();
    }

    public void execute(ActionEvent actionEvent) {
        simulation.makeRequestFromInside(id.getValue(), floor.getValue());
    }
}
