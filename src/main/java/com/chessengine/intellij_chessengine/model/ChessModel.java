package com.chessengine.intellij_chessengine.model;

import com.chessengine.intellij_chessengine.model.pieces.Pawn;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChessModel {
    private final Board board;
    private boolean whiteToMove;
    private long turnNumber;
    private Stack<Move> undoStack = new Stack<>();
    private Stack<Move> redoStack = new Stack<>();

    public ChessModel() {
        board = new Board();
        turnNumber = 0;
        whiteToMove = true;
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public Board getBoard() {
        return board;
    }

    public void updateStack(String move) {
        undoStack.push(new Move(move, board.getFENfromBoard()));
        redoStack.clear();
    }

    public Move getLastMove() {
        return undoStack.peek();
    }

    public Stack<Move> getUndoStack() {
        return undoStack;
    }

    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPieceAt(startX, startY);
        if (piece != null && piece.isWhite() == whiteToMove) {
            if (piece.getValidMoves().contains(new javafx.util.Pair<>(endX, endY))) {
                board.movePiece(startX, startY, endX, endY);
                whiteToMove = !whiteToMove; // Switch turns
                turnNumber++;
                return true;
            }
        }
        return false;
    }

    public String getMoveNotation(Piece piece, int startX, int startY, int endX, int endY, Piece capturedPiece) {
        StringBuilder notation = new StringBuilder();

        // Add piece type (no character for pawn)
        if (!(piece instanceof Pawn)) {
            notation.append(board.getPieceChar(piece));
        }

        // Check for capture
        if (capturedPiece != null) {
            if (piece instanceof Pawn) {
                notation.append((char) ('a' + startY)); // Column of pawn if it captures
            }
            notation.append("x"); // Capture indicator
        }

        // Add destination square (e.g., "e5")
        notation.append((char) ('a' + endY));
        notation.append(8 - endX); // Convert row index to board notation

        // Check if move results in check or checkmate
        if (board.isKingInCheck(!piece.isWhite())) {
            notation.append(isCheckmate() ? "#" : "+");
        }

        return notation.toString();
    }

    // Check if the current player's king is in check
    public boolean isCheck() {
        return board.isKingInCheck(whiteToMove);
    }

    // Check if the current player is in checkmate
    public boolean isCheckmate() {
        // If the king is in check, check for possible moves
        if (isCheck()) {
            // Check all pieces to see if any can make a valid move
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece piece = board.getPieceAt(row, col);
                    if (piece != null && piece.isWhite() == whiteToMove) {
                        // Check if this piece has any valid moves
                        piece.calculateValidMoves(row, col, board, false);
                        if (!piece.getValidMoves().isEmpty()) {
                            return false; // There is a valid move, so not checkmate
                        }
                    }
                }
            }
            return true; // No valid moves left, so checkmate
        }
        return false; // Not in check, thus not checkmate
    }

    // Get the current player's turn as a string
    public long getCurrentTurn() {
        return turnNumber;
    }

    public boolean undoMove() {
        if(undoStack.isEmpty())
            return false;

        Move move = undoStack.pop();
        redoStack.push(move);
        if(undoStack.isEmpty())
            board.initializeBoard();
        else
            board.initializeBoardFromFEN(undoStack.peek().getFenString());
        whiteToMove = !whiteToMove;
        turnNumber--;
        return true;
    }

    public boolean redoMove() {
        if(redoStack.isEmpty())
            return false;

        Move move = redoStack.pop();
        undoStack.push(move);
        board.initializeBoardFromFEN(move.getFenString());
        whiteToMove = !whiteToMove;
        turnNumber++;
        return true;
    }

    public void resetGame() {
        board.initializeBoard();
        undoStack.clear();
        redoStack.clear();
        whiteToMove = true;
        turnNumber = 0;
    }

    public void loadGameHistory(String filename) {
        Stack<Move> gameHistory = GameLoader.loadGameHistory(filename);
        if (gameHistory != null) {
            undoStack = gameHistory;
            board.initializeBoardFromFEN(undoStack.peek().getFenString());
            whiteToMove = undoStack.size() % 2 == 0;
            turnNumber = undoStack.size();
        }
    }
}
