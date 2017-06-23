package ch.redacted.util;

/**
 * Created by sxo on 19/01/17.
 */

public class ReleaseTypes {

    public static String getReleaseType(int releaseId) {

        switch (releaseId) {
            case 1:
                return "Album";
            case 3:
                return "Soundtrack";
            case 5:
                return "EP";
            case 6:
                return "Remixed By";
            case 7:
                return "Compilation";
            case 9:
                return "Single";
            case 11:
               return "Live Album";
            case 13:
                return "Remix";
            case 14:
                return "Bootleg";
            case 15:
                return "Interview";
            case 16:
                return "Mixtape";
            case 17:
                return "Demo";
            case 18:
                return "Concert Recording";
            case 19:
                return "DJ Mix";
            default:
                return "Unknown Release Type";
        }
    }
}
