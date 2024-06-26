package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.model.Simulation;
import project.presenter.ElevatorSimulationPresenter;
import project.presenter.MainPresenter;

import java.io.IOException;

public class SimulationApp extends Application {

    private Stage primaryStage;
    private Simulation simulation;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("main.fxml"));
        BorderPane viewRoot = loader.load();
        MainPresenter presenter = loader.getController();
        presenter.setSimulationApp(this);

        primaryStage.setScene(new Scene(viewRoot, 800, 600));
        primaryStage.setTitle("Elevator Simulation");
        primaryStage.show();

        this.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            simulation.pauseSimulation();
        });


    }

    public void addStage(Simulation simulation){
        try {
            this.simulation = simulation;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane newWindowRoot = loader.load();
            ElevatorSimulationPresenter presenter = loader.getController();
            presenter.setSimulation(simulation);

            primaryStage.setScene(new Scene(newWindowRoot, 800, 600));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error with loading");
        }
    }


}
