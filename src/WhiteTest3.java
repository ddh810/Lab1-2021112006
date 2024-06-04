import static org.junit.jupiter.api.Assertions.*;

class WhiteTest3 {
    @org.junit.jupiter.api.Test
    void test() {
        Main.main(null);
        String start = "one";
        assertEquals("D",Main.randomWalk(start));
    }
}