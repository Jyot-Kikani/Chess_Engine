package com.chessengine.intellij_chessengine.controller;

import com.chessengine.intellij_chessengine.model.ChessModel;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.view.ChessView;
import com.chessengine.intellij_chessengine.model.PieceType;

import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;

public class ChessController {
    private final ChessModel model;
    private final ChessView view;

    private Pair<Integer, Integer> selectedSquare = null;
    private List<Pair<Integer, Integer>> validMoves = new ArrayList<>();

    // Load sounds as .mp3 files
    private final AudioClip selectSound = new AudioClip(getClass().getResource("/sounds/select3.mp3").toExternalForm());
    private final AudioClip moveSound = new AudioClip(getClass().getResource("/sounds/move2.mp3").toExternalForm());

    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;
        initializeListeners();
        updateView();
    }

    private void initializeListeners() {
        view.getBoardGrid().setOnMouseClicked(this::handleSquareClick);
    }

    private void handleSquareClick(MouseEvent event) {
        int col = (int) (event.getX() / view.TILE_SIZE);
        int row = (int) (event.getY() / view.TILE_SIZE);

        if (selectedSquare == null) {
            // Select the piece and calculate valid moves
            Piece piece = model.getBoard().getPieceAt(row, col);
            if (piece != null) {
                selectedSquare = new Pair<>(row, col);
                if(piece.isWhite() != model.isWhiteToMove()) {
                    selectedSquare = null;
                    return;
                }
                piece.calculateValidMoves(row, col, model.getBoard(), false);
                validMoves = piece.getValidMoves();
            }
            updateView();
        }
        else {
            // Attempt to move the selected piece
            int startX = selectedSquare.getKey();
            int startY = selectedSquare.getValue();
            Piece capturedPiece = model.getBoard().getPieceAt(row, col);
            if (model.movePiece(startX, startY, row, col)) {
                moveSound.play();

                Piece movedPiece = model.getBoard().getPieceAt(row, col);

                // Determine turn number and add to move history
                long turnNumber = (model.getCurrentTurn() + 1) / 2;
                String moveNotation = model.getMoveNotation(movedPiece, startX, startY, row, col, capturedPiece);
                if (model.getCurrentTurn() % 2 == 1) {
                    view.addMoveToHistory(turnNumber, moveNotation, ""); // White's move
                } else {
                    view.addMoveToHistory(turnNumber, "", moveNotation); // Black's move
                }

                // Clear selection after attempting move
                selectedSquare = null;
                validMoves.clear();
                view.animatePieceMovement(model.getBoard(), startX, startY, row, col, movedPiece);
            }

            else {
                // If the clicked square is another piece, select it instead
                Piece piece = model.getBoard().getPieceAt(row, col);
                if (piece != null) {
                    selectedSquare = new Pair<>(row, col);
                    // If the piece is not the same color as the current player, deselect it
                    if(piece.isWhite() != model.isWhiteToMove()) {
                        selectedSquare = null;
                        validMoves.clear();
                        updateView();
                        return;
                    }
//                    selectSound.play();
                    piece.calculateValidMoves(row, col, model.getBoard(), false);
                    validMoves = piece.getValidMoves();
                    updateView();
                    return;
                }
            }
        }
    }

    private void updateView() {
        view.updateBoard(model.getBoard(), selectedSquare, validMoves);
    }
}