import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class Launch extends Application implements  EventHandler<ActionEvent>{
    TextField searchField;
    Button searchButton;
    Text title;
    HBox hbox;
    HBox hbox2;
    VBox vbox;
    TextArea textAreaQuery;
    TextArea textAreaResult1;
    TextArea textAreaResult2;
    TextArea textAreaResult3;
    TextArea textAreaResult4;

    Graph g = new Graph();
    File json = new File("business.json");
    String line;
    int keyCount = 0;

    void setup(){
        try {
            Scanner scanner = new Scanner(json);
            while (scanner.hasNextLine() && keyCount < 10000) {

                line = scanner.nextLine();
                JSONObject obj = new JSONObject(line);
                String name = obj.getString("name");
                double stars = obj.getDouble("stars");
                int reviewCount = obj.getInt("review_count");
                double lat = obj.getDouble("latitude");
                double lon = obj.getDouble("longitude");
                Business b = new Business(keyCount, name, stars, reviewCount, lon, lat);
                keyCount++;
                g.queue.businesses.add(b);

            }
            g.setDisjointSets();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        setup();

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        VBox vbox = addVBox();
        HBox hbox2 = addHBox2();
        border.setTop(hbox);
        border.setCenter(vbox);
        border.setBottom(hbox2);
        hbox2.setAlignment(Pos.TOP_LEFT);



        primaryStage.setTitle("Yelp similarity finder");



        Scene scene = new Scene(border, 1500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    HBox addHBox(){
        hbox = new HBox();
        searchField = new TextField();
        searchField.setPrefColumnCount(30);
        searchButton = new Button("Search");
        searchButton.setOnAction(this);
        hbox.getChildren().addAll(searchField, searchButton);
        return hbox;
    }

    VBox addVBox(){
        vbox = new VBox();
        title = new Text("Search result:");
        textAreaQuery = new TextArea();
        textAreaQuery.setEditable(false);
        Text lowertitle = new Text("Similar:");

        vbox.getChildren().addAll(title, textAreaQuery, lowertitle);
        return vbox;
    }

    HBox addHBox2(){
        hbox2 = new HBox();
        textAreaResult1 = new TextArea();
        textAreaResult2 = new TextArea();
        textAreaResult3 = new TextArea();
        textAreaResult4 = new TextArea();
        textAreaResult1.setEditable(false);
        textAreaResult1.setWrapText(true);
        textAreaResult2.setEditable(false);
        textAreaResult2.setWrapText(true);
        textAreaResult3.setEditable(false);
        textAreaResult3.setWrapText(true);
        textAreaResult4.setEditable(false);
        textAreaResult4.setWrapText(true);
        hbox2.getChildren().addAll(textAreaResult1, textAreaResult2, textAreaResult3, textAreaResult4);
        return hbox2;

    }

    public void handle(ActionEvent event){
        if(event.getSource() == searchButton){
            try{

                Business n = g.queue.businesses.get(Integer.parseInt(searchField.getText()));
                textAreaQuery.setText("Name: " + n.name + "\n" + "Rating: " + n.stars + "\n" + "Review Count: " + n.reviewCount + "\n" + "Disjoint set: " + n.disjointKey + " Out of " + g.disjointSets.size() + " disjoint sets");
                textAreaResult1.setText("Name: " + g.queue.businesses.get(n.neighbors.get(0).key).name +
                        "\n" + "Rating: " + g.queue.businesses.get(n.neighbors.get(0).key).stars +
                        "\n" + "Review Count: " + g.queue.businesses.get(n.neighbors.get(0).key).reviewCount +
                        "\n" + "Disjoint set: " + g.queue.businesses.get(n.neighbors.get(0).key).disjointKey +
                        "\n" + "Distance from search: " + n.neighbors.get(0).distance+ " km");
                textAreaResult2.setText("Name: " + g.queue.businesses.get(n.neighbors.get(1).key).name +
                        "\n" + "Rating: " + g.queue.businesses.get(n.neighbors.get(1).key).stars +
                        "\n" + "Review Count: " + g.queue.businesses.get(n.neighbors.get(1).key).reviewCount +
                        "\n" + "Disjoint set: " + g.queue.businesses.get(n.neighbors.get(1).key).disjointKey +
                        "\n" + "Distance from search: " + n.neighbors.get(1).distance+ " km");
                textAreaResult3.setText("Name: " + g.queue.businesses.get(n.neighbors.get(2).key).name +
                        "\n" + "Rating: " + g.queue.businesses.get(n.neighbors.get(2).key).stars +
                        "\n" + "Review Count: " + g.queue.businesses.get(n.neighbors.get(2).key).reviewCount +
                        "\n" + "Disjoint set: " + g.queue.businesses.get(n.neighbors.get(2).key).disjointKey +
                        "\n" + "Distance from search: " + n.neighbors.get(2).distance+ " km");
                textAreaResult4.setText("Name: " + g.queue.businesses.get(n.neighbors.get(3).key).name +
                        "\n" + "Rating: " + g.queue.businesses.get(n.neighbors.get(3).key).stars +
                        "\n" + "Review Count: " + g.queue.businesses.get(n.neighbors.get(3).key).reviewCount +
                        "\n" + "Disjoint set: " + g.queue.businesses.get(n.neighbors.get(3).key).disjointKey +
                        "\n" + "Distance from search: " + n.neighbors.get(3).distance + " km");


            }catch(Exception e){

            }

        }
    }
}
