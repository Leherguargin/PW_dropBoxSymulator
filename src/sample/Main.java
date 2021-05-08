package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//       Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(15.0, 12.0, 15.0, 12.0));
        mainLayout.setSpacing(10.0);
        mainLayout.setStyle("-fx-background-color: #7d8ea3;");

        HBox topLayout = new HBox();
        topLayout.setSpacing(10.0);
        topLayout.getChildren().addAll(generateDisk("disk 1"), generateDisk("disk 2"), generateDisk("disk 3"), generateDisk("disk 4"));

        HBox bottomLayout = new HBox();

        Client client1 = new Client("client 1");

        bottomLayout.getChildren().add(client1.render());

        mainLayout.getChildren().addAll(topLayout, bottomLayout);
        Scene scene = new Scene(mainLayout, 800.0, 600.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Disk simulation");
        primaryStage.show();
    }

    public VBox generateDisk(String name) {
        VBox container = new VBox();
        container.alignmentProperty();

        Label diskName = new Label(name);
        diskName.setPrefWidth(100.0);
        diskName.setAlignment(Pos.TOP_CENTER);
        diskName.setPadding(new Insets(0, 0, 10, 0));

        StackPane diskIcon = new StackPane();

        Circle circle = new Circle();
        circle.setRadius(50.0);
        circle.setFill(Color.rgb(38, 59, 203));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2.0);
        circle.setStrokeType(StrokeType.INSIDE);

        Label diskStatus = new Label("client2");
        diskStatus.setTextFill(Color.rgb(147, 200, 203));


        diskIcon.getChildren().addAll(circle, diskStatus);
        container.getChildren().addAll(diskName, diskIcon);
        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
