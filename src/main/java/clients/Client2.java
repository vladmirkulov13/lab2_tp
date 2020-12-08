package clients;


import converter.FileConverter;
import server.Coordinates;
import server.Move;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client2 {
    private static Socket clientSocket;
    private static Scanner reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                int winPlayer = 0;
                //make client socket with port number 8081
                clientSocket = new Socket("localhost", 8081);
                //input stream from server
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // initialization first state of desk
                int[][] startDesk = new int[8][8];
                for (int i = 0; i < 8; i++) {
                    startDesk[0][i] = 0;
                    startDesk[1][i] = 1;
                    startDesk[2][i] = 0;
                    startDesk[3][i] = 0;
                    startDesk[4][i] = 0;
                    startDesk[5][i] = 0;
                    startDesk[6][i] = 2;
                    startDesk[7][i] = 0;
                }
                System.out.println("Start desk:");
                for(int i = 0 ; i<8;i++) {
                    if(i==0)
                    {System.out.print("0 1 2 3 4 5 6 7");
                        System.out.println();
                        System.out.println("----------------");}
                    for (int j = 0; j < 8; j++) {
                        System.out.print(startDesk[i][j] + " ");
                        if(j==7)
                            System.out.print("|" + i);
                    }
                    System.out.println();
                }
                System.out.println();
                while (true) {
                    //read from console by Scanner
                    reader = new Scanner(System.in);
                    //output stream from client to server
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    //read json from server abd check it
                    String serverJson = in.readLine();
                    if (serverJson.equals("The first player has won")) {
                        winPlayer = 1;
                        break;
                    }

                    if (serverJson.equals("The second player has won")) {
                        winPlayer = 2;
                        break;
                    }
                    //if there isn't winner
                    int desk[][];
                    //get changed desk from json
                    desk = FileConverter.getDesk(serverJson);
                    for(int i = 0 ; i<8;i++) {
                        if(i==0)
                        {System.out.print("0 1 2 3 4 5 6 7");
                            System.out.println();
                            System.out.println("----------------");}
                        for (int j = 0; j < 8; j++) {

                            System.out.print(desk[i][j] + " ");
                            if(j==7)
                                System.out.print("|" + i);
                        }
                        System.out.println();
                    }
                    ////read from console by Scanner each coordinate
                    System.out.println("Введите ход (from(x,y) to(x,y):");
                    int x1 = reader.nextInt();
                    int y1 = reader.nextInt();
                    int x2 = reader.nextInt();
                    int y2 = reader.nextInt();
                    //make a move with read coordinates and number of player
                    Move move = new Move(new Coordinates(x1, y1), new Coordinates(x2, y2), 2);
                    //make a json file with created move
                    String json = FileConverter.toJSON(move);
                    //convert input move to json and put it in out stream - send to server
                    out.write(json + "\n");
                    out.flush();

                }
                //after break it needs to send message about winner
                if (winPlayer == 1) {
                    System.out.println("The first player has won");
                }
                if (winPlayer == 2) {
                    System.out.println("The second player has won");
                }
            } finally {
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


}
