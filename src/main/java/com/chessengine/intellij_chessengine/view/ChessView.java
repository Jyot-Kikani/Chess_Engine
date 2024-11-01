package com.chessengine.intellij_chessengine.view;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;
import java.util.List;

public class ChessView {
    private final GridPane boardGrid = new GridPane();
    public final int TILE_SIZE = 80;

    public ChessView() {
        initializeBoardGrid();
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
            highlightSquare(move.getKey(), move.getValue(), Color.ORANGE);
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
        highlight.setOpacity(0.75);  // semi-transparent for overlay
        boardGrid.add(highlight, col, row);
    }

    private Image getPieceImage(Piece piece) {
        String imageName = (piece.isWhite() ? "w" : "b") + (piece.getType() == PieceType.KNIGHT ? "n" : piece.getType().name().toLowerCase().charAt(0)) + ".png";
        return new Image(getClass().getResourceAsStream("/images/" + imageName));
    }
}
