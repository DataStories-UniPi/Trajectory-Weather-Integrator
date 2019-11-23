Weather Integrator
=========
[![Travis CI](https://travis-ci.org/nkoutroumanis/Weather-Integrator.svg?branch=master)](https://travis-ci.org/nkoutroumanis/Weather-Integrator)<br/>

Data integrator implementation in java for integrating weather information datasets composed of spatio-temporal points.

This work provides a mechanism for enriching spatio-temporal point records (records that contain longitude, latitude and date information) with weather data;
attributes that describe characteristics of the prevailing weather conditions, such as temperature. The weather data can be found in GRIB files that store the values 
of weather attributes in serialized form and refer to a specific time period and to a specific geographical area (or the whole globe). 

Features                                                                                                                        
-                                                                                                                               
* The records of spatio-temporal points (dataset) that are to be enriched may exist in files (such as CSV) or in a Kafka topic  
* The records of spatio-temporal points (dataset) that are to be enriched may be either in JSON or in delimiter separated format
* The output result (enriched records) may be written either in files or in Kafka topic                                         
* The weather data source (GRIB files) can be accessed either locally or via HDFS during the integration procedure              

What are GRIB files and where can I find them? 
-                                              
GRIB is a standardized binary data format by the World Meteorological Organization's Commission for Basic Systems, commonly used in meteorology to store historical and forecast weather data.
Publicly GRIB files with forecast weather data are offered by [NoAA](https://www.ncdc.noaa.gov/data-access/model-data/model-datasets/global-forcast-system-gfs) and [ECMWF](https://apps.ecmwf.int/datasets/) and are freely available for download.
Other organizations may also distribute for free weather data sources in the form of GRIB files.

What GRIB files should I choose for enriching my dataset with weather data?
-                                             
When choosing the GRIB files as the weather data source for the enrichment procedure, it is preferable to opt for those with the lowest grid scale for better accuracy.
Also, since 4 models are run per day each one starting at 00:00, 06:00, 12:00 and 18:00, prefer the files for each model with steps 000 or 003 or 006.

The step number of a grib file declares the hours ahead from the starting time of the model. The contained weather information is referring to that time ahead. 
GRIB files with steps > 000, contain both instantaneous weather attributes and average variables for some passed time. For instance, a GRIB file with step 006 
contain weather attributes that refer to 6-hours ahead from the starting time of the model and 6-hour average attributes that refer to the passed time period. 

_Important Note:_ be sure to download the GRIB files that will cover the spanning time period of the spatio-temporal points of your dataset! 
 
How can I see the available weather attributes of a GRIB file so as to choose what information to integrate?
-                                                                          
+++
 

Getting started
-                                               
Having downloaded the GRIB files, you are almost a step before starting using the weather data integrator. 
All that is left, is cloning the repository and executing some lifecycle stages in Maven. 

```
$ git clone https://github.com/nkoutroumanis/Weather-Integrator
$ cd  Weather-Integrator/
$ mvn install
$ +++
```

**Weather Integrator out of the box**

+++
              
**Weather Integrator API usage**

+++






























































































































































