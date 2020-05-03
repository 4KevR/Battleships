package com.github.battleships;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class RemotePlayer extends Player {
    int mode;
    PrintWriter out;
    BufferedReader in;

    private String playerName = "";
    private final String mainPlayer;

    int lastShot;
    int [] coordinatesToShoot = new int[2];

    public RemotePlayer (int mode, Socket clientSocket, String mainPlayer) throws IOException {
        this.mode = mode;
        this.mainPlayer = mainPlayer;

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (this.mode == 0) {
            this.serverExchangeNames();
        } else {
            this.clientExchangeNames();
        }
    }

    public void serverExchangeNames () throws IOException {
        while (playerName.equals("")) {
            playerName = in.readLine();
            out.println(mainPlayer);
        }
    }

    public void clientExchangeNames () throws IOException {
        while (playerName.equals("")) {
            out.println(mainPlayer);
            playerName = in.readLine();
        }
    }

    public void syncPlacingShips () throws IOException {
        if (mode == 0) {
            String confirm = "";
            while (confirm.equals("")) {
                confirm = in.readLine();
            }
        } else {
            out.println("Confirm");
        }
    }

    @Override
    public void shoot (int [] coordinates) throws IOException {
        lastShot = 0;
        hitAttempts++;
        while (lastShot == 0) {
            out.println(Arrays.toString(coordinates));
            lastShot = Integer.parseInt(in.readLine());
        }
        if (lastShot == 1) {
            myArea.setField(coordinates, 'O');
        } else {
            myArea.setField(coordinates, 'X');
        }
        myArea.showArea();
    }

    public int [] receiveAttack () throws IOException {
        int received = 0;
        while (received == 0) {
            String input = in.readLine();
            if (!input.equals("")) {
                received++;
                coordinatesToShoot = fromString(input);
            }
        }
        return coordinatesToShoot;
    }

    public void sendResult (int pLastShot) {
        out.println(pLastShot);
    }

    public void waitForConfirmation () throws IOException {
        String confirmation = "";
        while (confirmation.equals("")) {
            confirmation = in.readLine();
        }
    }

    public void confirm () {
        out.println("Confirm");
    }

    public String getPlayerName () {
        return playerName;
    }

    private static int[] fromString(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        int [] result = new int[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }
}
