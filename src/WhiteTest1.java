import static org.junit.jupiter.api.Assertions.*;

class WhiteTest1 {
    @org.junit.jupiter.api.Test
    void test() {
        Main.main(null);
        String start = "Eleven";
        assertEquals("A",Main.randomWalk(start));
    }
}