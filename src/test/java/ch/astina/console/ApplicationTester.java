package ch.astina.console;

import ch.astina.console.input.ArrayInput;
import ch.astina.console.input.Input;
import ch.astina.console.output.Output;
import ch.astina.console.output.StreamOutput;
import ch.astina.console.output.Verbosity;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApplicationTester
{
    private Application application;
    private Input input;
    private StreamOutput output;
    private int statusCode;

    public ApplicationTester(Application application)
    {
        this.application = application;
    }

    public int run(Map<String, String> parameters)
    {
        return run(parameters, null, null, null);
    }

    public int run(String... nameValues)
    {
        Map<String, String> parameters = new LinkedHashMap<>();
        String name = null, value;
        for (String nameOrValue : nameValues) {
            if (name == null) {
                name = nameOrValue;
            } else {
                value = nameOrValue;
                parameters.put(name, value);
                name = null;
            }
        }

        return run(parameters);
    }

    public int run(Map<String, String> parameters, Boolean interactive, Boolean decorated, Verbosity verbosity)
    {
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

        statusCode = application.run(input, output);

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
