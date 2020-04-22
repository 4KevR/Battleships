package com.github.battleships;

import java.util.Random;

public class Player {
    private final String playerName;
    Area myArea = new Area();
    public int hitShips = 0;
    public int numberOfShipPoints;
    private final int [] shipLengths;
    public int placedShips = 0;
    public int hitAttempts = 0;
    public int lastShot = 0;

    public Player (String playerName, int playerMode, int [] shipLengths) {
        this.playerName = playerName;
        this.shipLengths = shipLengths;

        for (int ship: shipLengths) {
            numberOfShipPoints += ship;
        }

        if (playerMode == 1) {
            this.placeShipComputer();
        }
    }

    public void shoot (int [] coordinatesToShoot) {
        char characterOnField = myArea.giveField(coordinatesToShoot);
        hitAttempts += 1;
        if (characterOnField == '#') {
            myArea.setField(coordinatesToShoot,'X');
            hitShips += 1;
            if (hitShips == numberOfShipPoints) {
                lastShot = 4;  // won game
            } else if (this.countShips(coordinatesToShoot) == 0) {
                lastShot = 3;  // a whole ship is destroyed
            } else {
                lastShot = 2;  // hit a part of the ship
            }
        } else if (characterOnField == '~') {
            myArea.setField(coordinatesToShoot,'O');
            lastShot = 1;  // hit water
        }
    }

    public int computerShot () {
        return 0;
    }

    public void placeShip (int [] coordinates, int alignment, int length) {
        // 0 = right; 1 = down;
        if (alignment == 0) {
            for (int i = coordinates[0]; i < coordinates[0] + length; i++) {
                int[] field = {i, coordinates[1]};
                myArea.placeShip(field);
            }
        } else {
            for (int i = coordinates[1]; i < coordinates[1] + length; i++) {
                int[] field = {coordinates[0], i};
                myArea.placeShip(field);
            }
        }
        placedShips += length;
    }

    public void placeShipComputer () {
        int success = 0;
        this.myArea.initArea();
        for (int ship: shipLengths) {
            int counterTry = 0;
            success = 0;
            while (counterTry < 20) {
                int alignment = new Random().nextInt(2);
                int posX;
                int posY;
                if (alignment == 0) {
                    posX = new Random().nextInt(10 - ship);
                    posY = new Random().nextInt(10);
                } else {
                    posX = new Random().nextInt(10);
                    posY = new Random().nextInt(10 - ship);
                }
                int countShips = 0;
                for (int counter = 0; counter < ship; counter++) {
                    int[] field = {posX, posY};
                    if (alignment == 0) {
                        field[0] += counter;
                    } else {
                        field[1] += counter;
                    }
                    countShips += this.countShips(field);
                }
                if (countShips == 0) {
                    int [] field = {posX, posY};
                    this.placeShip(field, alignment, ship);
                    success = 1;
                    break;
                } else {
                    counterTry++;
                }
            }
            if (success == 0) {
                break;
            }
        }
        if (success == 1) {
            this.myArea.zeigeSpielfeld();
            placedShips = numberOfShipPoints;
        } else {
            this.placeShipComputer();
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
