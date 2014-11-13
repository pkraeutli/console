package ch.astina.console;

import ch.astina.console.command.Command;
import ch.astina.console.input.ArrayInput;
import ch.astina.console.input.Input;
import ch.astina.console.output.Output;
import ch.astina.console.output.StreamOutput;
import ch.astina.console.output.Verbosity;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CommandTester
{
    private Command command;
    private Input input;
    private StreamOutput output;
    private int statusCode;

    public CommandTester(Command command)
    {
        this.command = command;
    }

    public int execute(Map<String, String> parameters)
    {
        return execute(parameters, null, null, null);
    }

    public int execute(Map<String, String> parameters, Boolean interactive, Boolean decorated, Verbosity verbosity)
    {
        if (!parameters.containsKey("command")
                && command.getApplication() != null
                && command.getApplication().getDefinition().hasArgument("command")) {
            parameters.put("command", command.getName());
        }

        input = new ArrayInput(parameters);
        if (interactive != null) {
            input.setInteractive(interactive);
        }

        output = new StreamOutput(new ByteArrayOutputStream());
        if (decorated != null) {
            output.setDecorated(decorated);
        }
        if (verbosity != null) {
            output.setVerbosity(verbosity);
        }

        statusCode = command.run(input, output);

        return statusCode;
    }

    public String getDisplay()
    {
        String display;
        try {
            display = ((ByteArrayOutputStream) output.getStream()).toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return display;
    }

    public Input getInput()
    {
        return input;
    }

    public Output getOutput()
    {
        return output;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
