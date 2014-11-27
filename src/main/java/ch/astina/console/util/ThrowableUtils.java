package ch.astina.console.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ThrowableUtils
{
    public static String getThrowableAsString(Throwable throwable)
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }
}
