package pl.wtorkowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.wtorkowy.crypt.BigInt;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScreen.fxml"));
        StackPane stackPane = loader.load();
        Scene scene = new Scene(stackPane, 800, 500);

        primaryStage.setTitle("DES");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
        BigInt c = new BigInt("576998766797896796975676985437367537463547374565642542657654587686585767452640");
        BigInt a = new BigInt("5197420");
        BigInt b = new BigInt("4324232342342342323753457343754376537656587385685638563568836538653856835638686568357383857588750");
        System.out.println(c.mod(b));
    }
}
