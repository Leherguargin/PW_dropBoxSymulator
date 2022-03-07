package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Thread.sleep;

public class Main extends Application {

    private final TableView<Client> tableView = new TableView<>();
    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private final SimpleStringProperty[] uploading = {
            new SimpleStringProperty(" - "),
            new SimpleStringProperty(" - "),
            new SimpleStringProperty(" - "),
            new SimpleStringProperty(" - ")
    };
    private List<Client> clientsHistory = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(15.0, 12.0, 15.0, 12.0));
        mainLayout.setSpacing(10.0);
        mainLayout.setStyle("-fx-background-color: #7d8ea3;");

        HBox topLayout = new HBox();
        Button addClientButton = new Button("Add client");
        addClientButton.setOnAction((e) -> {
            Client client = new Client();
            clients.add(client);
        });
        Button startSimulation = new Button("Start");
        startSimulation.setOnAction((e) -> {
            Thread simulation = new Thread(() -> {
                for (int i = 0; i < 4; i++) {
                    int finalI = i;
                    Thread disk = new Thread(() -> {
                        while (true) {
                            try {
                                Client client = this.getClient();
                                Platform.runLater(() -> {
                                    this.uploading[finalI].set(Long.toString(client.getId()));
                                });
                                sleep(client.getFile0());//TODO dodaj resztę plików
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            } catch (NoSuchElementException noSuchElementException) {
                                System.out.println("Brak klientów do uploadu");
                                Platform.runLater(() -> {
                                    this.uploading[finalI].set(" - ");
                                });
                                break;
                            }
                        }
                    });
                    disk.start();
                }
            });
            simulation.start();
        });
        topLayout.setSpacing(10.0);
        topLayout.getChildren().addAll(
                generateDisk("disk 0", uploading[0]),
                generateDisk("disk 1", uploading[1]),
                generateDisk("disk 2", uploading[2]),
                generateDisk("disk 3", uploading[3]),
                addClientButton, startSimulation
        );

        tableView.setEditable(true);

        TableColumn<Client, String> id = new TableColumn<>("Client id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> time = new TableColumn<>("time");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
//        TableColumn<Client, String> prior = new TableColumn<>("prior");
//        prior.setCellValueFactory(new PropertyValueFactory<>("prior"));

        TableColumn<Client, String> files = new TableColumn<>("size");
        for (int i = 0; i < Client.MAX_FILES; i++) {
            TableColumn<Client, String> tc = new TableColumn<>("file " + i);
            tc.setCellValueFactory(new PropertyValueFactory<>("file" + i));
            files.getColumns().add(tc);
        }

        tableView.setItems(clients);

        tableView.getColumns().addAll(id, time, files);

        mainLayout.getChildren().addAll(topLayout, tableView);
        Scene scene = new Scene(mainLayout, 800.0, 600.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DropBox simulation");
        primaryStage.show();
    }

    private synchronized Client getClient() {
        Client client = this.clients
                .stream().max((c1, c2) -> {
                    //tutaj liczymy priorytet
                    int c1prior = c1.getFile0();
                    int c2prior = c2.getFile0();
                    long now = new GregorianCalendar().getTime().getTime();
                    System.out.printf("%d %d %d %d\n", c1prior, c2prior, now - c1.getTime(), now - c2.getTime());
                    return c2prior - c1prior;
                })
                .orElseThrow(NoSuchElementException::new);

        this.clientsHistory.add(client);
        this.clients.remove(client);
        return client;
    }

    public VBox generateDisk(String name, SimpleStringProperty labelText) {
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

        Label diskStatus = new Label();
        diskStatus.textProperty().bindBidirectional(labelText);
        diskStatus.setTextFill(Color.rgb(147, 200, 203));


        diskIcon.getChildren().addAll(circle, diskStatus);
        container.getChildren().addAll(diskName, diskIcon);
        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
