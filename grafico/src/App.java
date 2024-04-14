import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) throws Exception {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Telemetria APS");

        FXMLLoader fl = new FXMLLoader(getClass().getResource("interface.fxml"));
        Parent root = fl.load();
        Scene tela = new Scene(root);
        
        primaryStage.setScene(tela);
        primaryStage.show();

    }
}
