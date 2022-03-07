package sample;

public class GenID {
    private static Long max = 0L;

    public static Long getNext() {
        return max++;
    }
}
