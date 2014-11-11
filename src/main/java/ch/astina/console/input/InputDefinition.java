package ch.astina.console.input;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;

import java.util.*;

public class InputDefinition
{
    private Map<String, InputArgument> arguments = new LinkedHashMap<String, InputArgument>();

    private int requiredCount;

    private boolean hasAnArrayArgument = false;

    private boolean hasOptional;

    private Map<String, InputOption> options = new HashMap<String, InputOption>();

    private Map<String, String> shortcuts = new HashMap<String, String>();

    public InputDefinition()
    {
        resetArguments();
    }

    private void resetArguments()
    {
        arguments = new LinkedHashMap<String, InputArgument>();
        requiredCount = 0;
        hasAnArrayArgument = false;
        hasOptional = false;
    }

    public void setArguments(Collection<InputArgument> arguments)
    {
        setArguments(new ArrayList<>(arguments));
    }

    public void setArguments(List<InputArgument> arguments)
    {
        resetArguments();
        addArguments(arguments);
    }

    public void addArguments(Collection<InputArgument> arguments)
    {
        addArguments(new ArrayList<>(arguments));
    }

    public void addArguments(List<InputArgument> arguments)
    {
        for (InputArgument argument : arguments) {
            addArgument(argument);
        }
    }

    public void addArgument(InputArgument argument)
    {
        if (arguments.containsKey(argument.getName())) {
            throw new LogicException(String.format("An argument with name '%s' already exists.", argument.getName()));
        }

        if (hasAnArrayArgument) {
            throw new LogicException("Cannot add an argument after an array argument.");
        }

        if (argument.isRequired() && hasOptional) {
            throw new LogicException("Cannot add a required argument after an optional one.");
        }

        if (argument.isArray()) {
            hasAnArrayArgument = true;
        }

        if (argument.isRequired()) {
            ++requiredCount;
        } else {
            hasOptional = true;
        }

        arguments.put(argument.getName(), argument);
    }

    public InputArgument getArgument(String name)
    {
        if (!hasArgument(name)) {
            throw new InvalidArgumentException(String.format("The '%s' argument does not exist.", name));
        }

        return arguments.get(name);
    }

    public InputArgument getArgument(int pos)
    {
        return (InputArgument) arguments.values().toArray()[pos];
    }

    public boolean hasArgument(String name)
    {
        return arguments.containsKey(name);
    }

    public boolean hasArgument(int pos)
    {
        return arguments.size() > pos;
    }

    public Collection<InputArgument> getArguments()
    {
        return arguments.values();
    }

    public int getArgumentCount()
    {
        return hasAnArrayArgument ? Integer.MAX_VALUE : arguments.size();
    }

    public int getArgumentRequiredCount()
    {
        return requiredCount;
    }

    public Map<String, String> getArgumentDefaults()
    {
        HashMap<String, String> defaultValues = new LinkedHashMap<String, String>();
        for (InputArgument argument : arguments.values()) {
            defaultValues.put(argument.getName(), argument.getDefaultValue());
        }

        return defaultValues;
    }

    public void setOptions(Collection<InputOption> options)
    {
        setOptions(new ArrayList<>(options));
    }

    public void setOptions(List<InputOption> options)
    {
        this.options = new HashMap<String, InputOption>();
        this.shortcuts = new HashMap<String, String>();
        addOptions(options);
    }

    public void addOptions(Collection<InputOption> options)
    {
        addOptions(new ArrayList<>(options));
    }

    public void addOptions(List<InputOption> options)
    {
        for (InputOption option : options) {
            addOption(option);
        }
    }

    public void addOption(InputOption option)
    {
        if (options.containsKey(option.getName()) && !option.equals(options.get(option.getName()))) {
            throw new LogicException(String.format("An option named '%s' already exists.", option.getName()));
        }

        if (option.getShortcut() != null) {
            for (String shortcut : option.getShortcut().split("\\|")) {
                if (shortcuts.containsKey(shortcut) && !option.equals(options.get(shortcut))) {
                    throw new LogicException(String.format("An option with shortcut '%s' already exists.", shortcut));
                }
            }
        }

        options.put(option.getName(), option);

        if (option.getShortcut() != null) {
            for (String shortcut : option.getShortcut().split("|")) {
                shortcuts.put(shortcut, option.getName());
            }
        }
    }

    public InputOption getOption(String name)
    {
        if (!hasOption(name)) {
            throw new InvalidArgumentException(String.format("The '--%s' option does not exist.", name));
        }

        return options.get(name);
    }

    public boolean hasOption(String name)
    {
        return options.containsKey(name);
    }

    public Collection<InputOption> getOptions()
    {
        return options.values();
    }

    public boolean hasShortcut(String name)
    {
        return shortcuts.containsKey(name);
    }

    public InputOption getOptionForShortcut(String shortcut)
    {
        return getOption(shortcutToName(shortcut));
    }

    public Map<String, String> getOptionDefaults()
    {
        HashMap<String, String> defaultValues = new HashMap<String, String>();
        for (InputOption option : options.values()) {
            defaultValues.put(option.getName(), option.getDefaultValue());
        }

        return defaultValues;
    }

    private String shortcutToName(String shortcut)
    {
        if (!shortcuts.containsKey(shortcut)) {
            throw new InvalidArgumentException(String.format("The '-%s' option does not exist.", shortcut));
        }

        return shortcuts.get(shortcut);
    }

    public String getSynopsis()
    {
        StringBuilder synopsis = new StringBuilder();

        String shortcut;
        for (InputOption option : options.values()) {
            shortcut = option.getShortcut() == null ? "" : (String.format("-%s|", option.getShortcut()));
            synopsis.append(String.format("[" + (option.isValueRequired() ? "%s--%s='...'" : (option.isValueOptional() ? "%s--%s[='...']" : "%s--%s")) + "] ", shortcut, option.getName()));
        }

        for (InputArgument argument : arguments.values()) {
            synopsis.append(String.format(argument.isRequired() ? "%s " : "[%s] ", argument.getName() + (argument.isArray() ? "1" : "")));
            if (argument.isArray()) {
                synopsis.append(String.format("... [%sN]", argument.getName()));
            }
        }

        return synopsis.toString().trim();
    }

    public String asText()
    {
        // todo implement

        return null;
    }

    public String asXml()
    {
        // todo implement

        return null;
    }
}
