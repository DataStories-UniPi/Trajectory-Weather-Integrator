package com.github.nkoutroumanis;

import com.github.nkoutroumanis.weatherIntegrator.grib.GribFilesTree;
import com.github.nkoutroumanis.weatherIntegrator.lru.LRUCache;
import com.github.nkoutroumanis.weatherIntegrator.lru.LRUCacheManager;
import com.github.nkoutroumanis.weatherIntegrator.WeatherIntegrator;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@State(Scope.Benchmark)
public class BenchmarksTest {

    private final static String filesPath = "/Users/nicholaskoutroumanis/Desktop/csv";
    private final static String filesExportPath = "/Users/nicholaskoutroumanis/Desktop/folder";
    private final static String gribFilesPath = "/Users/nicholaskoutroumanis/Desktop/grib_files";
    private final static String gribFilesExtension = ".grb2";


    private List<String> variables;

    {
        try {
            variables = Files.lines(Paths.get("variables/weather-variables.txt")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final WeatherIntegrator wiWithIndex = WeatherIntegrator.newWeatherIntegrator(filesPath,
            filesExportPath, gribFilesPath, 3,
            8, 7, "yyyy-MM-dd HH:mm:ss",
            variables)
            .clearExportingFiles().lruCacheMaxEntries(1).useIndex().build();

    private final WeatherIntegrator wiWithoutIndex = WeatherIntegrator.newWeatherIntegrator(filesPath,
            filesExportPath, gribFilesPath, 3,
            8, 7, "yyyy-MM-dd HH:mm:ss",
            variables)
            .clearExportingFiles().lruCacheMaxEntries(1).build();

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MINUTES)
    public void integrateDataUsingIndex() {
        wiWithIndex.integrateData();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.HOURS)
    public void integrateDataWithoutIndex() {
        wiWithoutIndex.integrateData();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public LRUCacheManager preProcessing() {
        return LRUCacheManager.newLRUCacheManager(GribFilesTree.newGribFilesTree(gribFilesPath, gribFilesExtension),
                LRUCache.newLRUCache(4), true, Collections.unmodifiableList(variables), ";");
    }

}
