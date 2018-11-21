package com.github.nkoutroumanis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public interface FilesParse {

    default void lineParse(String line, String[] separatedLine, int numberOfColumnLongitude, int numberOfColumnLatitude, int numberOfColumnDate, float longitude, float  latitude){

    }

    default void afterLineParse(){

    }

    default void fileParse(Path filePath){

    }

    default void emptySpatiotemporalInformation(){

    }

    default void outOfRangeSpatialInformation(){

    }

    default void parse(String filesPath, String separator, String filesExtension, int numberOfColumnLongitude, int numberOfColumnLatitude, int numberOfColumnDate) {

        try (Stream<Path> stream = Files.walk(Paths.get(filesPath)).filter(path -> path.getFileName().toString().endsWith(filesExtension))) {

            stream.forEach((path) -> {

                fileParse(path);

                try (Stream<String> innerStream = Files.lines(path)) {
                    //for each line
                    innerStream.forEach(line -> {

                        String[] separatedLine = line.split(separator);
                        float longitude = Float.parseFloat(separatedLine[numberOfColumnLongitude - 1]);
                        float latitude = Float.parseFloat(separatedLine[numberOfColumnLatitude - 1]);

                        if(FilesParse.empty.test(separatedLine[numberOfColumnLongitude - 1]) || FilesParse.empty.test(separatedLine[numberOfColumnLatitude - 1]) || FilesParse.empty.test(separatedLine[numberOfColumnDate - 1])){
                            emptySpatiotemporalInformation();
                            return;

                        }
                        else if(FilesParse.longitudeOutOfRange.test(longitude) || FilesParse.latitudeOutOfRange.test(latitude)){
                            outOfRangeSpatialInformation();
                            return;
                        }

                        else {
                            lineParse(line, separatedLine, numberOfColumnLongitude, numberOfColumnLatitude, numberOfColumnDate, longitude,  latitude);
                        }
                    });

                    afterLineParse();

                } catch (IOException ex) {
                    Logger.getLogger(FilesParse.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (
                IOException ex) {
            Logger.getLogger(FilesParse.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static final Predicate<Float> longitudeOutOfRange = (longitude) -> ((Float.compare(longitude, 180) == 1) || (Float.compare(longitude, -180) == -1));
    static final Predicate<Float> latitudeOutOfRange = (latitude) -> ((Float.compare(latitude, 90) == 1) || (Float.compare(latitude, -90) == -1));
    static final Predicate<String> empty = (s1) -> (s1.equals(""));
}