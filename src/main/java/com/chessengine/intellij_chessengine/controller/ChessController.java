package com.chessengine.intellij_chessengine.controller;

import com.chessengine.intellij_chessengine.model.ChessModel;
import com.chessengine.intellij_chessengine.model.GameSaver;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.view.ChessView;
import com.chessengine.intellij_chessengine.model.PieceType;

import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChessController {
    private final ChessModel model;
    private final ChessView view;

    private Pair<Integer, Integer> selectedSquare = null;
    private List<Pair<Integer, Integer>> validMoves = new ArrayList<>();

    // Load sounds as .mp3 files
    private final AudioClip selectSound = new AudioClip(getClass().getResource("/sounds/select3.mp3").toExternalForm());
    private final AudioClip moveSound = new AudioClip(getClass().getResource("/sounds/move3.mp3").toExternalForm());
    private final AudioClip undoSound = new AudioClip(getClass().getResource("/sounds/undo.mp3").toExternalForm());
    private final AudioClip captureSound = new AudioClip(getClass().getResource("/sounds/capture.mp3").toExternalForm());
    private final AudioClip checkSound = new AudioClip(getClass().getResource("/sounds/check.mp3").toExternalForm());
    private final AudioClip endSound = new AudioClip(getClass().getResource("/sounds/end.mp3").toExternalForm());

    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
        initializeListeners();
        updateView();

        Optional<String> fileName = view.promptForLoadFileName();
        if (fileName.isPresent()) {
            String fullFileName = fileName.get();
            // Add logic to load the game using the fullFileName
            System.out.println("Loading game from: " + fullFileName);
            model.loadGameHistory(fullFileName);

            // Add the moves to the move history panel
            for (int i = 0; i < model.getUndoStack().size(); i++) {
                if (i % 2 == 0) {
                    view.addMoveToHistory((i + 2) / 2, model.getUndoStack().get(i).getMoveNotation(), "");
                } else {
                    view.addMoveToHistory((i + 2) / 2, "", model.getUndoStack().get(i).getMoveNotation());
                }
            }

            // Undo last checkmateMove so that game isn't instantly terminated
            this.undoMove();
        } else {
            System.out.println("No file specified. Skipping load.");
        }
    }

    private void initializeListeners() {
        view.getBoardGrid().setOnMouseClicked(this::handleSquareClick);
    }

    private void handleSquareClick(MouseEvent event) {
        int col = (int) (event.getX() / view.TILE_SIZE);
        int row = (int) (event.getY() / view.TILE_SIZE);

        if(!model.getBoard().isInBounds(row, col))
            return;

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

                Piece movedPiece = model.getBoard().getPieceAt(row, col);

                // Determine turn number and add to move history
                long turnNumber = (model.getCurrentTurn() + 1) / 2;
                String moveNotation = model.getMoveNotation(movedPiece, startX, startY, row, col, capturedPiece);
                if (model.getCurrentTurn() % 2 == 1) {
                    view.addMoveToHistory(turnNumber, moveNotation, ""); // White's move
                } else {
                    view.addMoveToHistory(turnNumber, "", moveNotation); // Black's move
                }

                if(moveNotation.contains("#"))
                    endSound.play();
                else if(moveNotation.contains("+"))
                    checkSound.play();
                else if(capturedPiece != null)
                    captureSound.play();
                else
                    moveSound.play();

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
            undoSound.play();
            view.removeLastMoveFromHistory();
            updateView();
        }
    }

    public void redoMove() {
        if(model.redoMove()) {
            String s = model.getLastMove().getMoveNotation();
            if(s.contains("+"))
                checkSound.play();
            else if(s.contains("x"))
                captureSound.play();
            else
                moveSound.play();
            if(!model.isWhiteToMove())
                view.addMoveToHistory((model.getCurrentTurn() + 1) / 2, model.getLastMove().getMoveNotation(), "");
            else
                view.addMoveToHistory((model.getCurrentTurn() + 1) / 2, "", model.getLastMove().getMoveNotation());
            updateView();
        }
    }

    public void resetGame() {
        model.resetGame();
        view.clearMoveHistory();
        updateView();
    }

    public void saveGame() {
        // Get the file name from the view
        Optional<String> fileName = view.getFileName();

        // Check if the user provided a file name
        if (fileName.isPresent()) {
            String fullFileName = fileName.get();

            // Call the method to save the game history with the provided file name
            GameSaver.saveGameHistory(model.getUndoStack(), fullFileName);
        } else {
            System.out.println("Save operation cancelled.");
        }
    }
}