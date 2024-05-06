package Gestionreclamation.Controller.Salle;

import Gestionreclamation.Entity.Salle.Activite;
import Gestionreclamation.Entity.Salle.Salle;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class detailFrontActiviteController {

    @FXML
    private Label coachact;

    @FXML
    private Label dateact;

    @FXML
    private Label descriptionact;

    @FXML
    private Pane detail;

    @FXML
    private ImageView imageact;

    @FXML
    private Label nameact;

    @FXML
    private Label nbrmaxact;

    @FXML
    private Label salleact;

    @FXML
    private Button reserver;

    @FXML
    private ImageView qrCodeImageView;


    public void setData(Activite activite) {
        this.currentActivite = activite;
        nameact.setText(activite.getNom());
        descriptionact.setText(activite.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact.setText(formattedDate);
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
        salleact.setText(String.valueOf(activite.getSalle_id()));

        SalleService salleService = new SalleService();
        try {
            imageact.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }
        try {
            Salle salle = salleService.getSalleById(activite.getSalle_id());
            if (salle != null) {
                salleact.setText(salle.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Activite currentActivite;
    @FXML
    void reserverActivite(ActionEvent event) {
        ActiviteService activiteUserService = new ActiviteService();
        try {
            if (currentActivite.getNbr_max() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Désolé, cette activité est complète.");
                alert.showAndWait();
                return;
            }
            int userId = 1; // Replace this with actual method to get logged in user id
            activiteUserService.reserverActivite(currentActivite.getId(), userId);

            // Send email
            String userEmail = "belhouchet.koussay@esprit.tn"; // replace with the user's email
            String subject = "Reservation Confirmation";
            String content = "<div style='padding: 10px; background-color: grey; color: black; border: none; border-radius: 15px;'>" +
                    "<h1 style='color: black;'>Cher utilisateur,</h1>" +
                    "<p style='color: black;'>Vous avez réservé avec succès l'activité : " + currentActivite.getNom() + "</p>" +
                    "<p style='color: black;'>Cordialement,</p>" +
                    "<p style='color: black;'>Votre équipe</p>" +
                    "</div>";
            EmailUtil.sendEmail(userEmail, subject, content);


            int code = userId * 33 +17;
            String reservationInfo = "Code confédentiel : " + code;// Replace with actual reservation ID
            showReservationConfirmation(reservationInfo);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Activité réservée avec succès.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Vous avez déjà réservé cette activité.");
            alert.showAndWait();
        }
    }

    public void showReservationConfirmation(String reservationInfo) {
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

            // Load the QR code into an ImageView
            Image qrCodeImage = new Image(new ByteArrayInputStream(pngData));
            qrCodeImageView.setImage(qrCodeImage);
        } catch (WriterException | IOException e) {
            System.out.println("Could not generate QR Code: " + e.getMessage());
        }
    }

}
