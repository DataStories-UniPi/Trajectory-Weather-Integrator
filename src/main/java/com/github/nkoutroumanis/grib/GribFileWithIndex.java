package com.github.nkoutroumanis.grib;

import com.github.nkoutroumanis.ParellelJob;
import ucar.ma2.Array;
import ucar.ma2.Index;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GribFileWithIndex {

    private GribFile gribFile;
    private final List<Map.Entry<Array,Index>> listOfEntries;


    private GribFileWithIndex(String path, List<String> listOfVariables) throws IOException {
        gribFile = GribFile.newGribFile(path, listOfVariables);

        listOfEntries = gribFile.getVariablesAsStream().map(v->{
            Array array = null;
            try {
                array = v.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new AbstractMap.SimpleEntry<>(array,array.getIndex());
        }).collect(Collectors.toList());
    }

    public static GribFileWithIndex newGribFileWithIndex(String path, List<String> listOfVariables){
        return newGribFileWithIndex(path, listOfVariables);
    }

    public String getDataValuesByLatLon(float lat, float lon){
        StringBuilder s = new StringBuilder();

        listOfEntries.forEach(e->{
                s.append(ParellelJob.SEPARATOR+" ");
                s.append(e.getKey().getFloat(e.getValue().set(0, 0, GribFile.getLatIndex(lat), GribFile.getLonIndex(lon))));
        });
        return s.toString();
    }


}
