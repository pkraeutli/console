package ch.astina.console;

import ch.astina.console.command.*;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;
import ch.astina.console.helper.HelperSet;
import ch.astina.console.helper.QuestionHelper;
import ch.astina.console.input.*;
import ch.astina.console.output.*;
import ch.astina.console.util.StringUtils;
import ch.astina.console.util.ThrowableUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Application
{
    private Map<String, Command> commands = new HashMap<String, Command>();
    private boolean wantHelps;
    private String name;
    private String version;
    private InputDefinition definition;
    private boolean autoExit = true;
    private String defaultCommand;
    private boolean catchExceptions = true;
    private Command runningCommand;
    private Command[] defaultCommands;
    private int[] terminalDimensions;
    private HelperSet helperSet;

    public Application()
    {
        this("UNKNOWN", "UNKNOWN");
    }

    public Application(String name, String version)
    {
        this.name = name;
        this.version = version;
        this.defaultCommand = "list";
        this.helperSet = getDefaultHelperSet();
        this.definition = getDefaultInputDefinition();

        for (Command command : getDefaultCommands()) {
            add(command);
        }
    }

    public static void main(String[] args)
    {
        Application app = new Application("Astina Console", "1.0.0-SNAPSHOT");
        app.add(new GreetingCommand());
        app.add((new Command("test")).setExecutor(new CommandExecutor()
        {
            @Override
            public int execute(Input input, Output output)
            {
                output.writeln("<info>Prosim!</info>");

                return 0;
            }
        }));
        int exitCode = app.run(args);

        System.exit(exitCode);
    }

    public int run(String[] args)
    {
        return run(new ArgvInput(args), new SystemOutput());
    }

    public int run(Input input, Output output)
    {
        configureIO(input, output);

        int exitCode;

        try {
            exitCode = doRun(input, output);
        } catch (Exception e) {
            if (!catchExceptions) {
                throw new RuntimeException(e);
            }

            if (output instanceof ConsoleOutput) {
                renderException(e, ((ConsoleOutput) output).getErrorOutput());
            } else {
                renderException(e, output);
            }

            exitCode = 1;
        }

        if (autoExit) {
            if (exitCode > 255) {
                exitCode = 255;
            }

            System.exit(exitCode);
        }

        return exitCode;
    }

    private void renderException(Throwable error, Output output)
    {
        String title = String.format("%s  [%s]  ", error.getMessage(), error.getClass());
        output.writeln(title);
        output.writeln("");

        if (output.getVerbosity().ordinal() >= Verbosity.VERBOSE.ordinal()) {
            output.writeln("<comment>Exception trace:</comment>");
            output.writeln(ThrowableUtils.getThrowableAsString(error));
        }
    }

    protected int doRun(Input input, Output output)
    {
        if (input.hasParameterOption("--version", "-V")) {
            output.writeln(getLongVersion());

            return 0;
        }

        String name = getCommandName(input);
        if (input.hasParameterOption("--help", "-h")) {
            if (name == null) {
                name = "help";
                input = new ArrayInput("command", "help");
            } else {
                wantHelps = true;
            }
        }

        if (name == null) {
            name = defaultCommand;
            input = new ArrayInput("command", defaultCommand);
        }

        Command command = find(name);

        runningCommand = command;
        int exitCode = doRunCommand(command, input, output);
        runningCommand = null;

        return exitCode;
    }

    protected int doRunCommand(Command command, Input input, Output output)
    {
        int exitCode;

        try {
            exitCode = command.run(input, output);
        } catch (Exception e) {
            // todo events
            throw new RuntimeException(e);
        }

        return exitCode;
    }

    private void configureIO(Input input, Output output)
    {
        if (input.hasParameterOption("--ansi")) {
            output.setDecorated(true);
        } else if (input.hasParameterOption("--no-ansi")) {
            output.setDecorated(false);
        }

        if (input.hasParameterOption("--no-interaction", "-n")) {
            input.setInteractive(false);
        }
        // todo implement posix isatty support

        if (input.hasParameterOption("--quiet", "-q")) {
            output.setVerbosity(Verbosity.QUIET);
        } else {
            if (input.hasParameterOption("-vvv") || input.hasParameterOption("--verbose=3") || input.getParameterOption("--verbose", "").equals("3")) {
                output.setVerbosity(Verbosity.DEBUG);
            } else if (input.hasParameterOption("-vv") || input.hasParameterOption("--verbose=2") || input.getParameterOption("--verbose", "").equals("2")) {
                output.setVerbosity(Verbosity.VERY_VERBOSE);
            } else if (input.hasParameterOption("-v") || input.hasParameterOption("--verbose=1") || input.getParameterOption("--verbose", "").equals("1")) {
                output.setVerbosity(Verbosity.VERBOSE);
            }
        }
    }

    public void setAutoExit(boolean autoExit)
    {
        this.autoExit = autoExit;
    }

    public void setCatchExceptions(boolean catchExceptions)
    {
        this.catchExceptions = catchExceptions;
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

    public String getHelp()
    {
        String nl = System.getProperty("line.separator");

        StringBuilder sb = new StringBuilder();
        sb
            .append(getLongVersion())
            .append(nl)
            .append(nl)
            .append("<comment>Usage:</comment>")
            .append(nl)
            .append(" [options] command [arguments]")
            .append(nl)
            .append(nl)
            .append("<comment>Options:</comment>")
            .append(nl)
        ;

        for (InputOption option : definition.getOptions()) {
            sb.append(String.format("  %-29s %s %s",
                    "<info>--" + option.getName() + "</info>",
                    option.getShortcut() == null ? "  " : "<info>-" + option.getShortcut() + "</info>",
                    option.getDescription())
            ).append(nl);
        }

        return sb.toString();
    }

    public String getLongVersion()
    {
        if (!getName().equals("UNKNOWN") && !getVersion().equals("UNKNOWN")) {
            return String.format("<info>%s</info> version <comment>%s</comment>", getName(), getVersion());
        }

        return "<info>Console Tool</info>";
    }

    public Command register(String name)
    {
        return add(new Command(name));
    }

    public void addCommands(Command... commands)
    {
        for (Command command : commands) {
            add(command);
        }
    }

    /**
     * Adds a command object.
     *
     * If a command with the same name already exists, it will be overridden.
     */
    public Command add(Command command)
    {
        command.setApplication(this);

        if (!command.isEnabled()) {
            command.setApplication(null);
            return null;
        }

        if (command.getDefinition() == null) {
            throw new LogicException(String.format("Command class '%s' is not correctly initialized. You probably forgot to call the super constructor.", command.getClass()));
        }

        commands.put(command.getName(), command);

        for (String alias : command.getAliases()) {
            commands.put(alias, command);
        }

        return command;
    }

    public Command find(String name)
    {
        return get(name);
    }

    public Map<String, Command> all()
    {
        return commands;
    }

    public Map<String, Command> all(String namespace)
    {
        Map<String, Command> commands = new HashMap<String, Command>();

        for (Command command : this.commands.values()) {
            if (namespace.equals(extractNamespace(command.getName(), StringUtils.count(namespace, ':') + 1))) {
                commands.put(command.getName(), command);
            }
        }

        return commands;
    }

    public String extractNamespace(String name)
    {
        return extractNamespace(name, null);
    }

    public String extractNamespace(String name, Integer limit)
    {
        List<String> parts = new ArrayList<String>(Arrays.asList(name.split(":")));
        parts.remove(parts.size() - 1);

        if (parts.size() == 0) {
            return null;
        }

        if (limit != null && parts.size() > limit) {
            parts = parts.subList(0, limit);
        }

        return StringUtils.join(parts.toArray(new String[parts.size()]), ":");
    }

    public Command get(String name)
    {
        if (!commands.containsKey(name)) {
            throw new InvalidArgumentException(String.format("The command '%s' does not exist.", name));
        }

        Command command = commands.get(name);

        if (wantHelps) {
            wantHelps = false;

            HelpCommand helpCommand = (HelpCommand) get("help");
            helpCommand.setCommand(command);

            return helpCommand;
        }

        return command;
    }

    public boolean has(String name)
    {
        return commands.containsKey(name);
    }

    public String[] getNamespaces()
    {
        Set<String> namespaces = new HashSet<>();

        String namespace;
        for (Command command : commands.values()) {
            namespace = extractNamespace(command.getName());
            if (namespace != null) {
                namespaces.add(namespace);
            }
            for (String alias : command.getAliases()) {
                extractNamespace(alias);
                if (namespace != null) {
                    namespaces.add(namespace);
                }
            }
        }

        return namespaces.toArray(new String[namespaces.size()]);
    }

    protected String getCommandName(Input input)
    {
        return input.getFirstArgument();
    }

    protected static InputDefinition getDefaultInputDefinition()
    {
        InputDefinition definition = new InputDefinition();
        definition.addArgument(new InputArgument("command", InputArgument.REQUIRED, "The command to execute"));
        definition.addOption(new InputOption("--help", "-h", InputOption.VALUE_NONE, "Display this help message."));
        definition.addOption(new InputOption("--quiet", "-q", InputOption.VALUE_NONE, "Do not output any message."));
        definition.addOption(new InputOption("--verbose", "-v|vv|vvv", InputOption.VALUE_NONE, "Increase the verbosity of messages: 1 for normal output, 2 for more verbose output and 3 for debug."));
        definition.addOption(new InputOption("--version", "-V", InputOption.VALUE_NONE, "Display this application version."));
        definition.addOption(new InputOption("--ansi", null, InputOption.VALUE_NONE, "Force ANSI output."));
        definition.addOption(new InputOption("--no-ansi", null, InputOption.VALUE_NONE, "Disable ANSI output."));
        definition.addOption(new InputOption("--no-interaction", "-n", InputOption.VALUE_NONE, "Do not ask any interactive question."));

        return definition;
    }

    public Command[] getDefaultCommands()
    {
        Command[] commands = new Command[2];
        commands[0] = new HelpCommand();
        commands[1] = new ListCommand();

        return commands;
    }

    public HelperSet getHelperSet()
    {
        return helperSet;
    }

    public void setHelperSet(HelperSet helperSet)
    {
        this.helperSet = helperSet;
    }

    protected HelperSet getDefaultHelperSet()
    {
        HelperSet helperSet = new HelperSet();
        helperSet.set(new QuestionHelper());

        return helperSet;
    }

    public int[] getTerminalDimensions()
    {
        if (terminalDimensions != null) {
            return terminalDimensions;
        }

//        String sttyString = getSttyColumns();

        return new int[] {80, 120};
    }

    public String getSttyColumns()
    {
        // todo make this work

        String sttyColumns = null;
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "stty", "-a");
            Process process = builder.start();
            StringBuilder out = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line, previous = null;
            while ((line = br.readLine()) != null) {
                if (!line.equals(previous)) {
                    previous = line;
                    out.append(line).append('\n');
                }
            }
            sttyColumns = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sttyColumns;
    }
}
