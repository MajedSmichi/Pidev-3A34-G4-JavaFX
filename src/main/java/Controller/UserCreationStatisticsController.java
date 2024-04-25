package Controller;

import Entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class UserCreationStatisticsController {

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        java.time.Month[] months = java.time.Month.values();
        for (java.time.Month month : months) {
            monthNames.add(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        xAxis.setCategories(monthNames);
        yAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(20);
        // Hide the legend
        barChart.setLegendVisible(false);

        // Set labels for the axes
        xAxis.setLabel("Month");
        yAxis.setLabel("Number of Users");
    }

    public void setUserCreationData(List<User> users) {
        // Create an array to hold the count of users created in each month
        int[] userCountPerMonth = new int[12];

        // Iterate over the users
        for (User user : users) {
            // Get the month the user was created (subtract 1 because array indices start at 0)
            int month = user.getCreatedAt().getMonthValue() - 1;

            // Increment the count for this month
            userCountPerMonth[month]++;
        }

        // Create a new series for the chart
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // Iterate over the months
        for (int i = 0; i < userCountPerMonth.length; i++) {
            // Create a data point for this month
            XYChart.Data<String, Integer> data = new XYChart.Data<>(monthNames.get(i), userCountPerMonth[i]);

            // Add the data point to the series
            series.getData().add(data);
        }

        // Add the series to the chart
        javafx.application.Platform.runLater(() -> {
            barChart.getData().add(series);

            // Define an array of colors
            String[] colors = {"red", "orange", "yellow", "green", "blue", "indigo", "violet", "purple", "pink", "brown", "gray", "black"};

            // Apply a different color to each bar
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Integer> data = series.getData().get(i);
                Node node = data.getNode();
                node.setStyle("-fx-bar-fill: " + colors[i] + ";");
            }
        });
    }
}