package pl.wtorkowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.wtorkowy.crypt.*;

import java.math.BigInteger;
import java.util.Arrays;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScreen.fxml"));
        StackPane stackPane = loader.load();
        Scene scene = new Scene(stackPane, 800, 500);

        primaryStage.setTitle("Algorytm Plecakowy");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
//        launch();
        BigInt a = new BigInt("100", false);
        BigInt b = new BigInt("1000", false);
        BigInt c = a.subtract(b);
        System.out.println(c);
    }
}
