package pl.wtorkowy.crypt;

import java.util.Random;

public class Generator {
    private String[] numbers = { "11",  "4",  "3", "22", "16",  "6", "38", "44", "29", "10",
                                         "1", "12", "20", "14", "15", "50", "17", "18", "19", "13",
                                        "21",  "2", "23", "24", "25", "26",  "7", "28",  "9", "30",
                                        "31", "32", "33", "34", "35", "36", "41", "27", "39", "37",
                                        "40", "42", "43",  "8", "45", "46", "47", "48", "49",  "5"};

    private BigInt privateKey;
    private BigInt n;
    private BigInt m;

    public BigInt[] generate(int times) {
        while(times%8 != 0)
            times++;

        String tmp = "";
        Random rand = new Random();
        BigInt[] result = new BigInt[times];

        tmp += numbers[rand.nextInt(numbers.length)];
        result[0] = new BigInt(tmp);
        result[1] = new BigInt(tmp).add(new BigInt(numbers[rand.nextInt(numbers.length)]));
        privateKey = result[0].add(result[1]);

        for (int i = 2; i < times; i++) {
            result[i] = new BigInt(privateKey.add(new BigInt(numbers[rand.nextInt(numbers.length)])).toString());
            privateKey = privateKey.add(result[i]);
        }

        for (int i = 0; i < 3; i++) {
            privateKey = privateKey.add(new BigInt(numbers[rand.nextInt(numbers.length)]));
        }

        m = privateKey;
        tmp = String.valueOf((rand.nextInt(9) + 1));
        for (int i = 0; i < m.getNumber().length*3/5 ; i++) {
            tmp += rand.nextInt(10);
        }
        n = new BigInt(tmp);

        while (!Euklides.isRelativelyPrime(m ,n)) {
            n = n.add(new BigInt(numbers[rand.nextInt(numbers.length)]));
        }

        return result;
    }

    public BigInt getN() {
        return n;
    }

    public BigInt getM() {
        return m;
    }
}
