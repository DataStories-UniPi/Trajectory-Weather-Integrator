package gr.ds.unipi.wi;

import gr.ds.unipi.wi.AppConfig;
import gr.ds.unipi.stpin.Rectangle;
import gr.ds.unipi.stpin.datasources.Datasource;
import gr.ds.unipi.stpin.outputs.FileOutput;
import gr.ds.unipi.stpin.outputs.KafkaOutput;
import gr.ds.unipi.stpin.outputs.Output;
import gr.ds.unipi.stpin.parsers.RecordParser;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherIntegratorJob {

    public static int BUFFERSIZE;
    public static long INFOEVERYN;

    public static void main(String args[]) throws Exception {

        Config conf = ConfigFactory.parseFile(new File(args[0]));

        Config wi = conf.getConfig("wi");
        Config filter = conf.getConfig("filter");

        BUFFERSIZE = wi.getInt("bufferSize");
        INFOEVERYN = wi.getInt("infoEveryN");

            AppConfig appConfig = AppConfig.newAppConfig(args[0]);

            Datasource datasource = appConfig.getDataSource();
            RecordParser recordParser = appConfig.getRecordParser(datasource);
            Output output = appConfig.getOutput();

            Stream<String> stream = Files.lines(Paths.get(wi.getString("variablesPath")));

            WeatherIntegrator.Builder w = WeatherIntegrator.newWeatherIntegrator(recordParser,
                    wi.getString("gribFilesFolderPath"), stream.collect(Collectors.toList()));

            if(wi.getBoolean("filter")){
                w.filter(Rectangle.newRectangle(filter.getDouble("minLon"), filter.getDouble("minLat"), filter.getDouble("maxLon"), filter.getDouble("maxLat")));
            }

            if(wi.getBoolean("removeLastValueFromRecords")){
                w.removeLastValueFromRecords();
            }

            w.lruCacheMaxEntries(wi.getInt("lruCacheMaxEntries")).gribFilesExtension(wi.getString("gribFilesExtension"));

            if(wi.getBoolean("useIndex")){
                w.useIndex();
            }

            if(output instanceof FileOutput){
                w.build().integrateAndOutputToDirectory((FileOutput) output);
            }
            else if(output instanceof KafkaOutput){
                w.build().integrateAndOutputToKafkaTopic((KafkaOutput) output);
            }

            output.close();


    }
}
