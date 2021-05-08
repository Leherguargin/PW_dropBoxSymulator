package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public VBox disk1vbox;
    public Label disc1status;
    public VBox disc2vbox;
    public Label disc2status;
    public VBox disc3vbox;
    public Label disc3status;
    public VBox disc4vbox;
    public Label disc4status;
    public VBox disc5vbox;
    public Label disc5status;
    public Button testButton;

    private Integer clientNumber = 0;
    private final ArrayList<Client> clients = new ArrayList<>();
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
