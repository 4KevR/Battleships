package com.github.battleships;

import java.io.IOException;
import java.util.Random;

public class Player {
    private String playerName;
    Area myArea = new Area();
    public int hitShips = 0;
    public int numberOfShipPoints;
    public int [] shipLengths;
    public int placedShips = 0;
    public int hitAttempts = 0;
    public int lastShot = 1;
    private final int [] aimed = new int[4];

    public Player () {}

    public Player (String playerName, int [] shipLengths) {
        this.playerName = playerName;
        this.shipLengths = shipLengths;

        for (int ship: shipLengths) {
            numberOfShipPoints += ship;
        }
    }

    public void shoot (int [] coordinatesToShoot) throws IOException {
        char characterOnField = myArea.giveField(coordinatesToShoot);
        hitAttempts++;
        if (characterOnField == '#') {
            myArea.setField(coordinatesToShoot,'X');
            hitShips++;
            int countPartsOfShip = 0;
            int end = 0;
            int containCharacters = 0;
            while (end < 2) {
                if (end == 0) {
                    containCharacters = myArea.nearbyFieldsContainCharacter(coordinatesToShoot, "X#");
                    if (containCharacters != 1 && containCharacters != 3) {
                        end = 1;
                    } else {
                        coordinatesToShoot[(containCharacters-1)/2]++;
                    }
                } else {
                    countPartsOfShip += this.countShips(coordinatesToShoot);
                    coordinatesToShoot[(containCharacters/2)-1]--;
                    if (coordinatesToShoot[(containCharacters/2)-1] < 0) {
                        end = 2;
                    } else if (myArea.giveField(coordinatesToShoot) == 'O' || myArea.giveField(coordinatesToShoot) == '~') {
                        end = 2;
                    }
                }
            }
            if (hitShips == numberOfShipPoints) {
                lastShot = 4;  // won game
            } else if (countPartsOfShip == 0) {
                lastShot = 3;  // a whole ship is destroyed
            } else {
                lastShot = 2;  // hit a part of the ship
            }
        } else if (characterOnField == '~') {
            myArea.setField(coordinatesToShoot,'O');
            lastShot = 1;  // hit water
        }
    }

    public void computerShot (int difficulty) throws IOException {
        int [] coordinatesToShoot = new int[2];
        if (difficulty == 0) {
            coordinatesToShoot[0] = new Random().nextInt(10);
            coordinatesToShoot[1] = new Random().nextInt(10);
        } else if (difficulty >= 1 && difficulty <= 3) {
            if ((lastShot == 1 && aimed[2] == 0) || lastShot == 3) {
                if (new Random().nextFloat() < 0.18*difficulty) {
                    coordinatesToShoot = myArea.getFieldWithChar('#');
                    aimed[0] = coordinatesToShoot[0]; aimed[1] = coordinatesToShoot[1];
                    aimed[2] = 1; aimed[3] = 0;
                } else {
                    coordinatesToShoot = myArea.getFieldWithChar('~');
                    aimed[2] = 0;
                }
            } else if (lastShot == 2 || aimed[2] == 1) {
                coordinatesToShoot[0] = aimed[0]; coordinatesToShoot[1] = aimed[1];
                if (aimed[3] == 0) {
                    int set;
                    if (new Random().nextFloat() < 0.20*difficulty) {
                        set = myArea.nearbyFieldsContainCharacter(coordinatesToShoot, "#");
                        aimed[3] = set;
                    } else {
                        set = myArea.nearbyFieldsContainCharacter(coordinatesToShoot, "~");
                        if (set == 0) {
                            set = myArea.nearbyFieldsContainCharacter(coordinatesToShoot, "#");
                            aimed[3] = set;
                        }
                    }
                    myArea.applyNumberPos(coordinatesToShoot, set);
                } else {
                    myArea.applyNumberPos(coordinatesToShoot, aimed[3]);
                    if (myArea.giveField(coordinatesToShoot) == '~') {
                        if (aimed[3]%2 == 0) {
                            aimed[3]--;
                        } else {
                            aimed[3]++;
                        }
                    }
                }
                if (myArea.giveField(coordinatesToShoot) != '~') {
                    aimed[0] = coordinatesToShoot[0];
                    aimed[1] = coordinatesToShoot[1];
                }
            }
        } else {
            if (lastShot == 1 || (difficulty == 5 && lastShot == 3)) {
                coordinatesToShoot = myArea.getFieldWithChar('#');
            } else if (lastShot == 2 || difficulty == 5) {
                int set = 0;
                while (set == 0) {
                    coordinatesToShoot = myArea.getFieldWithChar('#');
                    set = myArea.nearbyFieldsContainCharacter(coordinatesToShoot, "X");
                }
            } else {
                coordinatesToShoot = myArea.getFieldWithChar('~');
            }
        }
        char characterOnField = myArea.giveField(coordinatesToShoot);
        if (characterOnField != 'X' && characterOnField != 'O') {
            this.shoot(coordinatesToShoot);
        } else {
            this.computerShot(difficulty);
        }
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
