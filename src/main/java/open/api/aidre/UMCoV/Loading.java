package open.api.aidre.UMCoV;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Loading {
    private Stage stage;
    private Scene scene;
    private Object root;
    public void initialize() throws InterruptedException, IOException {
    }
    void gimmick() throws InterruptedException, IOException {
        Thread.sleep(1000);
        transition();
    }


    void transition() throws InterruptedException, IOException {
        Thread.sleep(1000);
        FXMLLoader fxmlLoader = new FXMLLoader(CovidUm_Application.class.getResource("CovidUm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
