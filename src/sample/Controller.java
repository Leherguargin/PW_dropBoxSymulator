package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Integer clientNumber = 0;
    private ArrayList<Client> clients = new ArrayList<>();
    public HBox clientsContainer;

    public void addNewClient(ActionEvent actionEvent) {
        this.clientNumber++;
        Client client = new Client("client" + this.clientNumber);
        this.clients.add(client);
        clientsContainer.getChildren().add(client.render());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addNewClient(new ActionEvent());
        addNewClient(new ActionEvent());
        addNewClient(new ActionEvent());
    }
}
