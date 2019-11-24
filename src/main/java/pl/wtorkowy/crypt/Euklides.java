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

    private static int algorithmEuclidean(int m, int n) {
        int temp;

        while(n != 0) {
            temp = m%n;
            m = n;
            n = temp;
        }

        return m;
    }

    public static boolean isRelativelyPrime (BigInt m, BigInt n) {
        return algorithmEuclidean(m, n).toString().equals("1");
    }

    public static boolean isRelativelyPrime (int m, int n) {
        return algorithmEuclidean(m, n) == 1;
    }
}
