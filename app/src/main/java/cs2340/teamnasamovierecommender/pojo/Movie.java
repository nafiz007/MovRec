package cs2340.teamnasamovierecommender.pojo;

/**
 * Created by sai on 4/20/16.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sai on 3/21/16.
 */
public class Movie implements Serializable {
    private String name;
    private int id;
    private String release;
    private String backdrop;
    private ArrayList<HashMap<String, String>> cast;

    public Movie() {
        this.name = "";
        this.id = 0;
        this.release = "";
        this.cast = new ArrayList<>();
    }

    public Movie(String name, int id) {
        this.name = name;
        this.id = id;
        this.release = "????";
        this.cast = new ArrayList<>();
    }

    public Movie(String name, int id, String release) {
        this.name = name;
        this.id = id;
        this.release = release;
        this.cast = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String toString() {
        return getName() + "\n" + getRelease();
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void putInCast(String character, String name) {
        HashMap<String, String> charAct = new HashMap();
        charAct.put(character, name);
        cast.add(charAct);
    }

    public ArrayList<HashMap<String, String>> getCast() {
        return cast;
    }
}
