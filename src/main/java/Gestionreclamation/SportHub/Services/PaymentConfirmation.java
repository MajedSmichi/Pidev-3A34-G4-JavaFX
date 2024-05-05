package Gestionreclamation.SportHub.Services;

import Gestionreclamation.SportHub.Controller.EvenementFront;
import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import com.google.zxing.WriterException;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.sql.SQLException;

public class PaymentConfirmation {

    public class JavaApp {
        private Stage stage;
        private Evenement event;
        private EvenementFront evenementFront;
        private GridPane eventContainer;

        public JavaApp(Stage stage, Evenement event, EvenementFront evenementFront, GridPane eventContainer) {
            this.stage = stage;
            this.event = event;
            this.evenementFront = evenementFront;
            this.eventContainer = eventContainer;
        }

        public void print(String message) {
            if (message.equals("Payment succeeded!")) {
                Platform.runLater(() -> {
                    stage.close();
                    TicketService ticketService = new TicketService();
                    try {
                        Ticket ticket = ticketService.getTicketByEventId(event.getId());
                        if (ticket != null && ticket.getNbreTicket() > 0) {
                            evenementFront.decreaseTicketQuantity(ticket);
                            // Clear the event container
                            eventContainer.getChildren().clear();
                            // Create a success message label
                            Label successLabel = new Label("Payment Successful!");
                            successLabel.getStyleClass().add("success-label"); // Add a CSS class for styling
                            // Add the success message label to the event container
                            eventContainer.add(successLabel, 0, 0);

                            // Generate a QR code with the details of the transaction and the ticket
                            QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                            String qrCodeText = "Félicitations, votre achat a été réussi\n"  +
                                    "Votre Ticket\n"  +
                                    "Event Name: " + event.getNom() +
                                    "Ticket Type: " + ticket.getType() +
                                    "Ticket Price: " + ticket.getPrix() ;
                            Image qrCodeImage = qrCodeGenerator.generateQRCode(qrCodeText, 200, 200);
                            ImageView qrCodeImageView = new ImageView(qrCodeImage);
                            // Add the QR code to the event container
                            eventContainer.add(qrCodeImageView, 0, 1);
                        }
                    } catch (SQLException | WriterException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

    }

    public void confirmPayment(String clientSecret, Evenement event, EvenementFront evenementFront, GridPane eventContainer) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true); // Enable JavaScript
        webView.setPrefHeight(200);
        webView.setPrefWidth(700);
        String html = "<html>\n" +
            "<head>\n" +
            "<style>\n" +
            "body {\n" +
            "  font-family: Arial, sans-serif;\n" +
            "}\n" +
            "#payment-form {\n" +
            "  width: 300px;\n" +
            "  margin: 0 auto;\n" +
            "}\n" +
            "#card-element {\n" +
            "  border: 1px solid #ccc;\n" +
            "  padding: 10px;\n" +
            "  border-radius: 4px;\n" +
            "}\n" +
            "button {\n" +
            "  background-color: #4CAF50;\n" +
            "  color: white;\n" +
            "  padding: 15px 32px;\n" +
            "  text-align: center;\n" +
            "  text-decoration: none;\n" +
            "  display: inline-block;\n" +
            "  font-size: 16px;\n" +
            "  margin: 4px 2px;\n" +
            "  cursor: pointer;\n" +
            "  border: none;\n" +
            "}\n" +
            "button:hover {\n" +
            "  background-color: #45a049;\n" +
            "}\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script src=\"https://js.stripe.com/v3/\"></script>\n" +
            "<form id='payment-form'>\n" +
            "  <div class='form-row'>\n" +
            "    <label for='card-element'>\n" +
            "      Credit or debit card\n" +
            "    </label>\n" +
            "    <div id='card-element'>\n" +
            "      <!-- A Stripe Element will be inserted here. -->\n" +
            "    </div>\n" +
            "    <!-- Used to display form errors. -->\n" +
            "    <div id='card-errors' role='alert'></div>\n" +
            "  </div>\n" +
            "  <button>Submit Payment</button>\n" +
            "</form>\n" +
            "<script>\n" +
            "var stripe = Stripe('pk_test_51OqH27C0U7VRZI3sCGMMa2P91ySR9BRMT9MmTx4TiEpwUFrXynEEqZTmwHIxgZ0ZfJoLyb3PTtUdqfuY7zqGj5sG00U8wODUiR');\n" +
            "var elements = stripe.elements();\n" +
            "var card = elements.create('card');\n" +
            "card.mount('#card-element');\n" +
            "var form = document.getElementById('payment-form');\n" +
            "form.addEventListener('submit', function(event) {\n" +
            "  event.preventDefault();\n" +
            "  stripe.createPaymentMethod({\n" +
            "    type: 'card',\n" +
            "    card: card\n" +
            "  }).then(function(result) {\n" +
            "    if (result.error) {\n" +
            "      var errorElement = document.getElementById('card-errors');\n" +
            "      errorElement.textContent = result.error.message;\n" +
            "    } else {\n" +
            "      stripe.confirmCardPayment('" + clientSecret + "', {\n" +
            "        payment_method: result.paymentMethod.id\n" +
            "      }).then(function(confirmResult) {\n" +
            "        if (confirmResult.error) {\n" +
            "          var errorElement = document.getElementById('card-errors');\n" +
            "          errorElement.textContent = confirmResult.error.message;\n" +
            "        } else {\n" +
            "          if (confirmResult.paymentIntent.status === 'succeeded') {\n" +
            "            console.log('Payment succeeded!');\n" +
            "          }\n" +
            "        }\n" +
            "      });\n" +
            "    }\n" +
            "  });\n" +
            "});\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";

        webEngine.loadContent(html);

        // Create a new Stage to display the WebView
        Stage paymentStage = new Stage();
        paymentStage.setScene(new Scene(webView));
        paymentStage.show();

        // Add a change listener to the location property of the webEngine
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("app", new JavaApp(paymentStage, event, evenementFront, eventContainer));
                webEngine.executeScript("console.log = function(message){ app.print(message); };");
            }
        });
    }
}