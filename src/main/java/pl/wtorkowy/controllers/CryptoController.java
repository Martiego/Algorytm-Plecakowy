package pl.wtorkowy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.wtorkowy.cast.ToTab;
import pl.wtorkowy.crypt.BigInt;
import pl.wtorkowy.crypt.Knapsack;
import pl.wtorkowy.crypt.KnapsackBigInt;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class CryptoController {
    @FXML
    private Label path;
    @FXML
    private TextArea text;
    @FXML
    private Label cipherText;
    @FXML
    private TextField times;

    @FXML
    private TextField key;
    @FXML
    private TextField n;
    @FXML
    private TextField m;
    @FXML
    private Label publicKey;
    @FXML
    private Label publicKeyFile;
    @FXML
    private Label privateKeyFile;
    @FXML
    private Label nFile;
    @FXML
    private Label mFile;
    @FXML
    private TextField nameFile;

    @FXML
    private ProgressBar progressBar;
    private double progress = 0;
    private double tmpProgress;
    private KnapsackBigInt knapsackBigInt;

    @FXML
    private File file;
    @FXML
    private Stage stage;
    @FXML
    private int[] cipherTextTab;

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Otworz Plik do zaszyfrowania");

        file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            path.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void encrypt() {
        int[] privateKey = ToTab.toIntTab(key.getText());
        char[] text = ToTab.toCharTab(this.text.getText());
        byte[] textByte = ToTab.toByteTab(text);

        int tmp = textByte.length;
        while(tmp%privateKey.length != 0)
            tmp++;

        byte[] tmpText = new byte[tmp];
        System.arraycopy(textByte, 0, tmpText, 0, textByte.length);
        Arrays.fill(tmpText, textByte.length, tmp, (byte) 0);

        Knapsack knapsack = new Knapsack(privateKey, Integer.parseInt(n.getText()), Integer.parseInt(m.getText()));
        publicKey.setText(knapsack.getPublicKey());
        knapsack.encrypt(tmpText);
        cipherTextTab = knapsack.getCipherText();
        if(knapsack.isSuperIncreasing())
            cipherText.setText(knapsack.getCipherTextString());
        else
            cipherText.setText("Private Key isn't superincreasing !");
    }

    @FXML
    public void decrypt() {
        int[] privateKey = ToTab.toIntTab(key.getText());

        Knapsack knapsack = new Knapsack(privateKey, Integer.parseInt(n.getText()), Integer.parseInt(m.getText()));
        knapsack.decrypt(cipherTextTab);
        cipherText.setText(knapsack.getDecipherTextString());
    }

    @FXML
    public void encryptFile() {
        if (file != null) {
            Thread thread = new Thread(new EncryptFile());
            thread.start();
        }
    }

    @FXML
    public void decryptFile() {
        if(file != null) {
            Thread thread = new Thread(new DecryptFile());
            thread.start();
        }
    }

    @FXML
    public void copy() {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(cipherText.getText()), null);
    }

    @FXML
    public void generate() {
        knapsackBigInt = new KnapsackBigInt(Integer.parseInt(times.getText()));
        publicKeyFile.setText(Arrays.toString(knapsackBigInt.getPublicKey()));
        privateKeyFile.setText(Arrays.toString(knapsackBigInt.getPrivateKey()));
        nFile.setText(knapsackBigInt.getN().toString());
        mFile.setText(knapsackBigInt.getM().toString());
    }

    public class EncryptFile implements Runnable {

        @Override
        public void run() {
            try {
                progressBar.setProgress(progress);
                FileInputStream fileInputStream = new FileInputStream(file);

                String name = ToTab.replace(file.getAbsolutePath(), File.separatorChar, nameFile.getText());
                FileWriter newFile = new FileWriter(name);

                long fileLen = file.length();
                long times = fileLen;
                int len = knapsackBigInt.getPublicKey().length/8;
                while(times%len != 0) {
                    times++;
                }
                int rest = (int) (len - (times - fileLen));
                int[] tmpInt = new int[len];
                BigInt[] bigInts;

                tmpProgress = 1.0/(times/len);

                for (int i = 0; i < times/len - 1; i++) {
                    progressBar.setProgress(progress += tmpProgress);
                    for (int j = 0; j < len; j++) {
                        tmpInt[j] = fileInputStream.read();
                    }
                    knapsackBigInt.encrypt(ToTab.toByteTab(tmpInt));
                    bigInts = knapsackBigInt.getCipherText();
                    for (int j = 0; j < bigInts.length; j++) {
                        newFile.write(bigInts[j].toString() + "\n");
                    }
                }

                if(rest != 0) {
                    for (int i = 0; i < rest; i++) {
                        tmpInt[i] = fileInputStream.read();
                    }

                    for (int i = rest; i < len; i++) {
                        tmpInt[i] = '\0';
                    }

                    knapsackBigInt.encrypt(ToTab.toByteTab(tmpInt));
                    bigInts = knapsackBigInt.getCipherText();

                    for (int i = 0; i < bigInts.length; i++) {
                        newFile.write(bigInts[i].toString() + "\n");
                    }
                }

                newFile.close();
                fileInputStream.close();

                progress = 0;
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public class DecryptFile implements Runnable {

        @Override
        public void run() {
            try {
                progressBar.setProgress(progress);

                String name = ToTab.replace(file.getAbsolutePath(), File.separatorChar, nameFile.getText());
                File newFile = new File(name);
                newFile.createNewFile();

                BigInt[] tmp = new BigInt[1];
                byte[] decipherByte;
                int[] decipherInt;

                Scanner in = new Scanner(file);

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                while(in.hasNext()) {
                    progressBar.setProgress(progress += tmpProgress);
                    tmp[0] = new BigInt(in.next());
                    knapsackBigInt.decrypt(tmp);
                    decipherByte = knapsackBigInt.getDecipherText();
                    decipherInt = ToTab.toIntTab(decipherByte);
                    for (int i: decipherInt) {
                        fileOutputStream.write(i);
                    }
                }

                fileOutputStream.close();

                progress = 0;
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
