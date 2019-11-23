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
        number = val;
    }

    public BigInt add(BigInt val) {
        byte[] tmpTab = compare(this, val);
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

    private byte[] compare(BigInt a, BigInt b) {
        if(a.getNumber().length > b.getNumber().length)
            return a.getNumber();
        else
            return b.getNumber();
    }


    public byte[] getNumber() {
        return number;
    }

    @Override
    public String toString() {
        String text = "";
        int len = number.length - 1;

        if(number[len] != 0)
            text += number[len];

        for (int i = len - 1; i >= 0; i--) {
            text += number[i];
        }

        return text;
    }
}
