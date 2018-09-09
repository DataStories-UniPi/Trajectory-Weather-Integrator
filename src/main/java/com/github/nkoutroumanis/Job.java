package com.github.nkoutroumanis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Job {

    public static int hits = 0;
    public static int numberofRows = 0;

    public static void main(String args[]) throws InterruptedException {

        try {
            Stream<String> stream = Files.lines(Paths.get("./variables/weather-variables.txt"));

            WeatherIntegrator.newWeatherIntegrator("/Users/nicholaskoutroumanis/Desktop/csv",
                    "/Users/nicholaskoutroumanis/Desktop/folder/", "/Users/nicholaskoutroumanis/Desktop/grib_files", 3,
                    8, 7, "yyyy-MM-dd HH:mm:ss",
                    /*Arrays.asList("Temperature_isobaric")*/stream.collect(Collectors.toList()))
                    .clearExportingFiles().useIndex().lruCacheMaxEntries(15).build().IntegrateData();

            Runtime rt = Runtime.getRuntime();
            System.out.println("Approximation of used Memory: " + (rt.totalMemory() - rt.freeMemory()) / 1000000 + " MB");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("hits: " + hits);
        System.out.println("numberofRows: " + numberofRows);
        System.out.println("hits/numberofRows: " + hits / numberofRows);


    }

}
