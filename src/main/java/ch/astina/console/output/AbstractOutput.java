package ch.astina.console.output;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.formatter.DefaultOutputFormatter;
import ch.astina.console.formatter.OutputFormatter;

/**
 * Base class for output classes.
 *
 * There are five levels of verbosity:
 *
 *  * normal: no option passed (normal output)
 *  * verbose: -v (more output)
 *  * very verbose: -vv (highly extended output)
 *  * debug: -vvv (all debug output)
 *  * quiet: -q (no output)
 */
public abstract class AbstractOutput implements Output
{
    private Verbosity verbosity;
    private OutputFormatter formatter;

    public AbstractOutput()
    {
        this(Verbosity.NORMAL);
    }

    public AbstractOutput(Verbosity verbosity)
    {
        this(verbosity, true);
    }

    public AbstractOutput(Verbosity verbosity, boolean decorated)
    {
        this(verbosity, decorated, new DefaultOutputFormatter());
    }

    public AbstractOutput(Verbosity verbosity, boolean decorated, OutputFormatter formatter)
    {
        this.verbosity = verbosity;
        this.formatter = formatter;
        this.formatter.setDecorated(decorated);
    }

    @Override
    public void setFormatter(OutputFormatter formatter)
    {
        this.formatter = formatter;
    }

    @Override
    public OutputFormatter getFormatter()
    {
        return formatter;
    }

    @Override
    public void setDecorated(boolean decorated)
    {
        formatter.setDecorated(decorated);
    }

    @Override
    public boolean isDecorated()
    {
        return formatter.isDecorated();
    }

    @Override
    public void setVerbosity(Verbosity verbosity)
    {
        this.verbosity = verbosity;
    }

    @Override
    public Verbosity getVerbosity()
    {
        return verbosity;
    }

    public boolean isQuiet()
    {
        return verbosity.equals(Verbosity.QUIET);
    }

    public boolean isVerbose()
    {
        return verbosity.ordinal() >= Verbosity.VERBOSE.ordinal();
    }

    public boolean isVeryVerbose()
    {
        return verbosity.ordinal() >= Verbosity.VERY_VERBOSE.ordinal();
    }

    public boolean isDebug()
    {
        return verbosity.ordinal() >= Verbosity.DEBUG.ordinal();
    }

    @Override
    public void write(String message)
    {
        write(message, false);
    }

    @Override
    public void write(String message, boolean newline)
    {
        write(message, newline, OutputType.NORMAL);
    }

    @Override
    public void write(String message, boolean newline, OutputType type)
    {
        if (isQuiet()) {
            return;
        }

        switch (type) {
            case NORMAL:
                message = formatter.format(message);
                break;
            case RAW:
                break;
            case PLAIN:
                // todo strip < > tags
                break;
            default:
                throw new InvalidArgumentException(String.format("Unknown output type given (%s)", type));
        }

        doWrite(message, newline);
    }

    @Override
    public void writeln(String message)
    {
        write(message, true);
    }

    @Override
    public void writeln(String message, OutputType type)
    {
        write(message, true, type);
    }

    /**
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     */
    abstract protected void doWrite(String message, boolean newline);
}
