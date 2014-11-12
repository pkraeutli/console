package ch.astina.console.command;

import ch.astina.console.Application;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;
import ch.astina.console.helper.Helper;
import ch.astina.console.helper.HelperSet;
import ch.astina.console.input.Input;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputDefinition;
import ch.astina.console.input.InputOption;
import ch.astina.console.output.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Command
{
    private Application application;
    private String name;
    private InputDefinition definition;
    private String help;
    private String description;
    private boolean ignoreValidationErrors = false;
    private boolean applicationDefinitionMerged = false;
    private boolean applicationDefinitionMergedWithArgs = false;
    private String synopsis;
    private HelperSet helperSet;

    public Command()
    {
        this(null);
    }

    public Command(String name)
    {
        definition = new InputDefinition();

        if (name != null) {
            setName(name);
        }

        configure();

        if (this.name == null || this.name.isEmpty()) {
            throw new LogicException(String.format("The command defined in '%s' cannot have an empty name.", getClass()));
        }
    }

    public void ignoreValidationErrors()
    {
        ignoreValidationErrors = true;
    }

    public void setApplication(Application application)
    {
        this.application = application;
        if (application == null) {
            setHelperSet(null);
        } else {
            setHelperSet(application.getHelperSet());
        }
    }

    public Application getApplication()
    {
        return application;
    }

    public HelperSet getHelperSet()
    {
        return helperSet;
    }

    public void setHelperSet(HelperSet helperSet)
    {
        this.helperSet = helperSet;
    }

    public Helper getHelper(String name)
    {
        return helperSet.get(name);
    }

    /**
     * Checks whether the command is enabled or not in the current environment
     *
     * Override this to check for x or y and return false if the command can not
     * run properly under the current conditions.
     */
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * Configures the current command.
     */
    protected void configure()
    {
    }

    /**
     * Executes the current command.
     */
    protected abstract int execute(Input input, Output output);

    /**
     * Interacts with the user.
     */
    protected void interact(Input input, Output output)
    {
    }

    /**
     * Initializes the command just after the input has been validated.
     *
     * This is mainly useful when a lot of commands extends one main command
     * where some things need to be initialized based on the input arguments and options.
     */
    protected void initialize(Input input, Output output)
    {
    }

    /**
     * Runs the command.
     */
    public int run(Input input, Output output)
    {
        // force the creation of the synopsis before the merge with the app definition
        getSynopsis();

        mergeApplicationDefinition();

        try {
            input.bind(definition);
        } catch (RuntimeException e) {
            if (!ignoreValidationErrors) {
                throw e;
            }
        }

        initialize(input, output);

        if (input.isInteractive()) {
            interact(input, output);
        }

        input.validate();

        return execute(input, output);
    }

    public void mergeApplicationDefinition()
    {
        mergeApplicationDefinition(true);
    }

    public void mergeApplicationDefinition(boolean mergeArgs)
    {
        if (application == null || (applicationDefinitionMerged && (applicationDefinitionMergedWithArgs || !mergeArgs))) {
            return;
        }

        if (mergeArgs) {
            Collection<InputArgument> currentArguments = definition.getArguments();
            definition.setArguments(application.getDefinition().getArguments());
            definition.addArguments(currentArguments);
        }

        definition.addOptions(application.getDefinition().getOptions());

        applicationDefinitionMerged = true;
        if (mergeArgs) {
            applicationDefinitionMergedWithArgs = true;
        }
    }

    public Command setDefinition(InputDefinition definition)
    {
        this.definition = definition;

        applicationDefinitionMerged = false;

        return this;
    }

    public InputDefinition getDefinition()
    {
        return definition;
    }

    public InputDefinition getNativeDefinition()
    {
        return getDefinition();
    }

    public Command addArgument(String name)
    {
        definition.addArgument(new InputArgument(name));

        return this;
    }

    public Command addArgument(String name, int mode)
    {
        definition.addArgument(new InputArgument(name, mode));

        return this;
    }

    public Command addArgument(String name, int mode, String description)
    {
        definition.addArgument(new InputArgument(name, mode, description));

        return this;
    }

    public Command addArgument(String name, int mode, String description, String defaultValue)
    {
        definition.addArgument(new InputArgument(name, mode, description, defaultValue));

        return this;
    }

    public Command addOption(String name)
    {
        definition.addOption(new InputOption(name));

        return this;
    }

    public Command addOption(String name, String shortcut)
    {
        definition.addOption(new InputOption(name, shortcut));

        return this;
    }

    public Command addOption(String name, String shortcut, int mode)
    {
        definition.addOption(new InputOption(name, shortcut, mode));

        return this;
    }

    public Command addOption(String name, String shortcut, int mode, String description)
    {
        definition.addOption(new InputOption(name, shortcut, mode, description));

        return this;
    }

    public Command addOption(String name, String shortcut, int mode, String description, String defaultValue)
    {
        definition.addOption(new InputOption(name, shortcut, mode, description, defaultValue));

        return this;
    }

    public Command setName(String name)
    {
        validateName(name);

        this.name = name;

        return this;
    }

    public String getName()
    {
        return name;
    }

    public Command setDescription(String description)
    {
        this.description = description;

        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public Command setHelp(String help)
    {
        this.help = help;

        return this;
    }

    public String getHelp()
    {
        return help;
    }

    public String getProcessedHelp()
    {
        String help = getHelp();
        if (help == null) {
            return "";
        }

        help = help.replaceAll("%command.name%", getName());

        return help;
    }

    public String getSynopsis()
    {
        if (synopsis == null) {
            synopsis = String.format("%s %s", name, definition.getSynopsis()).trim();
        }

        return synopsis;
    }

    private void validateName(String name)
    {
        if (!name.matches("^[^\\:]++(\\:[^\\:]++)*$")) {
            throw new InvalidArgumentException(String.format("Command name '%s' is invalid.", name));
        }
    }
}
