package com.chessengine.intellij_chessengine;

import com.chessengine.intellij_chessengine.model.ChessModel;
import com.chessengine.intellij_chessengine.view.ChessView;
import com.chessengine.intellij_chessengine.controller.ChessController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChessApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        ChessModel model = new ChessModel();
        ChessView view = new ChessView();
        ChessController controller = new ChessController(model, view);

        // Create a BorderPane as the main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(view.getBoardGrid()); // Set the chessboard in the center
        mainLayout.setRight(view.getMoveHistoryPanel()); // Set the move history panel on the right

        Scene scene = new Scene(mainLayout, 940, 640);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}