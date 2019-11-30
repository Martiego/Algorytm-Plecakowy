package pl.wtorkowy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.wtorkowy.crypt.BigInt;

public class BigIntTest {

    @ParameterizedTest(name = "{0},{1},{2},{3},{4}")
    @CsvSource({
            "10, true, 20, true, 30",
            "15, true, 5, false, 10",
            "20, false, 10, true, -10",
            "50, false, 10, false, -60"
    })
    void add(String first, boolean firstSign, String second, boolean secondSign, String expectedResult) {
        BigInt a = new BigInt(first, firstSign);
        BigInt b = new BigInt(second, secondSign);
        assertEquals(expectedResult, a.add(b).toString(),
                () -> first + " + " + second + " should equal " + expectedResult);
    }

    @ParameterizedTest(name = "{0},{1},{2},{3},{4}")
    @CsvSource({
            "10, true, 20, true, -10",
            "15, true, 5, false, 20",
            "20, false, 10, true, -30",
            "50, false, 10, false, -40"
    })
    void subtract(String first, boolean firstSign, String second, boolean secondSign, String expectedResult) {
        BigInt a = new BigInt(first, firstSign);
        BigInt b = new BigInt(second, secondSign);
        assertEquals(expectedResult, a.subtract(b).toString(),
                () -> first + " - " + second + " should equal " + expectedResult);
    }
}
