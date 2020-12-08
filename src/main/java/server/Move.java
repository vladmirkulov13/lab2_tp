package server;


public class Move {
    //to make a move we need 'from' x, y coordinates
    //and 'to' x, y coordinates
    //and also the number of player
    Coordinates from;
    Coordinates to;
    Integer player;

    public Move() {
    }

    public Move(Coordinates from, Coordinates to, Integer player) {
        this.from = from;
        this.to = to;
        this.player = player;
    }


    public Coordinates getFrom() {
        return from;
    }

    public void setFrom(Coordinates from) {
        this.from = from;
    }

    public Coordinates getTo() {
        return to;
    }

    public void setTo(Coordinates to) {
        this.to = to;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}


