package ch.astina.console.output;

import ch.astina.console.formatter.OutputFormatter;

import java.io.OutputStream;
import java.io.PrintWriter;

public class StreamOutput extends AbstractOutput
{
    private OutputStream stream;
    private PrintWriter writer;

    public StreamOutput(OutputStream stream)
    {
        super();
        initialize(stream);
    }

    public StreamOutput(OutputStream stream, Verbosity verbosity)
    {
        super(verbosity);
        initialize(stream);
    }

    public StreamOutput(OutputStream stream, Verbosity verbosity, boolean decorated)
    {
        super(verbosity, decorated);
        initialize(stream);
    }

    public StreamOutput(OutputStream stream, Verbosity verbosity, boolean decorated, OutputFormatter formatter)
    {
        super(verbosity, decorated, formatter);
        initialize(stream);
    }

    public OutputStream getStream()
    {
        return stream;
    }

    private void initialize(OutputStream stream)
    {
        this.stream = stream;
        writer = new PrintWriter(stream);
    }

    @Override
    protected void doWrite(String message, boolean newline)
    {
        writer.print(message);

        if (newline) {
            writer.println();
        }

        writer.flush();
    }
}
