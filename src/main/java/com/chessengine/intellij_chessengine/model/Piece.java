package com.chessengine.intellij_chessengine.model;

import javafx.util.Pair;
import java.util.List;
import java.util.ArrayList;

public abstract class Piece {
    private final PieceType type;
    private final boolean isWhite;
    protected List<Pair<Integer, Integer>> validMoves;  // List of valid moves for this piece

    public Piece(PieceType type, boolean isWhite) {
        this.type = type;
        this.isWhite = isWhite;
        this.validMoves = new ArrayList<>();
    }

    public PieceType getType() {
        return type;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public List<Pair<Integer, Integer>> getValidMoves() {
        return validMoves;
    }

    public void clearValidMoves() {
        validMoves.clear();
    }

    // Abstract method to calculate valid moves based on piece type and board state
    public abstract void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety);
}
