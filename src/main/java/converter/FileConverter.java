package converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.Move;

import java.io.IOException;

public class FileConverter {

    //read a Move and get json
    public static String toJSON(Move move) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(move);
    }

    //read a json and get Move
    public static Move toJavaObject(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Move move = mapper.readValue(json, Move.class);
        return move;
    }

    //read a jsonDesk to int[][]
    public static int[][] getDesk(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, int[][].class);
    }
}

