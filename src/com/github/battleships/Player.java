package com.github.battleships;

import java.util.Arrays;

public class Player {

    private String playerName = "";
    Area myArea = new Area();
    int hitShips = 0;
    int numberOfShipPoints;

    public Player (String playerName, int numberOfShipPoints) {
        this.playerName = playerName;
        this.numberOfShipPoints = numberOfShipPoints;
    }

    public int shoot (int [] coordinatesToShoot) {
        char characterOnField = myArea.giveField(coordinatesToShoot);
        if (characterOnField == '#') {
            myArea.setField(coordinatesToShoot,'X');
            hitShips += 1;
            if (hitShips == numberOfShipPoints) {
                return 4;  // won game
            } else if (this.countShips(coordinatesToShoot) == 0) {
                return 3;  // a whole ship is destroyed
            } else {
                return 1;  // hit a part of the ship
            }
        } else if (characterOnField == '~') {
            myArea.setField(coordinatesToShoot,'O');  // hit water
            return 2;
        } else {
            return 0;  // Feld wurde bereits beschossen - wiederhole Eingabe
        }
    }

    public void placeShip (int [] coordinates, int alignment, int length ) {
        // 0 = right; 1 = down;
        if (alignment == 0) {
            for (int i = coordinates[0]; i < coordinates[0] + length; i++) {
                int[] field = {i, coordinates[1]};
                myArea.placeShip(field);
            }
        } else {
            for (int i = coordinates[1]; i < coordinates[1] + length; i++) {
                int[] field = {coordinates[0], i};
                System.out.println(Arrays.toString(field));
                myArea.placeShip(field);
            }
        }
    }

    public int countShips (int [] coordinates) {
        int counterShip = 0;
        int [] field = new int[2];
        for (int y = coordinates[1]-1; y < coordinates[1]+2; y++) {
            for (int x = coordinates[0]-1; x < coordinates[0]+2; x++) {
                if (y >= 0 && y <= 9 && x >= 0 && x <= 9) {
                    field[0] = x; field[1] = y;
                    if (myArea.giveField(field) == '#') {
                        counterShip += 1;
                    }
                }
            }
        }
        return counterShip;
    }

    public String getName () {
        return playerName;
    }
}
