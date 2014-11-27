package ch.astina.console.output;

import ch.astina.console.formatter.DefaultOutputFormatter;
import ch.astina.console.formatter.OutputFormatter;

public class NullOutput implements Output
{
    @Override
    public void write(String message)
    {
        // do nothing
    }

    @Override
    public void write(String message, boolean newline)
    {
        // do nothing
    }

    @Override
    public void write(String message, boolean newline, OutputType type)
    {
        // do nothing
    }

    @Override
    public void writeln(String message)
    {
        // do nothing
    }

    @Override
    public void writeln(String message, OutputType type)
    {
        // do nothing
    }

    @Override
    public void setVerbosity(Verbosity verbosity)
    {
        // do nothing
    }

    @Override
    public Verbosity getVerbosity()
    {
        return Verbosity.QUIET;
    }

    @Override
    public void setDecorated(boolean decorated)
    {
        // do nothing
    }

    @Override
    public boolean isDecorated()
    {
        return false;
    }

    @Override
    public void setFormatter(OutputFormatter formatter)
    {
        // do nothing
    }

    @Override
    public OutputFormatter getFormatter()
    {
        return new DefaultOutputFormatter();
    }

    public boolean isQuiet()
    {
        return true;
    }

    public boolean isVerbose()
    {
        return false;
    }

    public boolean isVeryVerbose()
    {
        return false;
    }

    public boolean isDebug()
    {
        return false;
    }
}
