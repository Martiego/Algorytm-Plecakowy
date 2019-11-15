package pl.wtorkowy.crypt;

import pl.wtorkowy.cast.ToTab;

public class Knapsack {
    private int[] privateKey;
    private int[] publicKey;
    private char[] text;
    private byte[] textByte;
    private int[] cipherText;

    int n, m;

    public Knapsack() {
//        text = new char[] {'c', 'i', 'e', 'k', 'a', 'w', 'e'};
        textByte = new byte[] {0,1,1,0,0,0,1,1,0,1,0,1,1,0,1,1,1,0};
        privateKey = new int[] {2, 3, 6, 13, 27, 52};
        publicKey = new int[privateKey.length];
        n = 31;
        m = 105;
    }

    public void encrypt() {
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


    public void generatePublicKey() {
        for (int i = 0; i < privateKey.length; i++) {
            publicKey[i] = (privateKey[i] * n) % m;
        }
    }

    public int[] getCipherText() {
        return cipherText;
    }

    public int[] getPublicKey() {
        return publicKey;
    }
}
