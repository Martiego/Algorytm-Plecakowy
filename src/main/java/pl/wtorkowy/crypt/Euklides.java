package pl.wtorkowy.crypt;

public class Euklides {

    private static BigInt algorithmEuclidean(BigInt m, BigInt n) {
        BigInt temp;

        while (!n.toString().equals("0")) {
            temp = m.mod(n);
            m = n;
            n = temp;
        }
        return m;
    }

    public static boolean isRelativelyPrime (BigInt m, BigInt n){
        if (algorithmEuclidean(m, n).toString().equals("1")) {
            return true;
        }
        else {
            return false;
        }

    }
}
