package ch.astina.console.input;

import ch.astina.console.error.InvalidArgumentException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArrayInput extends AbstractInput
{
    private Map<String, String> parameters;

    public ArrayInput()
    {
        this(new HashMap<String, String>());
    }

    public ArrayInput(String... nameValues)
    {
        parameters = new LinkedHashMap<>();
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
    }

    public ArrayInput(Map<String, String> parameters)
    {
        super();
        this.parameters = parameters;
    }

    public ArrayInput(Map<String, String> parameters, InputDefinition definition)
    {
        super(definition);
        this.parameters = parameters;
    }

    @Override
    protected void parse()
    {
        String key, value;
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            key = parameter.getKey();
            value = parameter.getValue();
            if (key.startsWith("--")) {
                addLongOption(key.substring(2), value);
            } else if (parameter.getKey().startsWith("-")) {
                addShortOption(key.substring(1), value);
            } else {
                addArgument(key, value);
            }
        }
    }

    private void addShortOption(String shortcut, String value)
    {
        if (!definition.hasShortcut(shortcut)) {
            throw new InvalidArgumentException(String.format("The '-%s' option does not exist.", shortcut));
        }

        addLongOption(definition.getOptionForShortcut(shortcut).getName(), value);
    }

    private void addLongOption(String name, String value)
    {
        if (!definition.hasOption(name)) {
            throw new InvalidArgumentException(String.format("The '--%s' option does not exist.", name));
        }

        InputOption option = definition.getOption(name);

        if (value == null) {
            if (option.isValueRequired()) {
                throw new InvalidArgumentException(String.format("The '--%s' option requires a value.", name));
            }

            value = option.isValueOptional() ? option.getDefaultValue() : "true";
        }

        options.put(name, value);
    }

    private void addArgument(String name, String value)
    {
        if (!definition.hasArgument(name)) {
            throw new InvalidArgumentException(String.format("The '%s' argument does not exist.", name));
        }

        arguments.put(name, value);
    }

    @Override
    public String getFirstArgument()
    {
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            if (parameter.getKey().startsWith("-")) {
                continue;
            }
            return parameter.getValue();
        }

        return null;
    }

    @Override
    public boolean hasParameterOption(String... values)
    {
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            for (String value : values) {
                if (parameter.getKey().equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getParameterOption(String value)
    {
        return getParameterOption(value, null);
    }

    @Override
    public String getParameterOption(String value, String defaultValue)
    {
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            if (parameter.getKey().equals(value)) {
                return parameter.getValue();
            }
        }

        return defaultValue;
    }
}
