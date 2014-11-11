package ch.astina.console.output;

import ch.astina.console.formatter.OutputFormatter;

public class SystemOutput extends StreamOutput implements ConsoleOutput
{
    private Output stderr;

    public SystemOutput()
    {
        super(System.out);
        initialize();
    }

    public SystemOutput(Verbosity verbosity)
    {
        super(System.out, verbosity);
        initialize();
    }

    public SystemOutput(Verbosity verbosity, boolean decorated)
    {
        super(System.out, verbosity, decorated);
    }

    public SystemOutput(Verbosity verbosity, boolean decorated, OutputFormatter formatter)
    {
        super(System.out, verbosity, decorated, formatter);
    }

    private void initialize()
    {
        this.stderr = new StreamOutput(System.err);
    }

    @Override
    public void setDecorated(boolean decorated)
    {
        super.setDecorated(decorated);
        stderr.setDecorated(decorated);
    }

    @Override
    public void setFormatter(OutputFormatter formatter)
    {
        super.setFormatter(formatter);
        stderr.setFormatter(formatter);
    }

    @Override
    public void setVerbosity(Verbosity verbosity)
    {
        super.setVerbosity(verbosity);
        stderr.setVerbosity(verbosity);
    }

    @Override
    public Output getErrorOutput()
    {
        return stderr;
    }

    @Override
    public void setErrorOutput(Output error)
    {
        stderr = error;
    }
}
