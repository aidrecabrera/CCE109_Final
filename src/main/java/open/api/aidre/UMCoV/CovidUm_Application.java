package open.api.aidre.UMCoV;

// Importing the necessary libraries to run the program.
import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * It loads the FXML file, sets the scene to the root, makes the window transparent, allows the user to
 * drag the window around the screen, shows the stage, and plays the fade in animation
 */
public class CovidUm_Application extends Application {
    // Used to move the window around.
    private double x, y;

    /**
     * A function that allows the user to drag the window around the screen.
     *
     * @param primaryStage The stage that is being used to display the scene.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loading the FXML file that is being used to display the GUI.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CovidUm.fxml")));
        // Setting the scene to the root and making the window transparent.
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        // Allowing the user to drag the window around the screen.
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
        // Showing the stage and playing the fade in animation.
        primaryStage.show();
        new FadeIn(root).play();
    }
    public static void main(String[] args) {
        // Calling the `start()` function.
        launch();
    }
}