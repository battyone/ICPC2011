import java.io.*;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class ICPCRunner {
  /**
   * Asserts that when the main method is run in the specified class, sending
   * the contents of the specified input to the console, the result has the form
   * "Case 1: cases[1]\nCase 2: cases[2]\n", and so on, where the cases are
   * interpreted as regexes.
   */
  public static void assertMatches(Class<?> icpcClass, String input,
      Object... cases) {
    assertMatches(runOnInput(icpcClass, input), cases);
  }

  public static void assertMatches(String output, Object... cases) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < cases.length; i++) {
      builder.append("Case ").append(i + 1).append(": (?:").append(cases[i])
        .append(")\n");
    }

    if (!Pattern.matches(builder.toString(), output))
      throw new AssertionError(output);
  }

  public static String runOnInput(Class<?> icpcClass, String input) {
    try {
      Method main = icpcClass.getMethod("main", String[].class);
      InputStream in = System.in;
      PrintStream out0 = System.out;
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PrintStream printstream = new PrintStream(out);
      System.setOut(printstream);
      main.invoke(null, new Object[]{new String[0]});
      printstream.flush();
      String output = new String(out.toByteArray());
      System.setIn(in);
      System.setOut(out0);
      return output;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
