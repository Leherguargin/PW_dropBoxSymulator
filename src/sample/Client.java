package sample;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.nanoTime;

public class Client {
    private ArrayList<File> files = new ArrayList<>();
    private long time;
    private String name;
    private static final int MAX_FILES = 3;

    Client(String name) {
        this.time = nanoTime();
        this.name = name;
        Random random = new Random();
        Integer filesCount = 1 + random.nextInt(MAX_FILES);
        for (int i = 0; i < filesCount; i++) {
            this.files.add(new File());
        }

    }

    public long getWaitingTime() {
        return nanoTime() - time;
    }

    public ArrayList<File> getFiles() {
        return files;
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
        for (File file : files) {
            VBox fileData = new VBox();
            Label fileName = new Label("file" + fileNumber);
            fileName.setAlignment(Pos.CENTER);
            fileName.setContentDisplay(ContentDisplay.CENTER);
            fileName.setLayoutX(0.0);
            Double Y1 = 17.0 * fileNumber++;
            fileName.setLayoutY(Y1);
            fileName.setPrefWidth(80.0);
            fileName.setPrefHeight(17.0);

            ProgressBar progressBar = new ProgressBar(file.getProgress());
            progressBar.setLayoutX(5.0);
            Double Y2 = 17.0 * fileNumber++;
            progressBar.setLayoutY(Y2);
            progressBar.setPrefWidth(80.0);
            progressBar.setPrefHeight(17.0);

            fileData.getChildren().addAll(fileName, progressBar);
            group.getChildren().add(fileData);
        }

        clientLayout.getChildren().add(group);
        return clientLayout;
    }
}
