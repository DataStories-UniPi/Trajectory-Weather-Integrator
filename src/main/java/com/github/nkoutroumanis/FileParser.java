package com.github.nkoutroumanis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class FileParser implements Parser {

    public FileParser(String directoryName, String filesExtension) throws IOException {
        this.directoryName = directoryName;

        if(!directoryName.substring(directoryName.length()-1).equals(File.separator)){
            directoryName = directoryName + File.separator;
        }

        filesStream = Files.walk(Paths.get(directoryName)).filter(path -> path.getFileName().toString().endsWith(filesExtension));
        filesIter = filesStream.iterator();

        Path path = filesIter.next();
        filePath = path.toString();

        linesStream = Files.lines(path);
        linesIter = linesStream.iterator();
    }

    private final String directoryName;
    private Stream<Path> filesStream;
    private Iterator<Path> filesIter;
    private Stream<String> linesStream;
    private Iterator<String> linesIter;

    private String filePath;

    @Override
    public String[] nextLine() {
        return new String[] {linesIter.next(), filePath.substring(directoryName.length())};
        //return new AbstractMap.SimpleEntry(filePath, linesIter.next());
    }

    @Override
    public boolean hasNextLine() throws IOException {
        if (linesIter.hasNext()) {
            return true;
        }
        else {
            linesStream.close();
            if (filesIter.hasNext()) {

                Path path = filesIter.next();
                filePath = path.toString();

                linesStream = Files.lines(path);
                linesIter = linesStream.iterator();
                return true;
            }
            else {
                filePath = null;
                filesStream.close();
            }
        }
        return false;
    }
}
