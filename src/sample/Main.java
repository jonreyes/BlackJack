package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Main extends Application {
    int cash = 100;
    int bet = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Deck deck = new Deck();
        deck.complete();
        Deck player = new Deck();
        Deck dealer = new Deck();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        Button reset = new Button("Reset");
        //vbox.getChildren().add(reset);

        // Prepare deck view
        FlowPane deckPane = new FlowPane();
        ArrayList<ImageView> deckView = new ArrayList<>();
        deckPane.setAlignment(Pos.CENTER);
        vbox.getChildren().add(deckPane);

        Button shuffle = new Button("Shuffle");
        shuffle.setOnAction(e->{
            deck.complete();
            deck.shuffle();
            System.out.println("Deck");
            deckPane.getChildren().removeAll(deckPane.getChildren());
            for (Card c : deck.getCards()){
                //deckPane.getChildren().add(new ImageView("card/" + c.toString() + ".png"));
                System.out.print(c.toString() + ",");
            }
            System.out.println();

        });
        //vbox.getChildren().add(shuffle);

        Label cashLabel = new Label("Cash");
        Label cashAmount = new Label("$100");
        vbox.getChildren().add(cashLabel);
        vbox.getChildren().add(cashAmount);

        Button betButton = new Button("Bet");
        Label betAmount = new Label();
        TextField enterBet = new TextField();
        enterBet.setAlignment(Pos.CENTER);
        vbox.getChildren().add(betButton);
        vbox.getChildren().add(betAmount);
        vbox.getChildren().add(enterBet);

        betButton.setOnAction(e->{
            bet = Integer.parseInt(enterBet.getText());
            System.out.println("Bet:"+bet);
            betAmount.setText("$"+enterBet.getText());
        });


        Label dealerLabel = new Label("Dealer");
        vbox.getChildren().add(dealerLabel);

        FlowPane dealerPane = new FlowPane();
        ArrayList<ImageView> dealerView = new ArrayList<>();
        dealerPane.setAlignment(Pos.CENTER);
        vbox.getChildren().add(dealerPane);

        Label dealerPoints = new Label("0");
        vbox.getChildren().add(dealerPoints);

        Button ddraw = new Button("Draw");
        ddraw.setOnAction(e->{
            // Display Dealer Deck
            dealerView.add(new ImageView("card/"+deck.draw().toString()+".png"));
            for(ImageView d: dealerView){
                if(!dealerPane.getChildren().contains(d)) {
                    dealerPane.getChildren().add(d);
                }
            }
            // Update Decks
            dealer.add(deck.draw());
            deck.remove(deck.draw());
            // Print Dealer Deck
            System.out.println("Dealer");
            System.out.print(dealer.toString());
            // Update Dealer Points
            dealerPoints.setText(""+dealer.value());
            if(dealer.value()>21){
                dealerLabel.setText("Dealer Busts!");
            }
            if(dealer.value()==21){
                dealerLabel.setText("Dealer Wins!");
                cash -= bet;
                cashAmount.setText("$"+cash);
            }
        });
        //vbox.getChildren().add(ddraw);

        Label playerLabel = new Label("Player");
        vbox.getChildren().add(playerLabel);

        FlowPane playerPane = new FlowPane();
        ArrayList<ImageView> playerView = new ArrayList<>();
        playerPane.setAlignment(Pos.CENTER);
        vbox.getChildren().add(playerPane);

        Label playerPoints = new Label("0");
        vbox.getChildren().add(playerPoints);

        Button pdraw = new Button("Draw");
        pdraw.setOnAction(e->{
            // Display Player Deck
            playerView.add(new ImageView("card/"+deck.draw().toString()+".png"));
            for(ImageView h: playerView){
                if(!playerPane.getChildren().contains(h)){
                    playerPane.getChildren().add(h);
                }
            }
            // Update Decks
            player.add(deck.draw());
            deck.remove(deck.draw());
            // Print Player Decks
            System.out.println("Player");
            System.out.print(player.toString());
            // Update Player Points
            playerPoints.setText(""+player.value());
            if(player.value()>21){
                playerLabel.setText("Player Busts!");
            }
            if(player.value()==21){
                playerLabel.setText("Player Wins!");
                cash += bet;
                cashAmount.setText("$"+cash);
            }
        });
        //vbox.getChildren().add(pdraw);

        Button deal = new Button("Deal");
        deal.setOnAction(e->{
            reset.fire();
            shuffle.fire();
            ddraw.fire();
            pdraw.fire();
            pdraw.fire();
        });
        vbox.getChildren().add(deal);

        Button evaluate = new Button("Evaluate");
        evaluate.setOnAction(e->{
            int dealerDistance = Math.abs(21-Integer.parseInt(dealerPoints.getText()));
            int playerDistance = Math.abs(21-Integer.parseInt(playerPoints.getText()));
            if (dealerDistance<playerDistance){
                dealerLabel.setText("Dealer Wins!");
                cash -= bet;
                cashAmount.setText("$"+cash);
            }
            else{
                playerLabel.setText("Player Wins!");
                cash += bet;
                cashAmount.setText("$"+cash);
            }
        });

        FlowPane choices = new FlowPane();
        Button hit = new Button("Hit");
        hit.setOnAction(e->{
            if(dealer.value()<15){
                ddraw.fire();
            }
            pdraw.fire();
        });
        Button stand = new Button("Stand");
        stand.setOnAction(e->{
            while(dealer.value()<15){
                ddraw.fire();
            }
            evaluate.fire();
        });
        choices.getChildren().addAll(hit,stand);
        choices.setAlignment(Pos.CENTER);
        vbox.getChildren().add(choices);

        //vbox.getChildren().add(evaluate);
        reset.setOnAction(e->{
            cashAmount.setText("$"+cash);
            deckPane.getChildren().removeAll(deckPane.getChildren());
            deck.complete();
            dealerPane.getChildren().removeAll(dealerPane.getChildren());
            dealerView.removeAll(dealerView);
            dealer.empty();
            dealerLabel.setText("Dealer");
            dealerPoints.setText("0");
            playerPane.getChildren().removeAll(playerPane.getChildren());
            playerView.removeAll(playerView);
            player.empty();
            playerLabel.setText("Player");
            playerPoints.setText("0");
        });

        Scene scene = new Scene(vbox,800, 800);
        primaryStage.setTitle("BlackJack");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
