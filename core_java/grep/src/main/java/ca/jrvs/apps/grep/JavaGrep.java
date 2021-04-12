package ca.jrvs.apps.grep;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.List;

public interface JavaGrep {
  /**
   * Top level search workflow
   * throw IOException
   * */
  void process() throws IOException;

  /**
   * Traversal a given directory and return all files
   * @param rootDir input directory
   * @return file under the rootDir
   * */
  List<File> listFiles(String rootDir);

  /**
   * read a file and return all lines
   * @param inputFile input file
   * @return lines
   * @throws IllegalArgumentException if inputFile is not a File
   * */
  List<String> readLines(File inputFile) throws FileNotFoundException;

  /**
   * check if the line contains pattern
   * @param line A string
   * */
  boolean containsPattern(String line);

  /**
   * write liens to file
   * @param lines array of line
   * */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);

}