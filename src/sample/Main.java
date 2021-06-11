package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class Main extends Application {

    private TableView tableView = new TableView();
    private final ObservableList<Client> data = FXCollections.observableArrayList();
    int clientsNumber = 0;
    private long id = 1;
    String[] uploading = {" - ", " - ", " - ", " - "};
    Boolean[] available = {true, true, true, true};

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(15.0, 12.0, 15.0, 12.0));
        mainLayout.setSpacing(10.0);
        mainLayout.setStyle("-fx-background-color: #7d8ea3;");

        HBox topLayout = new HBox();
        Button addClient = new Button("Add client");
        addClient.setOnAction((e) -> {
            Client client = new Client("c" + ++clientsNumber, id++);
            data.add(client);
        });
        topLayout.setSpacing(10.0);
        topLayout.getChildren().addAll(
                generateDisk("disk 0", uploading[0]),
                generateDisk("disk 1", uploading[1]),
                generateDisk("disk 2", uploading[2]),
                generateDisk("disk 3", uploading[3]),
                addClient
        );

        tableView.setEditable(true);

        TableColumn name = new TableColumn("Client name");
        name.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        TableColumn id = new TableColumn("Client id");
        id.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        TableColumn time = new TableColumn("time");
        time.setCellValueFactory(new PropertyValueFactory<Client, String>("time"));

        TableColumn files = new TableColumn("size");
        for (int i = 0; i < Client.MAX_FILES; i++) {
            TableColumn tc = new TableColumn("file " + i);
            tc.setCellValueFactory(new PropertyValueFactory<Client, String>("size" + i));
            files.getColumns().add(tc);
        }

        tableView.setItems(data);

        tableView.getColumns().addAll(id, name, time, files);

        mainLayout.getChildren().addAll(topLayout, tableView);
        Scene scene = new Scene(mainLayout, 800.0, 600.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Disk simulation");
        primaryStage.show();
        Thread disksWorker = new Thread(() -> {
            while (true) {
                for (int j = 0; j < 4; j++) {
                    if (available[j] && !data.isEmpty()) {
                        //licz priorytet oraz uploaduj klienta z największym
                        System.out.println("start dla dysku: " + j);
                        long maxPrior = 0;
                        Client clientWithMaxPrior = null;
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).uploaded)
                                continue;
                            Client client = data.get(i);
                            if (!client.uploaded) {
                                client.prior = 1 / client.getWaitingTime() + clientsNumber + client.getSizes();
                                if (client.prior > maxPrior) {
                                    maxPrior = client.prior;
                                    clientWithMaxPrior = client;
                                }
                            }
                        }
                        //uploaduj plik z najlepszym priorytetem
                        if (clientWithMaxPrior == null)
                            continue;
                        System.out.println(clientWithMaxPrior.getName());
                        clientWithMaxPrior.uploaded = true;
                        available[j] = false;
                        uploading[j] = clientWithMaxPrior.getName();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        disksWorker.start();
    }

    public VBox generateDisk(String name, String labelText) {
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

        Label diskStatus = new Label(labelText);
        diskStatus.setTextFill(Color.rgb(147, 200, 203));


        diskIcon.getChildren().addAll(circle, diskStatus);
        container.getChildren().addAll(diskName, diskIcon);
        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
