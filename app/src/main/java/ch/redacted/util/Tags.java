package ch.redacted.util;

import java.util.Collections;
import java.util.List;

import ch.redacted.data.model.Artist;

/**
 * Created by sxo on 19/01/17.
 */

public class Tags {
    public static String PrettyTags(int numTags, List<String> input){
        String tags = "";
        for (int i = 0; i < numTags; i++) {
            if (input.size() > i){
                tags = tags.concat(input.get(i) + ", ");
            }
        }
        //remove trailing comma
        tags = tags.substring(0, tags.length() - 2);
        return tags;
    }

    public static String PrettyTags(int numTags, String[] input){
        String tags = "";
        for (int i = 0; i < numTags; i++) {
            if (input.length > i){
                tags = tags.concat(input[i] + ", ");
            }
        }
        //remove trailing comma
        tags = tags.substring(0, tags.length() - 2);
        return tags;
    }

    public static String PrettyArtistTags(int numTags, List<Artist.Tags> input) {
        String tags = "";

        Collections.sort(input);

        for (int i = 0; i < numTags; i++) {
            if (input.size() > i){
                tags = tags.concat(input.get(i).name + ", ");
            }
        }
        //remove trailing comma
        tags = tags.substring(0, tags.length() - 2);
        return tags;
    }
}
