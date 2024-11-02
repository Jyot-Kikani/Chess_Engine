package com.chessengine.intellij_chessengine.model;

import com.chessengine.intellij_chessengine.model.pieces.Pawn;

import java.util.ArrayList;
import java.util.List;

public class ChessModel {
    private final Board board;
    private boolean whiteToMove;
    private final List<String> moveHistory;
    private long turnNumber;


    public ChessModel() {
        board = new Board();
        moveHistory = new ArrayList<>();
        turnNumber = 0;
        whiteToMove = true;
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public Board getBoard() {
        return board;
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
}
