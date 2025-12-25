package booker;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testAppExists() {
        App classUnderTest = new App();
        assertNotNull("app class should exist", classUnderTest);
    }
}
