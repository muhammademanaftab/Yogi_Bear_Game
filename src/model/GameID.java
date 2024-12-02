/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

public class GameID {
    public final String difficulty;
    public final int level;

    public GameID(String difficulty, int level) {
        this.difficulty = difficulty;
        this.level = level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(difficulty, level);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameID other = (GameID) obj;
        return level == other.level && Objects.equals(difficulty, other.difficulty);
    }
}

