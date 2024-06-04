import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class BlackTest5 {
    @org.junit.jupiter.api.Test
    void testNull() {
        Main.main(null);
        ArrayList expected = new ArrayList();
        String Word1 = "1st";
        String Word2 = "3rd";
        assertEquals(expected,Main.queryBridgeWords(Word1,Word2));
    }
}