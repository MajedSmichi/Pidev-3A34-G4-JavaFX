package SportHub.Services;
import SportHub.Controller.EvenementFront;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;


public class PaymentConfirmation {

    public class JavaApp {
        private Stage stage;

        public JavaApp(Stage stage) {
            this.stage = stage;
        }

        public void print(String message) {
            if (message.equals("Payment succeeded!")) {
                Platform.runLater(() -> stage.close());
            }
        }
    }

    public void confirmPayment(String clientSecret, EvenementFront evenementFront) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true); // Enable JavaScript
        String html = "<html>\n" +
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
                window.setMember("app", new JavaApp(paymentStage));
                webEngine.executeScript("console.log = function(message){ app.print(message); };");
            }
        });


    }
}