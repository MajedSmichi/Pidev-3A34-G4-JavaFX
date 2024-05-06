package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Services.EvenementService;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewController {
    @FXML
    private WebView webView;

    private EvenementService evenementService;

    public CalendarViewController() {
        evenementService = new EvenementService();
    }

    @FXML
    public void initialize() {
        System.out.println("initialize() method is called"); // Print statement

        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/Gestionreclamation/SportHub/Calendar.html").toExternalForm());

        // Make the getEvents method available in JavaScript
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("myEvents", (Runnable) this::getEvents);
    }

public void getEvents() {
    try {
        System.out.println("getEvents() method is called") ;

        // Fetch the event with id 53 from the database
        Evenement event = evenementService.getEventById(53);

        // Create a list and add the event to it
        List<Evenement> events = new ArrayList<>();
        events.add(event);

        // Convert the events into a JSON string
        String eventsJson = evenementService.convertEventsToJson(events);

        // Print the eventsJson string for debugging
        System.out.println("eventsJson: " + eventsJson);

        // Pass the JSON string to FullCalendar
        webView.getEngine().executeScript("updateEvents(" + eventsJson + ")");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}



