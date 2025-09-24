package pt.pa.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import pt.pa.model.Laptop;
import pt.pa.model.Review;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amfs
 */
public class LaptopsGui extends BorderPane {

    private static final String DATA_PATH = "src/main/resources/laptop_reviews.json";

    private static final String BANNER_PATH = "src/main/resources/header_banner.png";

    List<Laptop> laptops;

    ListView<Laptop> listViewLaptops;

    private VBox mainContent;


    public LaptopsGui() throws Exception {
        try {
            this.laptops = loadData();
            initializeComponents();
        } catch (FileNotFoundException e) {
            throw new Exception("Unable to initialize Laptops GUI");
        }
    }

    public void initializeComponents() throws FileNotFoundException {
     //TODO
        listViewLaptops = new ListView<>();
        listViewLaptops.setCellFactory(param -> new LaptopListCell());

        // Create the header area using an HBox
        HBox header = new HBox();
        header.setPrefHeight(200);

        // Create the small image on the left
        ImageView imageView = loadThumbnailImage();

        // Add the image and label to the header
        header.getChildren().addAll(imageView);

        // Manipulate the list view
        listViewLaptops.setMaxWidth(200);
        listViewLaptops.getItems().addAll(laptops);

        // Create the main content area using a StackPane
        mainContent = new VBox(5);

        mainContent.setStyle("-fx-background-color: white; -fx-padding: 10px");

        // Set the header, menu, and main content in the BorderPane
        setTop(header);


        setLeft(listViewLaptops);


        setCenter(mainContent);

        Text h1Text = new Text();
        Text text = new Text();
        Text review = new Text();

        h1Text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        text.setWrappingWidth(550);
        review.setWrappingWidth(550);

        mainContent.getChildren().addAll(h1Text, text, review);


        //Add select item function
        listViewLaptops.setOnMouseClicked(event -> {
            Laptop selectedLaptop = listViewLaptops.getSelectionModel().getSelectedItem();

            String displayName = selectedLaptop.getDisplayName();
            String releaseDate = selectedLaptop.getReleaseDate();
            String cpu = selectedLaptop.getCpu();
            String ram = selectedLaptop.getRam();
            String ssd = selectedLaptop.getSsd();


            h1Text.setText("Laptop Information");

            text.setText("Display Name: " + displayName +
                    "  Release Date: " + releaseDate +
                    "  Cpu: " + cpu +
                    "  Ram: " + ram +
                    "  Ssd: " + ssd);

            review.setText("Reviews: ");

            StringBuilder reviewString = new StringBuilder();

            reviewString.append("Reviews: ").append("\n");
            for(Review r :  selectedLaptop.getReviews()) {
                reviewString.append("\n").append("User: ").append(r.getUserName()).append("     Rating: ").append(r.getRating()).append("\n").append("Comment: ").append(r.getComment()).append("\n\n");
            }

            review.setText(reviewString.toString());
        });


    }

    /**
     * Load the data  contain on json file specified on DATA_PATH.
     * @return list of Lapstop contained on the file
     * @throws FileNotFoundException in case of the file not exists
     */
    private List<Laptop> loadData() throws FileNotFoundException {

        Gson gson = new Gson();

        Type arrayListType = new TypeToken<ArrayList<Laptop>>() {
        }.getType();

        return gson.fromJson(new FileReader(DATA_PATH), arrayListType);
    }

    /**
     * create an Image View from the image file specified on BANNER_PATH
     * @return the Image View created from the file specified on BANNER_PATH
     * @throws FileNotFoundException in case the file not exists
     */
    ImageView loadThumbnailImage() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(BANNER_PATH);
        Image image = new Image(inputStream);
        return new ImageView(image);
    }


}
