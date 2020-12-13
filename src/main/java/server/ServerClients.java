package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import converter.FileConverter;

import java.io.*;
import java.net.Socket;

import static server.Connection.desk;

public class ServerClients extends Thread {
    //definite socket
    private Socket socket;
    //input stream - read
    private BufferedReader in;
    //output stream - write
    private BufferedWriter out;

    //possible winners
    enum WinPlayer {
        PLAYER1, PLAYER2
    }

    //constructor to init socket, streams and start thread
    public ServerClients(Socket socket) {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //start thread
    @Override
    public void run() {
        String json;
        try {
            WinPlayer winPlayer;

            a:
            while (true) {

                //read json from client
                json = in.readLine();
                //convert json to Move
                Move move = FileConverter.toJavaObject(json);
                //check that field belongs to player
                if (desk[move.from.getX()][move.from.getY()] != move.player) {
                    throw new Exception("Field 'from' is not yours");
                }
                //check the length of moving
                if (move.to.getX() - move.from.getX() > 1 && (move.from.getX() != 1 && move.from.getX() != 6)) {
                    throw new Exception("Field 'to' is too far");
                }
                //check the gambit length
                if (move.to.getX() - move.from.getX() > 2 && (move.from.getX() == 1 || move.from.getX() == 6)) {
                    throw new Exception("Field 'to' is too far");
                }
                //check valid of input coordinates
                if (move.from.getX() > 7 || move.from.getX() < 0 || move.to.getX() > 7 || move.to.getX() < 0) {
                    throw new IndexOutOfBoundsException("Out of desk coordinates input");
                }
                if (move.from.getY() > 7 || move.from.getY() < 0 || move.to.getY() > 7 || move.to.getY() < 0) {
                    throw new IndexOutOfBoundsException("Out of desk coordinates input");
                }
                //check validate of field 'to'
                if ((move.to.getX() - move.from.getX() == 1 && move.from.getY() == move.to.getY())
                        && desk[move.to.getX()][move.to.getY()] != 0) {
                    throw new Exception("Illegal moving");
                }
                if ((move.to.getX() - move.from.getX() == 1 && move.to.getY() - move.from.getY() == 1) &&
                        (desk[move.to.getX()][move.to.getY()] == move.player || desk[move.to.getX()][move.to.getY()] == 0)) {
                    throw new Exception("Illegal moving");
                }
                if (move.to.getY() - move.from.getY() > 1) {
                    throw new Exception("Illegal moving");
                }
                Connection.moving(move);
                //check winning
                boolean flag1 = false;
                boolean flag2 = false;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (desk[i][j] != 2) {
                            flag1 = true;
                        }
                        if (desk[i][j] != 1) {
                            flag2 = true;
                        }
                    }
                }
                if (!flag1) {
                    winPlayer = WinPlayer.PLAYER2;
                    System.out.println("The second player has won");
                    break ;
                }
                if (!flag2) {
                    winPlayer = WinPlayer.PLAYER1;
                    System.out.println("The first player has won");
                    break ;
                }
                for (int i = 0; i < 8; i++) {
                    if (desk[0][i] == 2) {
                        winPlayer = WinPlayer.PLAYER2;
                        System.out.println("The second player has won");
                        break a;
                    }
                    if (desk[7][i] == 1) {
                        winPlayer = WinPlayer.PLAYER1;
                        System.out.println("The first player has won");
                        break a;
                    }
                }

                //change the desk and write new desk to jsonDesk
                ObjectMapper mapper = new ObjectMapper();
                String jsonDesk = mapper.writeValueAsString(desk);
                //send jsonDesk to another client
                for (ServerClients vr : Connection.serverList) {
                    if (vr.socket.equals(this.socket)) {
                        continue;
                    }
                    vr.send(jsonDesk);
                }
                File file = new File("countMoving.json");

                importingSaveState(file);


            }
            //send to clients message about winning
            for (ServerClients vr : Connection.serverList) {

                if (winPlayer == WinPlayer.PLAYER2) {
                    vr.send("The second player has won");
                }
                if (winPlayer == WinPlayer.PLAYER1) {
                    vr.send("The first player has won");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importingSaveState(File file) throws IOException {
        ObjectMapper mapper1 = new ObjectMapper();
        if (file.exists()) {
            CountMoving countMoving = mapper1.readValue(file, CountMoving.class);
            countMoving.setCountMoving(countMoving.getCountMoving() + 1);
            mapper1.writeValue(file, countMoving);
        } else {
            CountMoving countMoving = new CountMoving();
            countMoving.setCountMoving(1);
            mapper1.writeValue(file, countMoving);
        }
    }
    private void send(String json) {
        try {
            //write json to output stream
            out.write(json + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }
}

