package gr.ds.unipi.wi.grib;

import java.util.List;

public interface GribFile {

    static double roundToHalf(double f) {
        return Math.round(f * 2) / 2.0;
    }

    static int getLatIndex(double fLat) { // lats: 90...-90 per 0.5 (361 values)
        double dLat = roundToHalf(fLat);
        int i = (int) (2 * (90 - dLat));
        return i;
    }

    static int getLonIndex(double fLon) { // lons: 0...359.5, per 0.5  (720 values)
        double dLon = roundToHalf(fLon);
        int i = (int) (2 * dLon);
        if (i < 0) {
            i = 720 + i;
        }
        return i;
    }

    List<Object> getDataValuesByLatLon(double lat, double lon);

}
