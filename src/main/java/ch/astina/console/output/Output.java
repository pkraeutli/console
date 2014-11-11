package ch.astina.console.output;

import ch.astina.console.formatter.OutputFormatter;

public interface Output
{
    public void write(String message);

    public void write(String message, boolean newline);

    public void write(String message, boolean newline, OutputType type);

    public void writeln(String message);

    public void writeln(String message, OutputType type);

    public void setVerbosity(Verbosity verbosity);

    public Verbosity getVerbosity();

    public void setDecorated(boolean decorated);

    public boolean isDecorated();

    public void setFormatter(OutputFormatter formatter);

    public OutputFormatter getFormatter();
}
