package sample;

import javafx.scene.layout.VBox;

import java.util.Random;

public class File {
    private Double progress;
    private Integer size;

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
