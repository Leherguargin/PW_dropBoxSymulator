package sample;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Client {
    public final static int MAX_FILES = 5;
    private final long id = GenID.getNext();
    private final LocalTime time;
    private long waitingTime;
    private final int file1;
    private final int file2;
    private final int file3;
    private final int file4;
    private final int file0;
    private double prior = 0;

    public Client(LocalTime time, int file1, int file2, int file3, int file4, int file0) {
        this.time = time;
        this.file1 = file1;
        this.file2 = file2;
        this.file3 = file3;
        this.file4 = file4;
        this.file0 = file0;
        getWaitingTime();
    }

    public Client() {
        Random random = new Random();
        final int MAX_SIZE = 10_000;
        file0 = random.nextInt(MAX_SIZE);
        file1 = random.nextInt(MAX_SIZE);
        file2 = random.nextInt(MAX_SIZE);
        file3 = random.nextInt(MAX_SIZE);
        file4 = random.nextInt(MAX_SIZE);
        this.time = LocalTime.now();
        this.waitingTime = 0;
    }

    public long getWaitingTime() {
        this.waitingTime = this.time.until(LocalTime.now(), ChronoUnit.SECONDS);
        return waitingTime;
    }

    public synchronized void updateWaitingTime() {
        this.waitingTime = this.time.until(LocalTime.now(), ChronoUnit.SECONDS);
    }

    public double getPrior() {
        return prior;
    }

    public void setPrior(double prior) {
        this.prior = prior;
    }

    public long getId() {
        return id;
    }

    public int getFile1() {
        return file1;
    }

    public int getFile2() {
        return file2;
    }

    public int getFile3() {
        return file3;
    }

    public int getFile4() {
        return file4;
    }

    public int getFile0() {
        return file0;
    }

    public VBox render() {
        VBox clientLayout = new VBox();
        Group group = new Group();

        Rectangle rect = new Rectangle(90.0, 200.0);
        rect.setFill(Paint.valueOf("#8fa3b5"));
        rect.setArcHeight(5.0);
        rect.setArcWidth(5.0);
        rect.setStroke(Paint.valueOf("black"));
        rect.setStrokeType(StrokeType.INSIDE);

        Label clientName = new Label(Long.toString(id));
        clientName.setAlignment(Pos.CENTER);
        clientName.setContentDisplay(ContentDisplay.CENTER);
        clientName.setLayoutX(0.0);
        clientName.setLayoutY(0.0);
        clientName.setPrefWidth(90.0);
        clientName.setPrefHeight(17.0);

        group.getChildren().addAll(rect, clientName);

        clientLayout.getChildren().add(group);
        return clientLayout;
    }
}
