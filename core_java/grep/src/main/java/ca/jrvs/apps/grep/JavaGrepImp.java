package ca.jrvs.apps.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepImp implements JavaGrep{

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  @Override
  public void process() throws IOException {
    try {
      List<String> matchedLines = new ArrayList<>();

      for (File file : listFiles(getRootPath())) {
        for (String line : readLines(file)) {
          if (containsPattern(line)) {
            matchedLines.add(line);
          }
        }
      }
      writeToFile(matchedLines);
    } catch (IOException ex) {
      logger.error("USAGE: JavaGrep regex rootPath outFile");
    }


  }

  @Override
  public List<File> listFiles(String rootDir){
    //Setup files and directories arraylists
    List<File> results = new ArrayList<>();
    Queue<File> dirs = new LinkedList<>();
    dirs.add(new File(rootDir));

    //Recursive check for directories or files. Files are added to the results Arraylist and returned
    while (!dirs.isEmpty()) {
      for (File f : dirs.poll().listFiles()) {
        if(f.isDirectory()) {
          dirs.add(f);
        } else if (f.isFile()) {
          results.add(f);
        }
      }
    }
    return results;



  }

  @Override
  public List<String> readLines(File inputFile) throws FileNotFoundException{
    List<String> lines = new ArrayList<>();
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(inputFile));
      String currentLine = reader.readLine();
      while (currentLine != null) {
        lines.add(currentLine);
        currentLine = reader.readLine();
      }
      reader.close();
    } catch (FileNotFoundException e) {
      logger.error("FileNotFoundException", e);
    } catch (IOException e) {
      logger.error("IOException", e);
    }
    return lines;

  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(getRegex());
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try {
      FileWriter writer = new FileWriter(getOutFile());
      for (String str : lines) {
        writer.write(str + System.lineSeparator());
      }
      writer.close();
    } catch (IOException ex) {
      logger.error("Cannot write to file", ex);
    }
  }

  public static void main(String[] args){
    if (args.length !=3 ){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);



    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error(ex.getMessage(), ex);

    }

  }

}
