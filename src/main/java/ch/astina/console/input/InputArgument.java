package ch.astina.console.input;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;

public class InputArgument
{
    public static final int REQUIRED = 1;
    public static final int OPTIONAL = 2;
    public static final int IS_ARRAY = 4;

    private String name;
    private int mode;
    private String description;
    private String defaultValue;

    public InputArgument(String name)
    {
        this(name, OPTIONAL, null, null);
    }

    public InputArgument(String name, int mode)
    {
        this(name, mode, null, null);
    }

    public InputArgument(String name, int mode, String description)
    {
        this(name, mode, description, null);
    }

    public InputArgument(String name, int mode, String description, String defaultValue)
    {
        if (mode > 7 || mode < 1) {
            throw new InvalidArgumentException("Argument mode " + mode + " is not valid.");
        }

        this.name = name;
        this.mode = mode;
        this.description = description;

        setDefaultValue(defaultValue);
    }

    public String getName()
    {
        return name;
    }

    public boolean isRequired()
    {
        return (mode & REQUIRED) == REQUIRED;
    }

    public boolean isArray()
    {
        return (mode & IS_ARRAY) == IS_ARRAY;
    }

    public void setDefaultValue(String defaultValue)
    {
        if (mode == REQUIRED && defaultValue != null) {
            throw new LogicException("Cannot set a default value except for InputArgument.OPTIONAL mode.");
        }

        if (isArray()) {
            // todo implement
        }

        this.defaultValue = defaultValue;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public String getDescription()
    {
        return description;
    }
}
