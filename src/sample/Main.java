package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Thread.sleep;

public class Main extends Application {
    private static final int LICZBA_DYSKOW = 4;
    private final TableView<Client> tableView = new TableView<>();
    private final ObservableList<Client> clients = FXCollections.synchronizedObservableList(
            FXCollections.observableArrayList(
                    new Client(LocalTime.now().minusSeconds(100), 100, 100, 100, 100, 100),
                    new Client(LocalTime.now(), 50, 10000, 10000, 10000, 10000),
                    new Client(LocalTime.now().minusSeconds(3), 10000, 10000, 10000, 10000, 10000),
                    new Client(LocalTime.now(), 1, 1, 1, 1, 1),
                    new Client(LocalTime.now().minusSeconds(10), 100, 1, 1, 1, 1)
            )//oczekiwana kolejnosc: 0, 4, 3, popraw to
    );
    private final List<SimpleStringProperty> uploading = new ArrayList<>();
    private final List<Client> clientsHistory = Collections.synchronizedList(new ArrayList<>());

    public Main() {
        for (int i = 0; i < LICZBA_DYSKOW; i++) {
            uploading.add(new SimpleStringProperty(" - "));
        }
    }

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
//        Button showChart = new Button("show chart");
//        showChart.setOnAction(this::chartWindow);
        Button startSimulation = new Button("Start");
        startSimulation.setOnAction(this::simulationHandler);
        topLayout.setSpacing(10.0);
        for (int i = 0; i < LICZBA_DYSKOW; i++) {
            topLayout.getChildren().add(generateDisk("disk " + i, uploading.get(i)));
        }
        topLayout.getChildren().addAll(addClientButton, startSimulation/*, showChart*/);

        tableView.setEditable(true);

        TableColumn<Client, String> id = new TableColumn<>("Client id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> time = new TableColumn<>("time");
        time.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        TableColumn<Client, String> files = new TableColumn<>("size");
        for (int i = 0; i < Client.MAX_FILES; i++) {
            TableColumn<Client, String> tc = new TableColumn<>("file " + i);
            tc.setCellValueFactory(new PropertyValueFactory<>("file" + i));
            files.getColumns().add(tc);
        }
        TableColumn<Client, String> prior = new TableColumn<>("prior");
        prior.setCellValueFactory(new PropertyValueFactory<>("prior"));

        tableView.setItems(clients);

        tableView.getColumns().addAll(id, time, files, prior);

        mainLayout.getChildren().addAll(topLayout, tableView);
        Scene scene = new Scene(mainLayout, 800.0, 600.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DropBox simulation");
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Zamknięto aplikacje!");
            //here close all threads
        });
        primaryStage.show();
        clientsRefresher();
    }

    private void showClock(ActionEvent actionEvent) {
        HBox main = new HBox();
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        SimpleStringProperty screenText = new SimpleStringProperty(now);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String now = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
                Platform.runLater(() -> {
                    screenText.set(now);
                });
            }
        }, 1000L, 1000L);
        Label screen = new Label();
        screen.textProperty().bindBidirectional(screenText);
        main.getChildren().add(screen);
        Scene scene = new Scene(main, 200, 200);
        Stage stage = new Stage();
        stage.setTitle("Clock");
        stage.setScene(scene);
        stage.show();
    }

    private void clientsRefresher() {
        Timer timer = new Timer("Clients time refresher");
        long delay = 1000L;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                clients.forEach(Client::updateWaitingTime);
                tableView.refresh();
            }
        }, delay, delay);
    }

    private void simulationHandler(ActionEvent actionEvent) {
        Thread simulation = new Thread(() -> {
            for (int i = 0; i < LICZBA_DYSKOW; i++) {
                int finalI = i;
                Thread disk = new Thread(() -> {
                    while (true) {
                        try {
                            Client client = this.getClient();
                            Platform.runLater(() -> {
                                this.uploading.get(finalI).set(Long.toString(client.getId()));
                            });
                            sleep(client.getFile0() + client.getFile1() + client.getFile2() + client.getFile3() + client.getFile4());
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } catch (NoSuchElementException noSuchElementException) {
                            System.out.println("Brak klientów do uploadu");
                            Platform.runLater(() -> {
                                this.uploading.get(finalI).set(" - ");
                            });
                            break;
                        }
                    }
                });
                disk.start();
            }
        });
        simulation.start();
    }

    private synchronized Client getClient() {
        Client client = this.clients
                .stream().peek(c -> {
                    long wainting = c.getWaitingTime();
                    //tutaj liczymy priorytet
                    double timePrior = 1.0 / 6250 * wainting * wainting + 3.0 / 5 * wainting;
                    long sizePrior = 10_000 - c.getFile0();
                    double prior = sizePrior + timePrior;
                    c.setPrior(prior);
                }).max((c1, c2) -> (int) (c2.getPrior() - c1.getPrior()))
                .orElseThrow(NoSuchElementException::new);

        System.out.println("id:\t" + client.getId() + "\ttime:\t" + client.getWaitingTime() +
                "\tsize:\t" + client.getFile0() + "\t\tprior:\t" + client.getPrior());
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

    private void chartWindow(ActionEvent actionEvent) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("milisekundy");
        yAxis.setLabel("priorytet");

        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Priorytet w zależności od rozmiaru pliku oraz czasu oczekiwania klienta");
        XYChart.Series<Number, Number> timeSeries = new XYChart.Series<>();
        timeSeries.setName("czas oczekiwania");
        XYChart.Series<Number, Number> fileSeries = new XYChart.Series<>();
        fileSeries.setName("wielkość pliku");
        ObservableList<XYChart.Data<Number, Number>> timeData = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<Number, Number>> fileData = FXCollections.observableArrayList();
        for (long i = 1L; i < 10_000; i += 100) {
            timeData.add(new XYChart.Data<>(i, 1.0 / 6250 * i * i + 3.0 / 5 * i));
            fileData.add(new XYChart.Data<>(i, 10_000 - i));
        }
        timeSeries.getData().addAll(timeData);
        fileSeries.getData().addAll(fileData);

        Scene scene = new Scene(lineChart, 850, 450);
        lineChart.getData().add(timeSeries);
        lineChart.getData().add(fileSeries);
        Stage stage = new Stage();
        stage.setTitle("Priorytet w zależności od rozmiaru pliku oraz czasu oczekiwania klienta");
        stage.setScene(scene);
        stage.show();
    }
}
