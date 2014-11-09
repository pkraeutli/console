package ch.astina.console.input;

import ch.astina.console.InvalidArgumentException;

public interface Input
{
    public String getFirstArgument();

    public boolean hasParameterOption(String... values);

    public String getParameterOption(String value, String defaultValue);

    public void bind(InputDefinition definition);

    public void validate() throws RuntimeException;

    public String[] getArguments();

    public String getArgument(String name);

    public void setArgument(String name, String value) throws InvalidArgumentException;

    public boolean hasArgument(String name);

    public String[] getOptions();

    public String getOption(String name);

    public void setOption(String name, String value) throws InvalidArgumentException;

    public boolean hasOption(String name);

    public boolean isInteractive();

    public void setInteractive(boolean interactive);
}
