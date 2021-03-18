package ca.jrvs.apps.grep;

//import java.util.logging.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    List<String> matchedLines = new ArrayList<>();

    /*matchedLines = []
    for file in listFilesRecursively(rootDir)
    for line in readLines(file)
    if containsPattern(line)
    matchedLines.add(line)
    writeToFile(matchedLines)*/
    for (File file: listFiles(getRootPath())){
      for(String line: readLines(file)){
        if (containsPattern(line)){
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);


  }

  @Override
  public List<File> listFiles(String rootDir){
    List<File> results = new ArrayList<>();


    File[] files = new File(rootDir).listFiles();
    //If this pathname does not denote a directory, then listFiles() returns null.

    assert files != null;
    for (File file : files) {
      if (file.isFile()) {
        results.add(new File(file.getName()));
      }
    }
    return results;



  }

  @Override
  public List<String> readLines(File inputFile) throws FileNotFoundException {
    List<String> lines = new ArrayList<>();
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(inputFile));
      String line1 = reader.readLine();
      while (line1 != null) {
        lines.add(line1);
        line1 = reader.readLine();
      }
      reader.close();
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
    FileWriter writer = new FileWriter(getOutFile());
    for (String str: lines) {
      writer.write(str + System.lineSeparator());
    }
    writer.close();
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
