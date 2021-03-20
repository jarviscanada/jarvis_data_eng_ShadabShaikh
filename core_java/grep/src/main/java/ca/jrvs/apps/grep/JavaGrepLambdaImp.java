package ca.jrvs.apps.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{

  @Override
  public List<File> listFiles(String rootDir) {
    //Setup files and directories arraylists
    try {
    Stream<File> filenames = Files.walk(Paths.get(rootDir))
        .filter(Files::isRegularFile)
        .map(Path::toFile);

      return filenames.collect(Collectors.toList());

    } catch (IOException e) {
      logger.error("IOException", e);
      throw new IllegalArgumentException("IOException");
    }
  }

  @Override
  public List<String> readLines(File inputFile) throws FileNotFoundException {
    List<String> list = new ArrayList<>();
    try
        (Stream<String> stream = Files.lines(inputFile.toPath())) {
          list = stream.collect(Collectors.toList());
      }
     catch (FileNotFoundException e) {
      logger.error("FileNotFoundException", e);
    } catch (IOException e) {
      logger.error("IOException", e);
    }
    return list;
  }

  public static void main(String[] args){
    if (args.length !=3 ){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    //Initialize arguments as variables
    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error(ex.getMessage(), ex);

    }

  }

}
