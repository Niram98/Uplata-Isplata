/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isplata;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Isplata extends Application {
    
    double racun = 0.0;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        ArrayList<Transakcija> up = new ArrayList<>(); // Листа уплата
        ArrayList<Transakcija> is = new ArrayList<>(); // Листа исплата
        
        RadioButton rb1 = new RadioButton("Uplata");
        RadioButton rb2 = new RadioButton("Isplata");
        ToggleGroup tg = new ToggleGroup();
        rb1.setToggleGroup(tg);
        rb2.setToggleGroup(tg);
        HBox hb1 = new HBox();
        hb1.setAlignment(Pos.CENTER);
        hb1.setSpacing(10);
        hb1.getChildren().addAll(rb1, rb2);
        
        Label suma = new Label("Suma:");
        TextField tf1 = new TextField();
        HBox hb2 = new HBox();
        hb2.setAlignment(Pos.CENTER);
        hb2.setSpacing(10);
        hb2.getChildren().addAll(suma, tf1);
        
        Label dan = new Label("Dan:");
        TextField tf2 = new TextField();
        tf2.setPrefWidth(60);
        Label mesec = new Label("Mesec:");
        TextField tf3 = new TextField();
        tf3.setPrefWidth(60);
        Label godina = new Label("Godina:");
        TextField tf4 = new TextField();
        tf4.setPrefWidth(60);
        HBox hb3 = new HBox();
        hb3.setAlignment(Pos.CENTER);
        hb3.setSpacing(10);
        hb3.getChildren().addAll(dan, tf2, mesec, tf3, godina, tf4);
        
        VBox vb1 = new VBox();
        vb1.setPadding(new Insets(10, 10, 10, 10));
        vb1.setSpacing(20);
        vb1.setAlignment(Pos.CENTER);
        vb1.setStyle("-fx-border-style: solid; -fx-border-color: red;");
        vb1.getChildren().addAll(hb1, hb2, hb3);
        
        Label stanje = new Label("Stanje na racunu:");
        TextField tf5 = new TextField(racun + "");
        tf5.setDisable(true);
        HBox hb4 = new HBox();
        hb4.setAlignment(Pos.CENTER);
        hb4.setSpacing(10);
        hb4.getChildren().addAll(stanje, tf5);
        
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        Button izvrsi = new Button("Izvrsi transakciju");
        Text poruka = new Text();
        poruka.setFill(Color.RED);
        Button prikazi = new Button("Prikazi transakcije");
        gp.add(izvrsi, 0, 0);
        gp.add(poruka, 0, 1);
        gp.add(prikazi, 0, 2);
        gp.setHalignment(izvrsi, HPos.CENTER);
        gp.setHalignment(poruka, HPos.CENTER);
        gp.setHalignment(prikazi, HPos.CENTER);
        
        VBox uplate = new VBox();
        uplate.setPrefHeight(300);
        uplate.setPadding(new Insets(10, 10, 10, 10));
        uplate.setSpacing(20);
        uplate.setAlignment(Pos.CENTER_LEFT);
        uplate.setPrefWidth(225);
        uplate.setStyle("-fx-border-style: solid; -fx-border-color: red;");
        
        VBox isplate = new VBox();
        isplate.setPrefHeight(300);
        isplate.setPadding(new Insets(10, 10, 10, 10));
        isplate.setSpacing(20);
        isplate.setAlignment(Pos.CENTER_LEFT);
        isplate.setPrefWidth(225);
        isplate.setStyle("-fx-border-style: solid; -fx-border-color: red;");
        
        HBox hb5 = new HBox();
        hb5.setSpacing(10);
        hb5.getChildren().addAll(uplate, isplate);

        /* Клик на дугме Izvrsi transakciju:
           Проверавају се вредности које корисник задаје – датум трансакције, сума и тип трансакције и на основу тога
           креира објекат који представља трансакцију. Ако трансакција не може да се изврши, исписује се у лабели
           одговарајућа порука. Таква трансакција се не сматра валидном, па неће бити ни додата у колекцију. Ако је
           трансакција успешно извршена, додаје се у колекцију и ажурира се вредност која је исписана у пољу за приказ
           текућег стања. */

        izvrsi.setOnAction(event -> {

            // Провера унесених података
            TextField[] polja = {tf1, tf2, tf3, tf4};
            for(int i = 0; i < polja.length; i++) {
                if(!polja[i].getText().matches("\\d*[1-9]\\d*"))
                    errorDialog(polja[i].getText());
            }
            proveraDatuma(tf2.getText(), tf3.getText(), tf4.getText());


            // Уплата
            if(rb1.isSelected()) {
                up.add(new Transakcija(Double.parseDouble(tf1.getText()), Integer.parseInt(tf2.getText()), Integer.parseInt(tf3.getText()), Integer.parseInt(tf4.getText())));
                dodaj(Double.parseDouble(tf1.getText()));
                poruka.setText("");
            }

            // Исплата
            if(rb2.isSelected()) {
                is.add(new Transakcija(Double.parseDouble(tf1.getText()), Integer.parseInt(tf2.getText()), Integer.parseInt(tf3.getText()), Integer.parseInt(tf4.getText())));
                poruka.setText("");
                if(racun >= Double.parseDouble(tf1.getText()))
                    povuci(Double.parseDouble(tf1.getText()));
                else
                    poruka.setText("Nema dovoljno sredstava za isplatu"); 
            }

            tf1.setText("");
            tf2.setText("");
            tf3.setText("");
            tf4.setText("");
            tf5.setText(racun + "");
        });

        /* Клик на дугме Prikazi transakcije:
           За сваку трансакцију из колекције креира се check-box дугме. Ако је трансакција извршена после 1.6.2019.,
           дугме ће бити чекирано. Уплате се приказују на левом панелу, a исплате на десном. Уједно се обрађују
           догађаји ниског нивоа над check-box дугметом. Када се мишем уђе на површину дугмета, дугме постаје неактивно,
           чиме се онемогућује да корисник промени статус дугмета (селектовано/деселектовнао). Када се напусти површина
           дугмета, дугме поново постаје активно. */

        prikazi.setOnAction(event -> {
            for(int i = 0; i < up.size(); i++) { 
                CheckBox c = new CheckBox(up.get(i).toString());
                if(up.get(i).getGodina() >= 2019 && up.get(i).getMesec() >= 6)
                    c.setSelected(true);
                hover(c);
                uplate.getChildren().add(c);
                
            }
            for(int i = 0; i < is.size(); i++) {
                CheckBox c = new CheckBox(is.get(i).toString());
                if(is.get(i).getGodina() >= 2019 && is.get(i).getMesec() >= 6)
                    c.setSelected(true);
                hover(c);
                isplate.getChildren().add(c);
                
            }
            
            
        });
        
        VBox v = new VBox();
        v.setPadding(new Insets(10, 10, 10, 10));
        v.setSpacing(20);
        v.getChildren().addAll(vb1, hb4, gp, hb5);
        
        Scene scene = new Scene(v, 450, 600);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public void dodaj(double iznos) {
        racun += iznos;
    }
    
    public void povuci(double iznos) {
        racun -= iznos;
    }
    
   public void hover(CheckBox cb){
       cb.addEventHandler(MouseEvent.MOUSE_ENTERED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
            cb.setDisable(true);
          }
        });
       
       cb.addEventHandler(MouseEvent.MOUSE_EXITED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
            cb.setDisable(false);
          }
        });
           
   }

   public void proveraDatuma(String dan, String mesec, String godina) {

        if(Integer.parseInt(mesec) > 12)
            errorDialog(mesec);

        if(Integer.parseInt(dan) > 31)
            errorDialog(dan);

        if((Integer.parseInt(mesec) == 4 || Integer.parseInt(mesec) == 6 || Integer.parseInt(mesec) == 9 || Integer.parseInt(mesec) == 11) && Integer.parseInt(dan) > 30)
            errorDialog(dan);

        // Провера за преступну и просту годину
        if (Integer.parseInt(godina) % 4 == 0 && (Integer.parseInt(godina) % 100 != 0 || Integer.parseInt(godina) % 400 == 0)) {
           if(Integer.parseInt(mesec) == 2 && Integer.parseInt(dan) > 29)
               errorDialog(dan);
        } else {
           if(Integer.parseInt(mesec) == 2 && Integer.parseInt(dan) > 28)
               errorDialog(dan);
        }
   }

   public void errorDialog(String unos) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle("Greška");
       alert.setHeaderText("Pogrešan unos!");
       alert.setContentText("\"" + unos + "\"" + " nije validan unos.");

       alert.showAndWait();
   }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
