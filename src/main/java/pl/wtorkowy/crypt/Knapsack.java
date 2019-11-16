package pl.wtorkowy.crypt;

import pl.wtorkowy.cast.ToTab;

public class Knapsack {
    private int[] privateKey;
    private int[] publicKey;
    private char[] text;
    private byte[] textByte;
    private int[] cipherText;
    private byte[] decipherText;

    int n, m, reverseN;

    public Knapsack() {
//        text = new char[] {'c', 'i', 'e', 'k', 'a', 'w', 'e'};
        textByte = new byte[] {0,1,1,0,0,0,1,1,0,1,0,1,1,0,1,1,1,0};
        privateKey = new int[] {2, 3, 6, 13, 27, 52};
        publicKey = new int[privateKey.length];
        n = 31;
        m = 105;
        reverseN = reverseN(n, m);
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

    public void decrypt() {
        int tmp = 0;
        int length = privateKey.length;
        byte[] tmpByte = new byte[length];
        decipherText = new byte[textByte.length];
        for(int i = 0; i < textByte.length/length; i++) {
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

    public byte[] getDecipherText() {
        return decipherText;
    }

    public int[] getCipherText() {
        return cipherText;
    }

    public int[] getPublicKey() {
        return publicKey;
    }
}
