package com.seekerscloud.pos.controller;

import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class StatisticsFormController {
    public AnchorPane context;
    public LineChart<String, Double> chart;

    public void initialize(){
        loadData();
    }

    private void loadData() {


        if (!Database.orderTable.isEmpty()){
            XYChart.Series<String,Double> series = new XYChart.Series<>();
            ObservableList<XYChart.Series<String,Double>> obList
                    = FXCollections.observableArrayList();


            for (Order temp: Database.orderTable
                 ) {

                series.getData().add(new XYChart.Data<>(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .format(temp.getPlacedDate()),
                        temp.getTotal()
                ));

            }
            obList.add(series);
            chart.setData(obList);

        }
        /*XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("2022-10-10",11500));
        series.getData().add(new XYChart.Data<>("2022-10-11",45000));
        series.getData().add(new XYChart.Data<>("2022-10-12",12000));
        series.getData().add(new XYChart.Data<>("2022-10-13",85000));
        series.getData().add(new XYChart.Data<>("2022-10-14",65000));
        series.getData().add(new XYChart.Data<>("2022-10-15",75000));
        series.getData().add(new XYChart.Data<>("2022-10-16",18000));
        series.getData().add(new XYChart.Data<>("2022-10-17",90000));
        series.getData().add(new XYChart.Data<>("2022-10-18",25000));
        series.getData().add(new XYChart.Data<>("2022-10-19",35000));
        series.getData().add(new XYChart.Data<>("2022-10-20",12500));
        series.setName("Income Report");
        chart.getData().add(series);*/
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashBoardForm", "Dashboard");
    }
    private void setUi(String location,String title) throws IOException {
        Stage window = (Stage)context.getScene().getWindow();
        window.setTitle(title);
        window.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml")))
        );
    }
}
