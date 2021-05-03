package sample;

import java.util.Random;

public class File {
    private final Double progress;
    private final Integer size;

    public File() {
        Random random = new Random();
        this.progress = 10.0;
        this.size = random.nextInt(300);
    }

    public Double getProgress() {
        return progress;
    }

    public Integer getSize() {
        return size;
    }
}
