package ch.astina.console.output;

public interface Output
{
    public static final int VERBOSITY_QUIET = 0;
    public static final int VERBOSITY_NORMAL = 1;
    public static final int VERBOSITY_VERBOSE = 2;
    public static final int VERBOSITY_VERY_VERBOSE = 3;
    public static final int VERBOSITY_DEBUG = 4;

    public static final int OUTPUT_NORMAL = 0;
    public static final int OUTPUT_RAW = 1;
    public static final int OUTPUT_PLAIN = 2;

    public void write(String message);

    public void write(String message, boolean newline);

    public void write(String message, boolean newline, String type);

    public void writeln(String message);

    public void writeln(String message, String type);

    public void setVerbosity(boolean level);

    public int getVerbosity();

    public void setDecorated(boolean decorated);

    public boolean isDecorated();

    public void setFormatter(OutputFormatter formatter);

    public OutputFormatter getFormatter();
}
