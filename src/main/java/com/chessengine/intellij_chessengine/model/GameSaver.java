package com.chessengine.intellij_chessengine.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Stack;

public class GameSaver {
    public static void saveGameHistory(Stack<Move> gameHistory, String fileName) {
        // Create a 'saved_games' directory if it doesn't exist
        File directory = new File("saved_games");
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Save the file in the 'saved_games' directory
        File file = new File(directory, fileName);

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameHistory);
            System.out.println("Game history saved successfully to " + file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
