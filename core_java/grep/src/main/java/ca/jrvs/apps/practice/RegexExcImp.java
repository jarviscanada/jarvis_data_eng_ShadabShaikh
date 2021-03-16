package ca.jrvs.apps.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

  public static String jpegReg = "([\\w-]+)+(\\.jpg|.jpeg)\\s";
  public static String ipReg = "(?:\\d{1,3}\\.){3}\\d{1,3}";
  public static String isEmptyLine = "^\\s*$";

  public static void main(String[] args) {
    System.out.println("!!!?abckweq???!caicnanckan kcanca");
    String jpegTestString = "thisimage.jpg";
    String jpegTestString2 = "file.csv";
    String ipTestString = "123.45.10.120";
    String ipTestString2 = "123.45.word";
    String emptyTestString = "";
    String emptyTestString2 = "foo";
    RegexExcImp regex = new RegexExcImp();

    System.out.println(jpegTestString + " is");
    if (regex.matchJpeg(jpegTestString)) {
      System.out.println("a jpeg file");
    } else {
      System.out.println("not a jpeg file");
    }
    System.out.println(jpegTestString2 + " is");
    if (regex.matchJpeg(jpegTestString2))
      System.out.println("a jpeg file");
    else {
      System.out.println("not a jpeg file");
    }
    System.out.println(ipTestString + " is");
    if (regex.matchIP(ipTestString))
      System.out.println("a valid IP.");
    else {
      System.out.println("not a valid IP.");
    }
    System.out.println(ipTestString2 + " is");
    if (regex.matchIP(ipTestString2))
      System.out.println("a valid IP.");
    else {
      System.out.println("not a valid IP.");
    }
    System.out.println(emptyTestString + " is");
    if (regex.isEmptyLine(emptyTestString))
      System.out.println("empty.");
    else {
      System.out.println("not empty.");
    }
    System.out.println(emptyTestString2 + " is");
    if (regex.isEmptyLine(emptyTestString2))
      System.out.println("empty.");
    else {
      System.out.println("not empty.?????");
    }
    System.out.println("test");
  }

  @Override
  public boolean matchJpeg(String filename) {
    return regexCheck(jpegReg, filename);
  }

  @Override
  public boolean matchIP(String ip) {
    return regexCheck(ipReg, ip);
  }

  @Override
  public boolean isEmptyLine(String line) {
    return regexCheck(isEmptyLine, line);
  }

  private boolean regexCheck(String regex, String input) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    return m.matches();
  }

}