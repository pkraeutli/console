package ch.astina.console.descriptor;

import ch.astina.console.Application;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.command.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationDescription
{
    public static final String GLOBAL_NAMESPACE = "_global";

    private final Application application;

    private final String namespace;

    private Map<String, List<String>> namespaces;

    private Map<String, Command> commands;

    private Map<String, Command> aliases;

    public ApplicationDescription(Application application)
    {
        this(application, null);
    }

    public ApplicationDescription(Application application, String namespace)
    {
        this.application = application;
        this.namespace = namespace;
    }

    public Map<String, List<String>> getNamespaces()
    {
        if (namespaces == null) {
            inspectApplication();
        }

        return namespaces;
    }

    public Map<String, Command> getCommands()
    {
        if (commands == null) {
            inspectApplication();
        }

        return commands;
    }

    public Command getCommand(String name)
    {
        if (!commands.containsKey(name) && !aliases.containsKey(name)) {
            throw new InvalidArgumentException(String.format("Command %s does not exist.", name));
        }

        if (commands.containsKey(name)) {
            return commands.get(name);
        }

        return aliases.get(name);
    }

    private void inspectApplication()
    {
        commands = new HashMap<String, Command>();
        aliases = new HashMap<String, Command>();
        namespaces = new HashMap<String, List<String>>();

        Map<String, Command> all;
        if (namespace == null) {
            all = application.all();
        } else {
            all = application.all(namespace);
        }

        for (Map.Entry<String, Map<String, Command>> entry : sortCommands(all).entrySet()) {

            String namespace = entry.getKey();
            List<String> names = new ArrayList<String>();

            for (Map.Entry<String, Command> commandEntry : entry.getValue().entrySet()) {
                String name = commandEntry.getKey();
                Command command = commandEntry.getValue();
                if (command.getName() == null || command.getName().isEmpty()) {
                    continue;
                }

                if (command.getName().equals(name)) {
                    commands.put(name, command);
                } else {
                    aliases.put(name, command);
                }

                names.add(name);
            }

            namespaces.put(namespace, names);
        }
    }

    private Map<String, Map<String, Command>> sortCommands(Map<String, Command> commands)
    {
        Map<String, Map<String, Command>> namespacedCommands = new HashMap<String, Map<String, Command>>();

        String key;
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            key = application.extractNamespace(entry.getKey(), 1);
            if (key == null || key.isEmpty()) {
                key = GLOBAL_NAMESPACE;
            }

            if (!namespacedCommands.containsKey(key)) {
                namespacedCommands.put(key, new HashMap<String, Command>());
            }
            namespacedCommands.get(key).put(entry.getKey(), entry.getValue());
        }

        // todo sort

        return namespacedCommands;
    }
}
