package com.chessengine.intellij_chessengine;

import com.chessengine.intellij_chessengine.model.ChessModel;
import com.chessengine.intellij_chessengine.view.ChessView;
import com.chessengine.intellij_chessengine.controller.ChessController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChessApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        ChessModel model = new ChessModel();
        ChessView view = new ChessView();
        ChessController controller = new ChessController(model, view);

        Scene scene = new Scene(view.getBoardGrid());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
