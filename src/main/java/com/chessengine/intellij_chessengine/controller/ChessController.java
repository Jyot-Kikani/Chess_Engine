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
        this.view.setController(this);
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
                // Adding pawn promotion condition
                if (model.getBoard().getPieceAt(row, col).getType() == PieceType.PAWN && (row == 0 || row == 7)) {
                    Piece newPieace = view.displayPromotionDialog(model.getBoard(), row, col);
                    model.getBoard().setPieceAt(row, col, newPieace);
                }

                moveSound.play();

                Piece movedPiece = model.getBoard().getPieceAt(row, col);

                // Determine turn number and add to move history
                System.out.println("Current: " + model.getCurrentTurn());
                long turnNumber = (model.getCurrentTurn() + 1) / 2;
                System.out.println("turnNumber: " + turnNumber);
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

                model.updateStack(moveNotation);

                if(moveNotation.charAt(moveNotation.length()-1) == '#') {
                    String msg = " wins";
                    if (movedPiece.isWhite())
                        msg = "White" + msg;
                    else
                        msg = "Black" + msg;
                    view.showGameEndDialog(msg);
                }
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

    public void undoMove() {
        if(model.undoMove()) {
            view.removeLastMoveFromHistory();
            updateView();
        }
    }

    public void redoMove() {
        if(model.redoMove()) {
            if(!model.isWhiteToMove())
                view.addMoveToHistory((model.getCurrentTurn() + 1) / 2, model.getLastMove().getKey(), "");
            else
                view.addMoveToHistory((model.getCurrentTurn() + 1) / 2, "", model.getLastMove().getKey());
            updateView();
        }
    }

    public void resetGame() {
        model.resetGame();
        view.clearMoveHistory();
        updateView();
    }

    public void saveGame() {

    }
}