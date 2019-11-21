package pl.wtorkowy.crypt;

import pl.wtorkowy.cast.ToTab;
import pl.wtorkowy.crypt.Euklides;

import java.util.Arrays;

public class Knapsack {
    private int[] privateKey;
    private int[] publicKey;
    private int[] cipherText;
    private byte[] decipherText;

    private int n, m, reverseN;
    private boolean superIncreasing;

    public Knapsack(int[] privateKey, int n, int m) {
        this.privateKey = privateKey;
        publicKey = new int[privateKey.length];
        this.n = n;
        this.m = m;
        reverseN = reverseN(n, m);
        generatePublicKey();
        superIncreasing = checkSuperIncreasing();
        //TODO
        // Przypisac do zmiennej zebv mozna bylo sprawdzac
        Euklides.isRelativelyPrime(m, n);
    }

    public void encrypt(byte[] textByte) {
        if(superIncreasing) {
            int tmp = 0;
            cipherText = new int[textByte.length/publicKey.length];
            for(int i = 0; i < textByte.length/publicKey.length; i++) {
                for (int j = 0; j < publicKey.length; j++) {
                    if(textByte[i*publicKey.length + j] == 1)
                        tmp += publicKey[j];
                }
                cipherText[i] = tmp;
                tmp = 0;
            }
        }
        else {
            cipherText = new int[] {0};
        }
    }

    public void decrypt(int[] cipherText) {
        int tmp = 0;
        int length = privateKey.length;
        byte[] tmpByte = new byte[length];
        decipherText = new byte[length*cipherText.length];
        for(int i = 0; i < cipherText.length; i++) {
            tmp = (cipherText[i]*reverseN)%m;
            for (int j = length - 1; j >= 0; j--) {
                if(privateKey[j] > tmp) {
                    tmpByte[j] = 0;
                }
                else {
                    tmp -= privateKey[j];
                    tmpByte[j] = 1;
                }
            }
            System.arraycopy(tmpByte, 0, decipherText, i * length, length);
        }
    }

    public int reverseN(int n, int m) {
        for(int i = 0; i < m; i++) {
            if (((n*i)%m)==1) return (i);
        }
        return 0;
    }

    public void generatePublicKey() {
        for (int i = 0; i < privateKey.length; i++) {
            publicKey[i] = (privateKey[i] * n) % m;
        }
    }

    public boolean checkSuperIncreasing() {
        for (int i = 0; i < privateKey.length - 2; i++) {
            if (privateKey[i+1] + privateKey[i] >= privateKey[i+2])
                return false;
        }
        return true;
    }

    public boolean isSuperIncreasing() {
        return superIncreasing;
    }

    public byte[] getDecipherText() {
        return decipherText;
    }

    public String getDecipherTextString() {
        return new String(ToTab.toCharTab(ToTab.toIntTab(decipherText)));
    }

    public int[] getCipherText() {
        return cipherText;
    }

    public String getCipherTextString() {
        return Arrays.toString(cipherText);
    }

    public String getPublicKey() {
        return Arrays.toString(publicKey);
    }

    public int getCipherTextInt() {
        return cipherText[0];
    }
}
