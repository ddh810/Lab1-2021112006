import static org.junit.jupiter.api.Assertions.*;

class WhiteTest4 {
    @org.junit.jupiter.api.Test
    void test() {
        Main.main(null);
        String start = "nine";
        assertEquals("B",Main.randomWalk(start));
    }
}