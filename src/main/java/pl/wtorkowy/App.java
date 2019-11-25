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
//        //BigInt c = new BigInt("576998766797896796975676985437367537463547374565642542657654587686585767452640");
//        BigInt a = new BigInt("9604");
//        BigInt b = new BigInt("98");
//        System.out.println("div: " + a.divide(b));
       // System.out.println(c.mod(b));
//        Generator generator = new Generator();
//        BigInt[] bigInts = generator.generate(100);
//        for (int i = 0; i < bigInts.length; i++) {
//            System.out.print(bigInts[i] + ", ");
//        }
//        System.out.println();
//        System.out.println(generator.getM());
//        System.out.println(generator.getN());
//        System.out.println(Euklides.isRelativelyPrime(a, b));
//        System.out.println("b: " + b);
//        System.out.println("a: " + a);
//        System.out.println(b.subtract(a));

//        KnapsackBigInt knapsackBigInt = new KnapsackBigInt(30);
//        System.out.println(Arrays.toString(knapsackBigInt.getPublicKey()));
//        System.out.println(Arrays.toString(knapsackBigInt.getPrivateKey()));
//        System.out.println(knapsackBigInt.getN());
//        System.out.println(knapsackBigInt.getM());
//        System.out.println(knapsackBigInt.getReverseN());
//
//        BigInt a = new BigInt("22");
//        BigInt b = new BigInt("7");
//        System.out.println(a.mod(b));

        KnapsackBigInt knapsackBigInt = new KnapsackBigInt(20);
        System.out.println(Arrays.toString(knapsackBigInt.getPrivateKey()));
        BigInt n = new BigInt("3223235333333339");
        BigInt m = new BigInt("120");
        System.out.println(knapsackBigInt.getReverseN());
//        BigInt reverseN = knapsackBigInt.reverseN(n,m);
//        System.out.println(reverseN);
    }
}
