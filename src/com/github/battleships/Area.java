package com.github.battleships;

import java.util.Random;

public class Area {
    private final char[][] area = new char[10][10];

    public Area () {
        this.initArea();
    }

    public void initArea () {
        for (int i = 0; i < this.area.length; i++) {
            for (int j = 0; j < this.area.length; j++) {
                this.area[i][j] = '~';
            }
        }
    }

    public void placeShip (int [] coordinates){
        this.area[coordinates[1]][coordinates[0]] = '#';
    }

    public char giveField (int [] coordinates){
        return this.area[coordinates[1]][coordinates[0]];
    }

    public void setField (int [] coordinates, char character){
        this.area[coordinates[1]][coordinates[0]] = character;
    }

    public int [] getFieldWithChar (char character) {
        int set = 0;
        int [] field = new int[2];
        while (set == 0) {
            field[0] = new Random().nextInt(10);
            field[1] = new Random().nextInt(10);
            if (this.giveField(field) == character) {
                set = 1;
            }
        }
        return field;
    }

    public int nearbyFieldsContainCharacter (int [] coordinates, String characters) {
        int [][] fields = {{coordinates[0]+1, coordinates[1]}, {coordinates[0]-1, coordinates[1]},
                {coordinates[0], coordinates[1]+1}, {coordinates[0], coordinates[1]-1}};
        for (int counter = 0; counter < 4; counter++) {
            if (fields[counter][0] >= 0 && fields[counter][0] <= 9 && fields[counter][1] >= 0 && fields[counter][1] <= 9) {
                if (characters.indexOf(this.giveField(fields[counter])) != -1) {
                    return counter+1;
                }
            }
        }
        return 0;
    }

    public void applyNumberPos (int [] coordinates, int number) {
        if (number == 1) {
            coordinates[0] = coordinates[0]+1;
        } else if (number == 2) {
            coordinates[0] = coordinates[0]-1;
        } else if (number == 3) {
            coordinates[1] = coordinates[1]+1;
        } else {
            coordinates[1] = coordinates[1]-1;
        }
    }

    public void showArea () {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i=0;i<this.area.length;i++) {
            int  asciiNumber    = 65+i;
            char asciiChar = (char)asciiNumber;
            System.out.print(asciiChar);
            System.out.print(" ");
            for (int j=0;j<this.area.length;j++) {
                System.out.print(this.area[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}