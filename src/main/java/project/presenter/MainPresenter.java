package project.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import project.SimulationApp;
import project.model.Simulation;


public class MainPresenter {
    @FXML
    private Button start;
    @FXML
    private Spinner<Integer> elevatorNumberSpinner;
    @FXML
    private Spinner<Integer> lowestFloorSpinner;
    @FXML
    private Spinner<Integer> numberOfFloorsSpinner;

    private Simulation simulation;
    private SimulationApp simulationApp;

    public void setSimulationApp(SimulationApp simulationApp) {
        this.simulationApp = simulationApp;
    }

    @FXML
    private void onOptionsAccepted() {

        int numberOfElevators = elevatorNumberSpinner.getValue();
        int lowestFloor = lowestFloorSpinner.getValue();
        int numberOfFloors = numberOfFloorsSpinner.getValue();

        simulation = new Simulation(numberOfElevators, lowestFloor, lowestFloor + numberOfFloors - 1);
        simulationApp.addStage(simulation);
    }

}

