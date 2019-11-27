package pl.wtorkowy.crypt;

public class BigInt {
    private byte[] number;
    private boolean positive = true;

    public BigInt(String val) {
        number = new byte[val.length()];

        for (int i = 0; i < val.length(); i++) {
            number[i] = (byte) Character.getNumericValue(val.charAt(val.length() - i - 1));
        }
    }

    public BigInt(String val, boolean positive) {
        number = new byte[val.length()];

        for (int i = 0; i < val.length(); i++) {
            number[i] = (byte) Character.getNumericValue(val.charAt(val.length() - i - 1));
        }

        this.positive = positive;
    }

    public BigInt(byte[] val) {

        number = trimZero(val);
    }

    public BigInt add(BigInt val) {
        byte[] tmpTab = getHigher(this, val);
        byte[] result = new byte[Math.max(number.length, val.number.length)+1];
        BigInt resultInt;

        if(this.positive && !val.positive) {
            val.positive = true;
            return this.subtract(val);
        }

        if(!this.positive && val.positive) {
            this.positive = true;
            return val.subtract(this);
        }

        if(number.length > val.number.length)
            System.arraycopy(val.number, 0, result, 0, val.number.length);
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

        if(tmp == 1) {
            result[result.length-1] = 1;
        }

        resultInt = new BigInt(result);

        if(!this.positive && !val.positive)
            resultInt.positive = false;

        return resultInt;
    }

    public BigInt subtract(BigInt val) {
        if (this.positive == val.positive) {
            if(this.positive) {
                if(this.isHigherEqual(val))
                    return this.subtractHelper(val, true);
                else
                    return val.subtractHelper(this, false);
            }
            else {
                if(this.isHigherEqual(val)) {
                    this.positive = true;
                    val.positive = true;
                    return this.subtractHelper(val, false);
                }
                else {
                    this.positive = true;
                    val.positive = true;
                    return val.subtractHelper(this, true);
                }
            }
        }
        else {
            if (this.positive) {
                val.positive = true;
                return this.add(val);
            }
            else {
                val.positive = false;
                return this.add(val);
            }
        }
    }

    private BigInt subtractHelper(BigInt val, boolean positive) {
        BigInt tmpInt = new BigInt(this.toString(), positive);
        int tmp = 1;

        for (int i = 0; i < val.number.length; i++) {
            if(tmpInt.number[i] - val.number[i] < 0) {
                while(tmpInt.number[i+tmp] == 0) {
                    tmp++;
                }
                tmpInt.number[i+tmp] = (byte) (tmpInt.number[i+tmp] - 1);
                while (tmp != 0) {
                    tmp--;
                    if(tmp == 0)
                        tmpInt.number[i+tmp] = (byte) (tmpInt.number[i+tmp] + 10);
                    else
                        tmpInt.number[i+tmp] = (byte) (tmpInt.number[i+tmp] + 9);
                }

                tmpInt.number[i] = (byte) (tmpInt.number[i] - val.number[i]);
                tmp = 1;
            }
            else {
                tmpInt.number[i] = (byte) (tmpInt.number[i] - val.number[i]);
            }
        }

        tmpInt.number = tmpInt.trimZero(tmpInt.number);

        return tmpInt;
    }

    public BigInt multiply(BigInt val) {
        BigInt higherInt = getHigherInt(this, val);
        BigInt lowerInt = getLowerInt(this, val);
        BigInt[] multiplyTab = new BigInt[lowerInt.number.length];
        BigInt result = new BigInt("0");
        boolean positiv = true;

        if((!this.positive && val.positive) || (this.positive && !val.positive))
            positiv = false;

        higherInt.positive = true;
        lowerInt.positive = true;


        multiplyTab[0] = higherInt;

        for (int i = 1; i < multiplyTab.length; i++) {
            multiplyTab[i] = multiplyTab[i-1].multiplyHelper(new BigInt("10"));
        }

        for (int i = 0; i < multiplyTab.length; i++) {
            for (int j = 0; j < lowerInt.number[i]; j++) {
                result = result.add(multiplyTab[i]);
            }
        }
        result.positive = positiv;

        return result;
    }

    private BigInt multiplyHelper(BigInt val) {
        BigInt tmpInt = new BigInt("1");
        BigInt lowerInt = getLowerInt(this, val);
        BigInt result = getHigherInt(this, val);
        BigInt tmp = getHigherInt(this, val);

        while(!lowerInt.toString().equals("1")) {
            result = result.add(tmp);

            lowerInt = lowerInt.subtract(tmpInt);
        }

        return result;
    }

