package com.chessengine.intellij_chessengine.view;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;


import com.chessengine.intellij_chessengine.model.pieces.Bishop;
import com.chessengine.intellij_chessengine.model.pieces.Knight;
import com.chessengine.intellij_chessengine.model.pieces.Queen;
import com.chessengine.intellij_chessengine.model.pieces.Rook;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ChessView {
    private final GridPane boardGrid = new GridPane();
    private TableView<MoveRecord> moveHistoryTable;
    public final int TILE_SIZE = 80;

    public ChessView() {
        initializeBoardGrid();
        moveHistoryTable = setupMoveHistoryPanel();
    }

    private void initializeBoardGrid() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.DARKGREEN);
                boardGrid.add(tile, col, row);
            }
        }
    }

    public GridPane getBoardGrid() {
        return boardGrid;
    }

    public void updateBoard(Board board, Pair<Integer, Integer> selectedSquare, List<Pair<Integer, Integer>> validMoves) {
        boardGrid.getChildren().clear();
        initializeBoardGrid();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    Image pieceImage = getPieceImage(piece);
                    ImageView pieceImageView = new ImageView(pieceImage);
                    pieceImageView.setFitHeight(TILE_SIZE);
                    pieceImageView.setFitWidth(TILE_SIZE);
                    boardGrid.add(pieceImageView, col, row);
                }
            }
        }

        // Highlight the selected square if any
        if (selectedSquare != null) {
            highlightSquare(selectedSquare.getKey(), selectedSquare.getValue(), Color.YELLOW);
        }

        // Highlight valid move squares
        for (Pair<Integer, Integer> move : validMoves) {
            Piece targetPiece = board.getPieceAt(move.getKey(), move.getValue());
            if (targetPiece != null) {
                highlightSquare(move.getKey(), move.getValue(), Color.RED);
            } else {
                highlightSquare(move.getKey(), move.getValue(), Color.ORANGE);
            }
        }
    }

    public void animatePieceMovement(Board board, int fromRow, int fromCol, int toRow, int toCol, Piece piece) {
        Image pieceImage = getPieceImage(piece);
        ImageView pieceImageView = new ImageView(pieceImage);
        pieceImageView.setFitHeight(TILE_SIZE);
        pieceImageView.setFitWidth(TILE_SIZE);

        // Set initial position on the board
        boardGrid.add(pieceImageView, fromCol, fromRow);

        // Create a transition animation
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), pieceImageView);
        transition.setByX((toCol - fromCol) * TILE_SIZE);
        transition.setByY((toRow - fromRow) * TILE_SIZE);
        transition.setOnFinished(e -> {
            // After animation, update the board visually
            PauseTransition pause = new PauseTransition(Duration.millis(2000)); // Adjust as needed
            boardGrid.getChildren().remove(pieceImageView);
            updateBoard(board, null, List.of()); // Replace with actual board update
        });

        transition.play();
    }

    private void highlightSquare(int row, int col, Color color) {
        Rectangle highlight = new Rectangle(TILE_SIZE, TILE_SIZE);
        highlight.setFill(color);
        if(color == Color.RED)
            highlight.setOpacity(0.60);
        else
            highlight.setOpacity(0.75);  // semi-transparent for overlay
        boardGrid.add(highlight, col, row);
    }

    private Image getPieceImage(Piece piece) {
        String imageName = (piece.isWhite() ? "w" : "b") + (piece.getType() == PieceType.KNIGHT ? "n" : piece.getType().name().toLowerCase().charAt(0)) + ".png";
        return new Image(getClass().getResourceAsStream("/images/" + imageName));
    }

    private TableView<MoveRecord> setupMoveHistoryPanel() {
        TableView<MoveRecord> tableView = new TableView<>();
        tableView.setPrefWidth(300);

        TableColumn<MoveRecord, Integer> turnColumn = new TableColumn<>("Turn");
        turnColumn.setCellValueFactory(new PropertyValueFactory<>("turn"));
        turnColumn.setPrefWidth(50);

        TableColumn<MoveRecord, String> whiteMoveColumn = new TableColumn<>("White");
        whiteMoveColumn.setCellValueFactory(new PropertyValueFactory<>("whiteMove"));
        whiteMoveColumn.setPrefWidth(125);

        TableColumn<MoveRecord, String> blackMoveColumn = new TableColumn<>("Black");
        blackMoveColumn.setCellValueFactory(new PropertyValueFactory<>("blackMove"));
        blackMoveColumn.setPrefWidth(125);

        tableView.getColumns().addAll(turnColumn, whiteMoveColumn, blackMoveColumn);
        return tableView;
    }

    public VBox getMoveHistoryPanel() {
        Label historyLabel = new Label("Move History");
        historyLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5px;");

        VBox moveHistoryContainer = new VBox();
        moveHistoryContainer.getChildren().addAll(historyLabel, moveHistoryTable);
        moveHistoryContainer.setSpacing(5);
        moveHistoryContainer.setPrefHeight(400); // Adjust as needed
        return moveHistoryContainer;
    }

    public void addMoveToHistory(long turn, String whiteMove, String blackMove) {
        if (!blackMove.isEmpty() && !moveHistoryTable.getItems().isEmpty()) {
            // Retrieve the last move record and update it with the black move
            MoveRecord lastMove = moveHistoryTable.getItems().getLast();
            lastMove.setBlackMove(blackMove);
            moveHistoryTable.refresh();
        } else {
            MoveRecord move = new MoveRecord(turn, whiteMove, blackMove);
            moveHistoryTable.getItems().add(move);
        }
    }

    public Piece displayPromotionDialog(Board board, int row, int col) {
        Piece currPiece = board.getPieceAt(row, col);
        List<String> choices = Arrays.asList("Queen", "Rook", "Bishop", "Knight");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", choices);
        dialog.setTitle("Pawn Promotion");
        dialog.setHeaderText("Choose a piece for promotion:");
        dialog.setContentText("Select piece:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            switch (result.get()) {
                case "Rook":
                    return new Rook(currPiece.isWhite());
                case "Bishop":
                    return new Bishop(currPiece.isWhite());
                case "Knight":
                    return new Knight(currPiece.isWhite());
                default:
                    return new Queen(currPiece.isWhite());
            }
        }
        return new Queen(currPiece.isWhite()); // Default to Queen if no selection is made
    }

    public void showGameEndDialog(String resultMessage) {
        // Create an alert dialog for the end of the game
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(resultMessage);
        alert.setContentText("Would you like to start a new game?");

        // Add options for the user
        ButtonType saveGameButton = new ButtonType("Save Game");
        ButtonType newGameButton = new ButtonType("New Game");
        ButtonType exitButton = new ButtonType("Exit");

        alert.getButtonTypes().setAll(saveGameButton, newGameButton, exitButton);

        // Show the dialog and capture the result
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == newGameButton) {
            System.out.println("New Game");
//            startNewGame(); // Call method to start a new game
            System.exit(0);
        }
        else if(result.isPresent() && result.get() == saveGameButton) {
            System.out.println("Save Game");
//            saveGame();
            System.exit(0);

        }
        else {
            System.exit(0); // Exit the application if the user chooses "Exit"
        }
    }
}
