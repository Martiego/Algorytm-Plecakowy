package pl.wtorkowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.wtorkowy.crypt.BigInt;
import pl.wtorkowy.crypt.Euklides;
import pl.wtorkowy.crypt.Generator;

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
        //BigInt c = new BigInt("576998766797896796975676985437367537463547374565642542657654587686585767452640");
        BigInt a = new BigInt("11728247571947062826625627925435");
        BigInt b = new BigInt("69021454330535003428573868087");
        System.out.println("mod: " + b.mod(a));
       // System.out.println(c.mod(b));
        Generator generator = new Generator();
        BigInt[] bigInts = generator.generate(100);
        for (int i = 0; i < bigInts.length; i++) {
            System.out.print(bigInts[i] + ", ");
        }
        System.out.println();
        System.out.println(generator.getM());
        System.out.println(generator.getN());
        System.out.println(Euklides.isRelativelyPrime(a, b));
//        System.out.println("b: " + b);
//        System.out.println("a: " + a);
//        System.out.println(b.subtract(a));
    }
}
