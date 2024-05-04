package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Services.ReclamationService;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

import java.io.File;

import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;

public class AfficherReclamtion {
    private VBox vbox;
    private HBox paginationBox; // New HBox for the pagination buttons

    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 1;
    private List<Reclamation> reclamations;
    private ReclamationService reclamationService; // Define reclamationService as an instance variable

    public AfficherReclamtion() {
        this.reclamationService = new ReclamationService(); // Assign to the instance variable
        reclamations = reclamationService.afficherreclamation();

        vbox = new VBox();

        // Initialize the paginationBox
        paginationBox = new HBox();
        paginationBox.setAlignment(Pos.CENTER); // Center the buttons
        paginationBox.setSpacing(10); // Add some spacing between the buttons

        // Create pagination buttons
        createPaginationButtons();
    }

    public void createCards() {
        // Initialize vbox
        vbox.getChildren().clear();
        vbox.setSpacing(10); // Set spacing between the cards
        // Add a title to the vbox
        Label titleLabel = new Label("Reclamations:                                                             Reponses:");
        titleLabel.getStyleClass().add("title"); // Apply the CSS class
        vbox.getChildren().add(titleLabel);

        // Assume this is your list of user claims
        List<Reclamation> pageReclamations = getPageReclamations(currentPage, ITEMS_PER_PAGE);

        for (Reclamation reclamation : pageReclamations) { // Iterate over pageReclamations instead of reclamations
            // Create a VBox for the card
            VBox card = new VBox();
            card.getStyleClass().add("card"); // Apply the CSS class
            card.setPrefSize(450, 200); // Replace with your desired width and height

            // Add claim details to the card
            Label sujetLabel = new Label("Sujet: " + reclamation.getSujet());
            sujetLabel.getStyleClass().add("label"); // Apply the CSS class
            sujetLabel.getStyleClass().add("card-label"); // Add a new CSS class for card labels

            Label descriptionLabel = new Label("Description: " + reclamation.getDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.getStyleClass().add("label"); // Apply the CSS class
            descriptionLabel.getStyleClass().add("card-label"); // Add a new CSS class for card labels

            Label dateLabel = new Label("Date: " + reclamation.getDate().toString());
            dateLabel.getStyleClass().add("label"); // Apply the CSS class
            dateLabel.getStyleClass().add("card-label"); // Add a new CSS class for card labels

            card.getChildren().addAll(sujetLabel, descriptionLabel, dateLabel);

            // Create a HBox to align the image and the card horizontally
            HBox hbox = new HBox(card);
            hbox.setSpacing(10); // Set spacing between the image and the card

            if ("Traité".equals(reclamation.getEtat())) {
                // Get the corresponding response
                Reponse reponse = reclamationService.getReponseByReclamationId(reclamation.getId());

                // Check if the reponse is not null
                if (reponse != null) {
                    System.out.println("Response is not null"); // Debugging print statement

                    // Print the response text
                    System.out.println("Response text: " + reponse.getReponse()); // Debugging print statement

                    // Create a new VBox for the response card
                    VBox responseCard = new VBox();
                    responseCard.getStyleClass().add("card"); // Apply the CSS class
                    responseCard.setPrefSize(400, 200); // Replace with your desired width and height

                    // Create a new Label for the response
                    Label reponses = new Label("Reponse: ");
                    reponses.getStyleClass().add("label"); // Apply the CSS class
                    Label reponseLabel = new Label( reponse.getReponse());
                    reponseLabel.setWrapText(true);
                    reponseLabel.getStyleClass().add("label"); // Apply the CSS class

                    // Add the response Label to the response card
                    responseCard.getChildren().addAll( reponses,reponseLabel);

                    // Add the response card to the HBox
                    hbox.getChildren().add(responseCard);
                } else {
                    System.out.println("Response is null"); // Debugging print statement
                }
            }
            else
            {
                // Create a StackPane for the image and set a background color
                StackPane imageContainer = new StackPane();
                CornerRadii cornerRadii = new CornerRadii(20); // Replace 20 with your desired radius
                BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, cornerRadii, Insets.EMPTY);
                imageContainer.setBackground(new Background(backgroundFill));

                // Create an ImageView for the image
                ImageView imageView = new ImageView();
                imageView.setFitWidth(400); // Replace with your desired width
                imageView.setFitHeight(200); // Replace with your desired height

                // Set the image path
                String imagePath = "/Gestionreclamation/image/enAttent.jpg"; // Replace with the path to your waiting image

                // Check if the image file exists
                URL url = getClass().getResource(imagePath);
                if (url != null) {
                    imageView.setImage(new Image(url.toString()));
                    System.out.println("Image loaded: " + imagePath);
                } else {
                    System.out.println("Image file not found: " + imagePath);
                }

                // Create a clip to match the StackPane's rounded corners
                Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                clip.setArcWidth(cornerRadii.getTopLeftHorizontalRadius() * 2);
                clip.setArcHeight(cornerRadii.getTopLeftVerticalRadius() * 2);
                imageView.setClip(clip);

                // Add the ImageView to the imageContainer
                imageContainer.getChildren().add(imageView);

                // Add the imageContainer to the hbox
                hbox.getChildren().add(imageContainer);
            }

            // Add the hbox to the VBox
            vbox.getChildren().add(hbox);

        }
        createPaginationButtons();

    }

    public void createPaginationButtons() {
        // Clear the paginationBox
        paginationBox.getChildren().clear();

        // Calculate the total number of pages
        int totalPages = (int) Math.ceil((double) reclamations.size() / ITEMS_PER_PAGE);

        // Create a "First Page" button
        Button firstPageButton = new Button("First Page");
        firstPageButton.getStyleClass().add("pretty-button");
        firstPageButton.setOnAction(event -> {
            currentPage = 1;
            createCards();
        });
        paginationBox.getChildren().add(firstPageButton);

        // Create a button for each page number
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        if (startPage > 1) {
            Text ellipsis = new Text("...");
            paginationBox.getChildren().add(ellipsis);
        }

        for (int i = startPage; i <= endPage; i++) {
            int pageNumber = i; // Need to create a final variable to use in the lambda expression
            Button pageButton = new Button(Integer.toString(pageNumber));
            pageButton.getStyleClass().clear();
            pageButton.getStyleClass().add("pretty-button");
            if (pageNumber == currentPage) {
                pageButton.getStyleClass().add("selected-page-button");
            }
            pageButton.setOnAction(event -> {
                currentPage = pageNumber;
                createCards();
            });
            paginationBox.getChildren().add(pageButton);
        }

        if (endPage < totalPages) {
            Text ellipsis = new Text("...");
            paginationBox.getChildren().add(ellipsis);
        }

        // Create a "Last Page" button
        Button lastPageButton = new Button("Last Page");
        lastPageButton.getStyleClass().add("pretty-button");
        lastPageButton.setOnAction(event -> {
            currentPage = totalPages;
            createCards();
        });
        paginationBox.getChildren().add(lastPageButton);
        vbox.getChildren().add(paginationBox);

    }

    public List<Reclamation> getPageReclamations(int page, int itemsPerPage) {
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, reclamations.size());
        return reclamations.subList(start, end);
    }


    public VBox getVbox() {

        return vbox;
    }
}