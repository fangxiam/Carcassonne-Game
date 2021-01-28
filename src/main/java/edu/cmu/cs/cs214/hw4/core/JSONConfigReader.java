package edu.cmu.cs.cs214.hw4.core;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * json reader
 */
public class JSONConfigReader {

    // CHECKSTYLE:OFF
    public static class JSONTile{
        public String code;
        public int count;
        public boolean hasMonastery;
        public String[] segments;
        public boolean isArmed;
        public boolean isJoined;
    }
    public static class JSONDeck{
        public JSONTile[] tiles;
    }
    public static JSONDeck parse(String configFile){

        Gson gson = new Gson();
        try{
            Reader reader = new FileReader(new File(configFile));
            return gson.fromJson(reader,JSONDeck.class);
        } catch(IOException e){
            throw new IllegalArgumentException("Error when reading file: " + configFile, e);
        }
    }
    // CHECKSTYLE:ON
}
