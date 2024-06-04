import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class BlackTest3 {
    @org.junit.jupiter.api.Test
    void testNull() {
        Main.main(null);
        ArrayList expected = new ArrayList();
        String Word1 = "";
        String Word2 = "";
        assertEquals(expected,Main.queryBridgeWords(Word1,Word2));
    }
}