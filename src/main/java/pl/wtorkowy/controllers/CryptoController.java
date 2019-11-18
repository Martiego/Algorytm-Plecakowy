package pl.wtorkowy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.wtorkowy.cast.ToTab;
import pl.wtorkowy.crypt.Knapsack;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CryptoController {
    @FXML
    private Label path;
    @FXML
    private TextArea text;
    @FXML
    private Label cipherText;

    @FXML
    private TextField key;
    @FXML
    private TextField keyFile;
    @FXML
    private TextField n;
    @FXML
    private TextField nFile;
    @FXML
    private TextField m;
    @FXML
    private TextField mFile;
    @FXML
    private Label publicKey;
    @FXML
    private Label publicKeyFile;
    @FXML
    private TextField nameFile;

    @FXML
    private ProgressBar progressBar;
    private double progress = 0;
    private double tmpProgress;

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
        cipherText.setText(knapsack.getCipherTextString());
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

    public class EncryptFile implements Runnable {

        @Override
        public void run() {
            try {
                progressBar.setProgress(progress);
                FileInputStream fileInputStream = new FileInputStream(file);

                String name = ToTab.replace(file.getAbsolutePath(), File.separatorChar, nameFile.getText());
                File newFile = new File(name);
                newFile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int[] privateKey = ToTab.toIntTab(keyFile.getText());
                Knapsack knapsack = new Knapsack(privateKey, Integer.parseInt(nFile.getText()), Integer.parseInt(mFile.getText()));
                int[] tmp = new int[(int) file.length()];
                byte[] tmpByte;

                for (int i = 0; i < file.length(); i++) {
                    tmp[i] = fileInputStream.read();
                }

                tmpByte = ToTab.toByteTab(tmp);

                int times = (int) ((file.length() * 8)/privateKey.length);
                int rest = (int) (file.length() * 8 - times * privateKey.length);
                tmpProgress = 1.0/times;
                // TODO
                //  No wiec tak, dostajemy za kazdym razem inta, tylko problem jest taki, ze
                //  Dlugosc inta zalezy od tego jak dlugi jest klucz, wiec gdy jest to 6 to otrzymujemy inta na 6 bitach
                //  Ale strumien zawsze majac na mysli int ma na mysli 8 bitow ;c
                //  Tak wiec pliki zawsze sa nadmiarowe
                //  Wiec kod do wywalenia

                for (int i = 0; i < times; i++) {
                    knapsack.encrypt(ToTab.cutTab(tmpByte, i * privateKey.length, privateKey.length));

                    fileOutputStream.write(knapsack.getCipherTextInt());
                }

                byte[] cip = ToTab.cutTab(tmpByte, times*privateKey.length, rest);
                fileOutputStream.write(ToTab.toInt(cip));

                fileOutputStream.close();
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
                FileInputStream fileInputStream = new FileInputStream(file);

                String name = ToTab.replace(file.getAbsolutePath(), File.separatorChar, nameFile.getText());
                File newFile = new File(name);
                newFile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                // TODO
                //  Deszyfrowanie cale do zrobienia od nowa ;/

                fileOutputStream.close();
                fileInputStream.close();

                progress = 0;
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
