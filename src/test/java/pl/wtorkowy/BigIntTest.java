package pl.wtorkowy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.wtorkowy.crypt.BigInt;

public class BigIntTest {
    @Test
    @DisplayName("adding numbers")
    void addTwoNumbers() {
        BigInt a = new BigInt("200", true);
        BigInt b = new BigInt("100", true);

        assertEquals("300", a.add(b).toString(), "100 + 200 = 300");

        b.setPositive(false);

        assertEquals("100", a.add(b).toString(), "200 + (-100) = 100");

        a.setPositive(false);
        b.setPositive(true);

        assertEquals("-100", a.add(b).toString(), "-200 + 100 = -100");

        b.setPositive(false);

        assertEquals("-300", a.add(b).toString(), "-200 + (-100) = -300");
    }
}
