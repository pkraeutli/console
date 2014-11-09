package ch.astina.console.command;

import ch.astina.console.Application;
import ch.astina.console.InvalidArgumentException;
import ch.astina.console.LogicException;
import ch.astina.console.input.Input;
import ch.astina.console.input.InputDefinition;
import ch.astina.console.output.Output;

public abstract class Command
{
    private Application application;
    private String name;
    private InputDefinition definition;
    private String help;
    private String description;
    private boolean ignoreValidationErrors = false;
    private boolean applicationDefinitionMerged = false;
    private String synopsis;

    public Command()
    {
    }

    public Command(String name)
    {
        definition = new InputDefinition();

        setName(name);

        configure();

        if (name == null || name.isEmpty()) {
            throw new LogicException(String.format("The command defined in '%s' cannot have an empty name.", getClass()));
        }
    }

    public void ignoreValidationErrors()
    {
        ignoreValidationErrors = true;
    }

    public Application getApplication()
    {
        return application;
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

    protected void mergeApplicationDefinition()
    {
        // todo implement
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

    private String getSynopsis()
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
