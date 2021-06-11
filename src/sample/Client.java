package sample;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.Random;

import static java.lang.System.nanoTime;

public class Client {
    private final long time;
    private final String name;
    private final long size0;
    private final long size1;
    private final long size2;
    private final long id;
    public boolean uploaded = false;
    public long prior = 0;
    public static final int MAX_FILES = 3;
    private final int MAX_SIZE = 10000;

    Client(String name, long id) {
        this.id = id;
        this.time = nanoTime();
        this.name = name;
        Random random = new Random();
        size0 = random.nextInt(MAX_SIZE);
        size1 = random.nextInt(MAX_SIZE);
        size2 = random.nextInt(MAX_SIZE);
    }

    public long getTime() {
        return time;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public long getSize0() {
        return size0;
    }

    public long getSize1() {
        return size1;
    }

    public long getSize2() {
        return size2;
    }

    public long getWaitingTime() {
        return nanoTime() - time;
    }

    public long getSizes() {
        return size0 + size1 + size2;
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

        Label clientName = new Label(name);
        clientName.setAlignment(Pos.CENTER);
        clientName.setContentDisplay(ContentDisplay.CENTER);
        clientName.setLayoutX(0.0);
        clientName.setLayoutY(0.0);
        clientName.setPrefWidth(90.0);
        clientName.setPrefHeight(17.0);

        group.getChildren().addAll(rect, clientName);

        int fileNumber = 1;
//        for (File file : files) {
//            VBox fileData = new VBox();
//            Label fileName = new Label("file" + fileNumber);
//            fileName.setAlignment(Pos.CENTER);
//            fileName.setContentDisplay(ContentDisplay.CENTER);
//            fileName.setLayoutX(10.0);
//            double Y1 = 17.0 * fileNumber++;
//            fileName.setLayoutY(Y1);
//            fileName.setPrefWidth(80.0);
//            fileName.setPrefHeight(17.0);
//
//            ProgressBar progressBar = new ProgressBar(file.getProgress());
//            progressBar.setLayoutX(5.0);
//            double Y2 = 17.0 * fileNumber++;
//            progressBar.setLayoutY(Y2);
//            progressBar.setPrefWidth(80.0);
//            progressBar.setPrefHeight(17.0);
//
//            fileData.getChildren().addAll(fileName, progressBar);
//            group.getChildren().add(fileData);
//        }

        clientLayout.getChildren().add(group);
        return clientLayout;
    }
}
