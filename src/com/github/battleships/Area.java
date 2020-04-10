package com.github.battleships;

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

    public void zeigeSpielfeld() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i=0;i<this.area.length;i++) {
            int  asciiZahl    = 65+i;
            char asciiZeichen = (char)asciiZahl;
            System.out.print( asciiZeichen  );
            System.out.print(" ");
            for (int j=0;j<this.area.length;j++) {
                System.out.print(this.area[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}