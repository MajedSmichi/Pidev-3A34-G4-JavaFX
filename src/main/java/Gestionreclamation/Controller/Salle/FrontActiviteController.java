package Gestionreclamation.Controller.Salle;

import Gestionreclamation.Controller.SessionManager;
import Gestionreclamation.Entity.Salle.Activite;
import Gestionreclamation.Entity.Salle.Salle;
import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.Salle.ActiviteService;
import Gestionreclamation.Services.Salle.SalleService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FrontActiviteController {

    @FXML
    private GridPane GridActivite;

    @FXML
    private Label coachact;
    @FXML
    private Label coachact1;

    @FXML
    private Label dateact;
    @FXML
    private Label dateact1;

    @FXML
    private Label descriptionact;
    @FXML
    private Label descriptionact1;

    @FXML
    private Pane detail;
    @FXML
    private Pane detail1;

    @FXML
    private ImageView imageact;
    @FXML
    private ImageView imageact1;

    @FXML
    private ScrollPane list;

    @FXML
    private Label nameact;
    @FXML
    private Label nameact1;

    @FXML
    private Label nbrmaxact;
    @FXML
    private Label nbrmaxact1;

    @FXML
    private Label salleact;
    @FXML
    private Label salleact1;
    @FXML
    private TextField search;

    @FXML
    private Button reserver;
    @FXML
    private Button annuler;

    @FXML
    private GridPane GridActivite1;

    @FXML
    private ScrollPane list1;

    @FXML
    private Button aloo;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private RadioButton filterButton;

    @FXML
    void visibilite(ActionEvent event) {
        boolean isVisible = list.isVisible();
        boolean isVisible1 = list1.isVisible();
        list.setVisible(!isVisible);
        list1.setVisible(!isVisible1);
        detail.setVisible(false);
        detail1.setVisible(false);

    }

    User user = SessionManager.getInstance().getCurrentUser();

    private Activite currentActivite;

    public void initialize() {
        try {
            ActiviteService activiteService = new ActiviteService();
            List<Activite> allActivites = getData();
            List<Activite> activitesByUser = activiteService.getActivitesByUserId( Integer.parseInt(user.getId())); // Replace 1 with the actual user ID

            populateGrid(allActivites);
            populateGrid1(activitesByUser);

            // Add a listener to the search TextField
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                List<Activite> filteredActivites = allActivites.stream()
                        .filter(activite -> activite.getNom().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());

                List<Activite> filteredActivitesByUser = activitesByUser.stream()
                        .filter(activite -> activite.getNom().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());

                try {
                    populateGrid(filteredActivites);
                    populateGrid1(filteredActivitesByUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private boolean showAll = true;
    @FXML
    private void filterActivities(ActionEvent event) throws SQLException, IOException {
        ActiviteService activiteService = new ActiviteService();
        List<Activite> allActivites = activiteService.getAllActivite();

        if (showAll) {
            List<Activite> filteredActivites = allActivites.stream()
                    .filter(a -> a.getNbr_max() != 0)
                    .collect(Collectors.toList());

            populateGrid(filteredActivites);
        } else {
            populateGrid(allActivites);
        }

        // Toggle the showAll flag for the next click
        showAll = !showAll;
    }

    private void populateGrid(List<Activite> activites) throws IOException {
        GridActivite.getChildren().clear();
        int row = 0;
        for (Activite activite : activites) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/ActiviteCard.fxml"));
            Pane pane = fxmlLoader.load();

            ActiviteCardController oneActivite = fxmlLoader.getController();
            oneActivite.setData(activite);
            pane.setOnMouseClicked(event -> displayActiviteDetails(activite));

            GridActivite.add(pane, 0, row); // Always add to the first column
            row++;
            GridPane.setMargin(pane, new Insets(10));
        }
    }
    private void populateGrid1(List<Activite> activites) throws IOException {
        GridActivite1.getChildren().clear();
        int row = 0;
        for (Activite activite : activites) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/ReservationCard.fxml"));
            Pane pane = fxmlLoader.load();

            ReservationCardController oneReservation = fxmlLoader.getController();
            oneReservation.setData(activite);
            pane.setOnMouseClicked(event -> displayActiviteDetails1(activite));

            GridActivite1.add(pane, 0, row); // Always add to the first column
            row++;
            GridPane.setMargin(pane, new Insets(10));
        }
    }

    private List<Activite> getData() throws SQLException {
        ActiviteService activiteService = new ActiviteService();
        return activiteService.getAllActivite();
    }
    private void displayActiviteDetails(Activite activite) {
        nameact.setText(activite.getNom());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact.setText(formattedDate);
        descriptionact.setText(activite.getDescription());
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
        salleact.setText(String.valueOf(activite.getSalle_id()));
        SalleService salleService = new SalleService();
        try {
            Salle salle = salleService.getSalleById(activite.getSalle_id());
            if (salle != null) {
                salleact.setText(salle.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            imageact.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }

        // Show the details AnchorPane
        detail.setVisible(true);
        this.currentActivite = activite;
        // Clear the QR code ImageView
        qrCodeImageView.setImage(null);
    }
    @FXML
    void reserverActivite(ActionEvent event) {
        ActiviteService activiteUserService = new ActiviteService();
        try {
            if (currentActivite.getNbr_max() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Désolé, cette activité est complète.");
                alert.showAndWait();
                return;
            }

            int userId = Integer.parseInt(user.getId()); // Replace this with actual method to get logged in user id
            activiteUserService.reserverActivite(currentActivite.getId(), userId);



            int sale = currentActivite.getSalle_id();
            SalleService salleService = new SalleService();
            Salle salle = salleService.getSalleById(sale);
            String get = salle.getNom();

            int code = userId * 33 +17;
            String verfie = "Réservation confirmée";
            String userDetails = "User: " + user.getNom() + " " + user.getPrenom() + ", Email: " + user.getEmail();
            String activiteDetails = "Activite: " + currentActivite.getNom() + ", Date: " + currentActivite.getDate() + ", Salle: " + currentActivite.getSalle_id();
            String reservationInfo = verfie + "\n" + userDetails + "\n" + activiteDetails + "\n" + "Code confédentiel : " + code ;
            String base64Image = showReservationConfirmation(reservationInfo);



            // Send email
            String userEmail = user.getEmail(); // replace with the user's email
            String subject = "Reservation Confirmation";
            String content = "<div style=\"padding: 20px; background-color: #f9f9f9; border-radius: 15px; border: 1px solid #ccc; max-width: 600px; margin: 0 auto;\">" +
                    "<h1 style=\"color: #333; font-size: 24px; text-align: center; margin-bottom: 20px;\">Cher utilisateur,</h1>" +
                    "<h1 style=\"color: #333; font-size: 20px; text-align: center;\">" + user.getNom() + " " + user.getPrenom() + "</h1>" +
                    "<p style=\"color: #555; font-size: 18px;\">Vous avez réservé avec succès l'activité : <strong>" + currentActivite.getNom() + "</strong> au sein de la salle : <strong>" + get + "</strong>.</p>" +
                    "<p style=\"color: #555; font-size: 18px;\">Veuillez sauvegarder le QR Code ci-dessous afin de pouvoir le présenter lors de votre visite pour accéder à nos services.</p>" +
                    "<p style=\"color: #555; font-size: 18px; margin-top: 20px;\">Cordialement,</p>" +
                    "<p style=\"color: #555; font-size: 18px; margin-bottom: 0;\">Sport Hub</p>" +
                    "<img src=\"data:image/png;base64, " + base64Image + "\" style=\"display: block; margin: auto;\" />" +
                    "</div>";

            EmailUtil.sendEmail(userEmail, subject, content);




            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Activité réservée avec succès.");

            List<Activite> activitesByUser = activiteUserService.getActivitesByUserId(userId);
            populateGrid1(activitesByUser);
            alert.showAndWait();


        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Vous avez déjà réservé cette activité.");
            alert.showAndWait();
        }
    }

    private void displayActiviteDetails1(Activite activite) {
        nameact1.setText(activite.getNom());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact1.setText(formattedDate);
        coachact1.setText(activite.getCoach());
        salleact1.setText(String.valueOf(activite.getSalle_id()));
        SalleService salleService = new SalleService();
        try {
            Salle salle = salleService.getSalleById(activite.getSalle_id());
            if (salle != null) {
                salleact1.setText(salle.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            imageact1.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }

        // Show the details AnchorPane
        detail1.setVisible(true);
        this.currentActivite = activite;
    }
    @FXML
    void annuler(ActionEvent event) {
        ActiviteService activiteService = new ActiviteService();
        try {
            int userId = Integer.parseInt(user.getId()); // Replace this with actual method to get logged in user id
            activiteService.deleteActiviteUser(currentActivite.getId(), userId);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Réservation annulée avec succès.");
            alert.showAndWait();

            List<Activite> activitesByUser = activiteService.getActivitesByUserId(userId);
            populateGrid1(activitesByUser);
            detail1.setVisible(false);

            int sale = currentActivite.getSalle_id();
            SalleService salleService = new SalleService();
            Salle salle = salleService.getSalleById(sale);
            String get = salle.getNom();


            // Send email
            String userEmail = user.getEmail(); // replace with the user's email
            String subject = "Reservation Cancellation";
            String content = "<div style=\"padding: 20px; background-color: #f9f9f9; border-radius: 15px; border: 1px solid #ccc; max-width: 600px; margin: 0 auto;\">" +
                    "<h1 style=\"color: #333; font-size: 24px; text-align: center; margin-bottom: 20px;\">Cher utilisateur,</h1>" +
                    "<h1 style=\"color: #333; font-size: 20px; text-align: center;\">" + user.getPrenom() + " " + user.getNom() + "</h1>" +
                    "<p style=\"color: #555; font-size: 18px;\">Votre réservation pour l'activité : <strong>" + currentActivite.getNom() + "</strong> dans la salle : <strong>" + get + "</strong> a été annulée avec succès.</p>" +
                    "<p style=\"color: #555; font-size: 18px;\">Nous espérons avoir le plaisir de vous accueillir à nouveau prochainement pour de nouvelles aventures.</p>" +
                    "<p style=\"color: #555; font-size: 18px; margin-top: 20px;\">Cordialement,</p>" +
                    "<p style=\"color: #555; font-size: 18px; margin-bottom: 0;\">Votre équipe</p>" +
                    "</div>";
            EmailUtil.sendEmail(userEmail, subject, content);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'annulation de la réservation.");
            alert.showAndWait();
        }
    }

    public String showReservationConfirmation(String reservationInfo) {
        try {
            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = qrCodeWriter.encode(reservationInfo, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Save QR code to a ByteArrayOutputStream
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Convert byte array to Base64
            String base64Image = Base64.getEncoder().encodeToString(pngData);

            // Load the QR code into an ImageView
            Image qrCodeImage = new Image(new ByteArrayInputStream(pngData));
            qrCodeImageView.setImage(qrCodeImage);

            return base64Image;
        } catch (WriterException | IOException e) {
            System.out.println("Could not generate QR Code: " + e.getMessage());
            return null;
        }
    }
}