package pl.wtorkowy;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.wtorkowy.crypt.Knapsack;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
        Knapsack knapsack = new Knapsack();
        knapsack.generatePublicKey();
        int[] tab = knapsack.getPublicKey();
        for (int i: tab) {
            System.out.println(i);
        }
        knapsack.encrypt();
        tab = knapsack.getCipherText();
        for (int i: tab) {
            System.out.println(i);
        }
        knapsack.decrypt();
        byte[] tabByte = knapsack.getDecipherText();
        for(byte b: tabByte) {
            System.out.print(b);
        }
    }
}
