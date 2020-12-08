package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Connection {
    //set a port number to connect with clients
    public static final int PORT = 8081;
    //set a list of ServerClients objects to store the clients by their sockets
    public static LinkedList<ServerClients> serverList = new LinkedList<>();

    //init static desk to have an access to change in every part of program
    public static int[][] desk = new int[8][8];

    static {
        for (int i = 0; i < 8; i++) {
            desk[0][i] = 0;
            desk[1][i] = 1;
            desk[2][i] = 0;
            desk[3][i] = 0;
            desk[4][i] = 0;
            desk[5][i] = 0;
            desk[6][i] = 2;
            desk[7][i] = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        //make a socket with port 8081 to connect with clients
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                //allow listeners to connect
                Socket socket = server.accept();
                try {
                    //add clients' sockets to serverList<>
                    serverList.add(new ServerClients(socket));
                } catch (Exception e) {
                    socket.close();
                    e.printStackTrace();
                }
            }
        } finally {
            server.close();
        }
    }

    //how to move the figures
    public static void moving(Move move) {
        desk[move.from.getX()][move.from.getY()] = 0;
        desk[move.to.getX()][move.to.getY()] = move.player;

    }
}