    public BigInt mod(BigInt val) {
        BigInt tmp;
        BigInt tmp2;
        BigInt ten = new BigInt("10");


        if(this.isEqual(val)) {
            return new BigInt("0");
        }
        else if(isHigher(this, val)) {
            tmp = this;
            tmp2 = val;

            BigInt[] multiplyTab = new BigInt[(tmp.number.length - tmp2.number.length)+1];

            multiplyTab[0] = tmp2;
            for (int i = 1; i < multiplyTab.length; i++) {
                multiplyTab[i] = multiplyTab[i-1].multiply(ten);
            }

            for (int i = multiplyTab.length - 1; i >= 0; i--) {
                while(multiplyTab[i].isLessEqual(tmp)) {
                    tmp = tmp.subtract(multiplyTab[i]);
                }
            }

            return tmp;
        }
        else {
            return this;
        }

    }

    public BigInt divide(BigInt val) {
        BigInt tmp;
        BigInt tmp2;
        BigInt ten = new BigInt("10");
        BigInt result = new BigInt("0");


        if(this.isEqual(val)) {
            return new BigInt("1");
        }
        else if(isHigher(this, val)) {
            tmp = this;
            tmp2 = val;
            BigInt[] multiplyTab = new BigInt[(tmp.number.length - tmp2.number.length)+1];
            BigInt[] tmpTab = new BigInt[(tmp.number.length - tmp2.number.length)+1];

            multiplyTab[0] = tmp2;
            tmpTab[0] = new BigInt("1");
            for (int i = 1; i < multiplyTab.length; i++) {
                multiplyTab[i] = multiplyTab[i-1].multiply(ten);
                tmpTab[i] = tmpTab[i-1].multiply(ten);
            }

            for (int i = multiplyTab.length - 1; i >= 0; i--) {
                while(multiplyTab[i].isLessEqual(tmp)) {
                    tmp = tmp.subtract(multiplyTab[i]);
                    result = result.add(tmpTab[i]);
                }
            }

            if((!this.positive && val.positive) || (this.positive && !val.positive))
                result.positive = false;

            return result;
        }
        else {
            return new BigInt("0");
        }

    }

    private byte[] getHigher(BigInt a, BigInt b) {
        if(a.number.length > b.number.length)
            return a.number;
        else
            return b.number;
    }

    private BigInt getHigherInt(BigInt a, BigInt b) {
        if(a.number.length > b.number.length)
            return a;
        else if(a.number.length == b.number.length) {
            if(isHigher(a, b))
                return a;
            else
                return b;
        }
        else
            return b;
    }

    private BigInt getLowerInt(BigInt a, BigInt b) {
        if(a.number.length < b.number.length)
            return a;
        else if(a.number.length == b.number.length){
            if(!isHigher(a,b))
                return a;
            else
                return b;
        }
        else
            return b;
    }

    private boolean isHigher(BigInt a, BigInt b) {
        if(a.number.length > b.number.length)
            return true;

        if(a.number.length < b.number.length)
            return false;

        for (int i = a.number.length - 1; i >= 0; i--) {
            if(a.number[i] < b.number[i])
                return false;
            else if(a.number[i] > b.number[i])
                return true;
        }

        return true;
    }

    private boolean isHigherEqual(BigInt val) {
        if(this.number.length > val.number.length)
            return true;

        if(this.number.length < val.number.length)
            return false;

        for (int i = this.number.length - 1; i >= 0; i--) {
            if(this.number[i] < val.number[i])
                return false;
            else if(this.number[i] > val.number[i])
                return true;
        }

        return true;
    }

    public boolean isLess(BigInt val) {
        if(this.number.length < val.number.length)
            return true;
        else if(this.number.length == val.number.length) {
            if(isHigher(this, val))
                return false;
            else
                return true;
        }
        else
            return false;
    }

    public boolean isLessEqual(BigInt val) {
        if(this.number.length < val.number.length)
            return true;
        else if(this.number.length == val.number.length) {
            if(this.isEqual(val))
                return true;
            else if(isHigher(this, val))
                return false;
            else
                return true;
        }
        else
            return false;
    }

    public boolean isEqual(BigInt val) {
        if(number.length == val.number.length) {
            for (int i = 0; i < number.length; i++) {
                if (number[i] != val.number[i])
                    return false;
            }
            return true;
        }

        return false;
    }

    public boolean isPositive() {
        return positive;
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
        if(!positive)
            text += "-";

        int len = number.length - 1;

        for (int i = len; i >= 0; i--) {
            text += number[i];
        }

        return text;
    }
}
