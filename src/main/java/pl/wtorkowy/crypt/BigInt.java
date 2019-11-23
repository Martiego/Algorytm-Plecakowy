package pl.wtorkowy.crypt;

/**
 *  Bardzo przyjemna klasa, po prostu liczby są zapisywane do tablicy byteow i symulujemy tak, żeby
 *  można było się nimi posługiwać jak normalnymi liczbami. Dla naszego ułatwienia liczby są zapisywane
 *  od końca. Index 0 to ostatnia cyfra. Operacje robione są tak jakby wykonywano je w słupku.
 */

public class BigInt {
    private byte[] number;

    public BigInt(String val) {
        number = new byte[val.length()];

        for (int i = 0; i < val.length(); i++) {
            number[i] = (byte) Character.getNumericValue(val.charAt(val.length() - i - 1));
        }
    }

    public BigInt(byte[] val) {

        number = trimZero(val);
    }

    public BigInt add(BigInt val) {
        byte[] tmpTab = getHigher(this, val);
        byte[] result = new byte[Math.max(number.length, val.number.length)+1];

        if(number.length > val.number.length)
            System.arraycopy(val.getNumber(), 0, result, 0, val.getNumber().length);
        else
            System.arraycopy(number, 0, result, 0, number.length);

        byte tmp = 0;

        for (int i = 0; i < tmpTab.length; i++) {
            if (result[i] + tmpTab[i] + tmp >= 10) {
                result[i] = (byte) (result[i] + tmpTab[i] + tmp - 10);
                tmp = 1;
            }
            else {
                result[i] = (byte) (result[i] + tmpTab[i] + tmp);
                tmp = 0;
            }
        }

        if(tmp == 1)
            result[result.length-1] = 1;

        return new BigInt(result);
    }

    public BigInt subtract(BigInt val) {
        int tmp = 1;

        if(number.length < val.number.length) {
            return new BigInt("-");
        }

        if (number.length == val.number.length && !isHigher(this, val)) {
            return new BigInt("-");
        }

        for (int i = 0; i < val.number.length; i++) {
            if(number[i] - val.number[i] < 0) {
                while(number[i+tmp] == 0) {
                    tmp++;
                }
                number[i+tmp] = (byte) (number[i+tmp] - 1);
                while (tmp != 0) {
                    tmp--;
                    if(tmp == 0)
                        number[i+tmp] = (byte) (number[i+tmp] + 10);
                    else
                        number[i+tmp] = (byte) (number[i+tmp] + 9);
                }

                number[i] = (byte) (number[i] - val.number[i]);
            }
            else {
                number[i] = (byte) (number[i] - val.number[i]);
            }
        }

        return new BigInt(number);
    }

    public BigInt multiply(BigInt val) {
        BigInt tmpInt = new BigInt("1");
        BigInt lowerInt = getLowerInt(this, val);
        BigInt result = getHigherInt(this, val);
        BigInt tmp = getHigherInt(this, val);

        while(!lowerInt.toString().equals("1")) {
            result = result.add(tmp);

            lowerInt = lowerInt.subtract(tmpInt);
            System.out.println(lowerInt);
        }

        return result;
    }


    //TODO
    // operacja modulo
    public BigInt mod() { return new BigInt("-"); }

    private byte[] getHigher(BigInt a, BigInt b) {
        if(a.number.length > b.number.length)
            return a.number;
        else
            return b.number;
    }

    private BigInt getHigherInt(BigInt a, BigInt b) {
        if(a.number.length > b.number.length)
            return a;
        else
            return b;
    }

    private BigInt getLowerInt(BigInt a, BigInt b) {
        if(a.number.length < b.number.length)
            return a;
        else
            return b;
    }

    private boolean isHigher(BigInt a, BigInt b) {
        if (a.number[0] < b.number[0])
            return false;

        for (int i = 1; i < a.number.length; i++) {
            if(a.number[i] < b.number[i])
                return false;
        }

        return true;
    }

    private byte[] trimZero(byte[] number) {
        int tmp = number.length - 1;
        while(number[tmp] == 0) {
            tmp--;
            if(tmp <= 0)
                return new byte[] { number[0] };
        }

        byte[] result = new byte[tmp+1];

        for (int i = 0; i < result.length; i++) {
            result[i] = number[i];
        }

        return result;
    }


    public byte[] getNumber() {
        return number;
    }

    @Override
    public String toString() {
        String text = "";
        int len = number.length - 1;

        for (int i = len; i >= 0; i--) {
            text += number[i];
        }

        return text;
    }
}
