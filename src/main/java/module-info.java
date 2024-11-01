module com.chessengine.intellij_chessengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.chessengine.intellij_chessengine to javafx.fxml;
    exports com.chessengine.intellij_chessengine;
}