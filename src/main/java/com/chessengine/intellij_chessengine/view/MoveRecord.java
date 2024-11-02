package com.chessengine.intellij_chessengine.view;

public class MoveRecord {
    private long turn;
    private String whiteMove;
    private String blackMove;

    public MoveRecord(long turn, String whiteMove, String blackMove) {
        this.turn = turn;
        this.whiteMove = whiteMove;
        this.blackMove = blackMove;
    }

    public long getTurn() {
        return turn;
    }

    public String getWhiteMove() {
        return whiteMove;
    }

    public String getBlackMove() {
        return blackMove;
    }

    public void setWhiteMove(String whiteMove) {
        this.whiteMove = whiteMove;
    }

    public void setBlackMove(String blackMove) {
        this.blackMove = blackMove;
    }
}
