import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class BlackTest1 {
    @org.junit.jupiter.api.Test
    void testNull() {
        Main.main(null);
        ArrayList expected = new ArrayList();
        expected.add("two");
        expected.add("six");
        String Word1 = "one";
        String Word2 = "three";
        assertEquals(expected,Main.queryBridgeWords(Word1, Word2));
    }
}