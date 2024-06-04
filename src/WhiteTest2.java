import static org.junit.jupiter.api.Assertions.*;

class WhiteTest2 {
    @org.junit.jupiter.api.Test
    void test() {
        Main.main(null);
        String start = "ten";
        assertEquals("A",Main.randomWalk(start));
    }
}