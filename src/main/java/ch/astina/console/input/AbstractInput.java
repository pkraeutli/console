package ch.astina.console.input;

import ch.astina.console.InvalidArgumentException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInput implements Input
{
    protected InputDefinition definition;
    protected Map<String, String> options;
    protected Map<String, String> arguments;
    protected boolean interactive = true;

    public AbstractInput()
    {
        this.definition = new InputDefinition();
    }

    public AbstractInput(InputDefinition definition)
    {
        bind(definition);
        validate();
    }

    @Override
    public void bind(InputDefinition definition)
    {
        this.arguments = new HashMap<String, String>();
        this.options = new HashMap<String, String>();
        this.definition = definition;

        parse();
    }

    protected abstract void parse();

    @Override
    public void validate()
    {
        if (arguments.size() < definition.getArgumentRequiredCount()) {
            throw new RuntimeException("Not enough arguments");
        }
    }

    @Override
    public boolean isInteractive()
    {
        return interactive;
    }

    @Override
    public void setInteractive(boolean interactive)
    {
        this.interactive = interactive;
    }

    @Override
    public String[] getArguments()
    {
        Map<String, String> argumentValues = definition.getArgumentDefaults();

        for (Map.Entry<String, String> argument : arguments.entrySet()) {
            argumentValues.put(argument.getKey(), argument.getValue());
        }

        return (String[]) argumentValues.values().toArray();
    }

    @Override
    public String getArgument(String name)
    {
        if (!definition.hasArgument(name)) {
            throw new InvalidArgumentException(String.format("The '%s' argument does not exist.", name));
        }

        if (arguments.containsKey(name)) {
            return arguments.get(name);
        }

        return definition.getArgument(name).getDefaultValue();
    }

    @Override
    public void setArgument(String name, String value) throws InvalidArgumentException
    {
        if (!definition.hasArgument(name)) {
            throw new InvalidArgumentException(String.format("The '%s' argument does not exist.", name));
        }

        arguments.put(name, value);
    }

    @Override
    public boolean hasArgument(String name)
    {
        return definition.hasArgument(name);
    }

    @Override
    public String[] getOptions()
    {
        Map<String, String> optionValues = definition.getOptionDefaults();

        for (Map.Entry<String, String> option : options.entrySet()) {
            optionValues.put(option.getKey(), option.getValue());
        }

        return (String[]) optionValues.values().toArray();
    }

    @Override
    public String getOption(String name)
    {
        if (!definition.hasOption(name)) {
            throw new InvalidArgumentException(String.format("The '%s' option does not exist.", name));
        }

        if (options.containsKey(name)) {
            return options.get(name);
        }

        return definition.getOption(name).getDefaultValue();
    }

    @Override
    public void setOption(String name, String value) throws InvalidArgumentException
    {
        if (!definition.hasOption(name)) {
            throw new InvalidArgumentException(String.format("The '%s' option does not exist.", name));
        }

        options.put(name, value);
    }

    @Override
    public boolean hasOption(String name)
    {
        return definition.hasOption(name);
    }
}
