package com.github.battleships;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RemotePlayer extends Player {
    int mode;
    private Socket clientSocket;
    PrintWriter out;
    BufferedReader in;

    private String playerName = "";
    private final String mainPlayer;

    public RemotePlayer (int mode, Socket clientSocket, String mainPlayer) throws IOException {
        this.mode = mode;
        this.mainPlayer = mainPlayer;
        this.clientSocket = clientSocket;

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
        System.out.println(playerName);
    }

    public void clientExchangeNames () throws IOException {
        while (playerName.equals("")) {
            out.println(mainPlayer);
            playerName = in.readLine();
        }
        System.out.println(playerName);
    }

    public String getPlayerName () {
        return playerName;
    }
}
