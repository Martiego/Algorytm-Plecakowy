package pl.wtorkowy.crypt;

//m has to be greater than n
public class Euklides {

    private static int algorithmEuclidean(int m, int n) {

        int temp;

        while (n != 0 ) {
            temp = m%n;
            m = n;
            n = temp;
        }

        return m;
    }

    public static boolean isRelativelyPrime (int m, int n){
        return algorithmEuclidean(m, n) == 1;
    }
}
