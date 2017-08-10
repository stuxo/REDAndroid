package ch.redacted.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ch.redacted.data.model.Profile;

/**
 * Created by hfatih on 1/6/2017.
 */

public class Calculator {

    public static String getBuffer(Profile profile) {
        //TODO this ratio value should be dynamic
        double minRatio = 0.65;
        double dl = bytesToGibiBytesDouble(profile.response.stats.downloaded);
        double ul = bytesToGibiBytesDouble(profile.response.stats.uploaded);

        double maxDl = ul / minRatio;
        double buffer = maxDl - dl;
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(buffer);
    }

    public static String getMaxPossibleBuffer(Profile profile) {
        double minRatio = profile.response.stats.requiredRatio;
        double dl = bytesToGibiBytesDouble(profile.response.stats.downloaded);
        double ul = bytesToGibiBytesDouble(profile.response.stats.uploaded);

        double maxDl = ul / minRatio;
        double buffer = maxDl - dl;
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(buffer);
    }

    public static double bytesToGibiBytesDouble(long bytes) {
        bytes = bytes / 1073741824;
        return bytes;
    }

    /**
     * Converts a long to a String with a variable number of decimal points
     * @param bytes
     * @return String
     */
    public static String toHumanReadableSize(long bytes, int decimalPoints) {
        boolean si = false;
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%." + decimalPoints + "f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
