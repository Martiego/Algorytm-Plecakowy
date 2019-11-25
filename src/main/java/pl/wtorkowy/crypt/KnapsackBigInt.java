package pl.wtorkowy.crypt;

public class KnapsackBigInt {
    private BigInt[] privateKey;
    private BigInt[] publicKey;

    private BigInt n, m, reverseN;
    private Generator generator = new Generator();

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
        BigInt i = new BigInt("0");
        BigInt one = new BigInt("1");
        BigInt tmp;
//        System.out.println("N: " + n);

        while (!i.isEqual(m)) {
            tmp = n.multiply(i);
//            System.out.println("N*i: " + tmp + " * " + i);
            tmp = tmp.mod(m);
//            System.out.println("tmp mod: " + tmp);
            if(tmp.isEqual(one)) {
                return i;
            }

            i = i.add(one);
        }

        return new BigInt("0");
//        BigInt u, w, x, z, q;
//        BigInt zero = new BigInt("0");
//        BigInt one = new BigInt("1");
//
//        u = one;
//        x = zero;
//        w = n;
//        z = m;
//
//        while(!w.isEqual(zero)) {
//            if(w.isLess(z)) {
//                q = u;
//                u = x;
//                x = q;
//                q = w;
//                w = z;
//                z = q;
//            }
//            q = w.divide(z);
//            u = u.subtract(q.multiply(x));
//            w = w.subtract(q.multiply(z));
//            //System.out.println("x: " + x);
//        }
//
//        if(z.isEqual(one)) {
//            if(x.isLess(zero))
//                x = x.add(m);
//
//            return x;
//        }
//        return x;
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
}
