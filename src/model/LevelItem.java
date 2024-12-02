/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public enum LevelItem {
    EMPTY(' '),
    TREE('T'),
    MOUNTAIN('M'),
    BASKET('B'),
    RANGER('R'),
    ENTRANCE('E'),
    YOGI('Y');

    public final char representation;

    LevelItem(char representation) {
        this.representation = representation;
    }
    
    
}
