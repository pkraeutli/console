package ch.astina.console;

import ch.astina.console.command.Command;
import ch.astina.console.input.*;
import ch.astina.console.output.Output;

public class Application
{
    private boolean wantHelps;
    private String name;
    private String version;
    private InputDefinition definition;
    private boolean autoExit = true;
    private String defaultCommand;

    public Application()
    {
        this("UNKNOWN", "UNKNOWN");
    }

    public Application(String name, String version)
    {
        this.name = name;
        this.version = version;
        this.defaultCommand = "list";
        this.definition = getDefaultInputDefinition();


    }

    public static void main(String[] args)
    {
        (new Application()).run(new ArgvInput(args), null);
    }

    public void run(Input input, Output output)
    {
        configureIO(input, output);

        int exitCode;

        try {
            exitCode = doRun(input, output);
        } catch (Exception e) {

        }
    }

    private int doRun(Input input, Output output)
    {
        if (input.hasParameterOption("--version", "-V")) {
            output.writeln(getLongVersion());

            return 0;
        }

        String name = getCommandName(input);
        if (input.hasParameterOption("--help", "-h")) {
            if (name == null) {
                name = "help";
                // todo implement
            } else {
                wantHelps = true;
            }
        }

        if (name == null) {
            name = defaultCommand;
            // todo implement
        }



        return 0;
    }

    private void configureIO(Input input, Output output)
    {
        // todo implement
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public InputDefinition getDefinition()
    {
        return definition;
    }

    public void setDefinition(InputDefinition definition)
    {
        this.definition = definition;
    }

    public String getLongVersion()
    {
        if (!getName().equals("UNKNOWN") && !getVersion().equals("UNKNOWN")) {
            return String.format("<info>%s</info> version <comment>%s</comment>", getName(), getVersion());
        }

        return "<info>Console Tool</info>";
    }

    public Command find(String name)
    {
        return null;
    }

    private String getCommandName(Input input)
    {
        return input.getFirstArgument();
    }

    protected static InputDefinition getDefaultInputDefinition()
    {
        InputDefinition definition = new InputDefinition();
        definition.addArgument(new InputArgument("command", InputArgument.REQUIRED, "The command to execute"));
        definition.addOption(new InputOption("--help", "h", InputOption.VALUE_NONE, "Display this help message."));
        definition.addOption(new InputOption("--quiet", "q", InputOption.VALUE_NONE, "Do not output any message."));
        definition.addOption(new InputOption("--verbose", "-v|vv|vvv", InputOption.VALUE_NONE, "Increase the verbosity of messages: 1 for normal output, 2 for more verbose output and 3 for debug."));
        definition.addOption(new InputOption("--version", "-V", InputOption.VALUE_NONE, "Display this application version."));
        definition.addOption(new InputOption("--ansi", null, InputOption.VALUE_NONE, "Force ANSI output."));
        definition.addOption(new InputOption("--no-ansi", null, InputOption.VALUE_NONE, "Disable ANSI output."));
        definition.addOption(new InputOption("--no-interaction", "-n", InputOption.VALUE_NONE, "Do not ask any interactive question."));

        return definition;
    }
}
