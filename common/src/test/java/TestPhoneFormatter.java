import net.thenightwolf.dm.common.model.message.PhoneFormatter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestPhoneFormatter {

    private String expectedCleanedResult = "2339334999";
    private String expectedPrettyResult = "(233) 933-4999";

    @Test
    public void testTenDigitPhoneNumber(){

        String tenDigitNumber = "(233) 933-4999";
        String cleaned = PhoneFormatter.cleanPhoneNumber(tenDigitNumber);
        assertTrue(expectedCleanedResult.equals(cleaned));
    }

    @Test
    public void testElevenDigitPhoneNumber(){
        String elevenDigitNumber = "+1 (233) 933-4999";
        String cleaned = PhoneFormatter.cleanPhoneNumber(elevenDigitNumber);
        assertTrue(expectedCleanedResult.equals(cleaned));
    }

    @Test
    public void testTenDigitPrettyFormat(){
        String pretty = PhoneFormatter.prettyPhoneNumber(expectedCleanedResult);
        assertTrue(pretty.equals(expectedPrettyResult));
    }
}
