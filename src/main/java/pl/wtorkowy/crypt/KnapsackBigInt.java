package pl.wtorkowy.crypt;

import java.math.BigInteger;

public class KnapsackBigInt {
    private BigInt[] privateKey;
    private BigInt[] publicKey;

    private BigInt n, m, reverseN;
    private Generator generator = new Generator();

    private BigInt[] cipherText;
    private byte[] decipherText;

    public KnapsackBigInt(int times) {
        privateKey = generator.generate(times);
        n = generator.getN();
        m = generator.getM();
        reverseN = reverseN(n, m);
        publicKey = generatePublicKey(privateKey);
    }

    private BigInt[] generatePublicKey(BigInt[] privateKey) {
        BigInt[] publicKey = new BigInt[privateKey.length];

        for (int i = 0; i < privateKey.length; i++) {
            publicKey[i] = (privateKey[i].multiply(n)).mod(m);
        }

        return publicKey;
    }

    public BigInt reverseN(BigInt n, BigInt m) {
        BigInt u, w, x, z, q;
        BigInt zero = new BigInt("0");
        BigInt one = new BigInt("1");

        q = zero;
        u = one;
        x = zero;
        w = n;
        z = m;
        System.out.println(w);
        System.out.println(z);


        while(!w.isEqual(zero)) {
            if(w.isLess(z)) {
                q = u;
                u = x;
                x = q;
                q = w;
                w = z;
                z = q;
            }
            q = w.divide(z);
            u = u.subtract(q.multiply(x));
            w = w.subtract(q.multiply(z));
        }

        if(z.isEqual(one)) {
            if(x.isLess(zero)) {
                x = x.add(m);
            }

            return x;
        }
        return x;
    }


    public void encrypt(byte[] text) {
        BigInt tmp = new BigInt("0");
        cipherText = new BigInt[text.length / publicKey.length];
        for (int i = 0; i < text.length / publicKey.length; i++) {
            for (int j = 0; j < publicKey.length; j++) {
                if (text[i * publicKey.length + j] == 1)
                    tmp = tmp.add(publicKey[j]);
            }
            cipherText[i] = tmp;
            tmp = new BigInt("0");
        }
    }

    public void decrypt(BigInt[] text) {
        BigInt tmp;
        int length = privateKey.length;
        decipherText = new byte[length*text.length];
        for(int i = 0; i < text.length; i++) {
            tmp = (text[i].multiply(reverseN)).mod(m);
            for (int j = length - 1; j >= 0; j--) {
                if(!privateKey[j].isLessEqual(tmp)) {
                    decipherText[j+i*length] = 0;
                }
                else {
                    tmp = tmp.subtract(privateKey[j]);
                    decipherText[j+i*length] = 1;
                }
            }

        }
    }

    public BigInt[] getPublicKey() {
        return publicKey;
    }

    public BigInt getReverseN() {
        return reverseN;
    }

    public BigInt[] getPrivateKey() {
        return privateKey;
    }

    public BigInt getN() {
        return n;
    }

    public BigInt getM() {
        return m;
    }

    public BigInt[] getCipherText() {
        return cipherText;
    }

    public byte[] getDecipherText() {
        return decipherText;
    }
}
