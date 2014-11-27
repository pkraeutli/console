package ch.astina.console.descriptor;

import ch.astina.console.Application;
import ch.astina.console.command.Command;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputDefinition;
import ch.astina.console.input.InputOption;
import ch.astina.console.util.StringUtils;

import java.util.List;
import java.util.Map;

public class MarkdownDescriptor extends AbstractDescriptor
{
    @Override
    protected void describeInputArgument(InputArgument argument, DescriptorOptions options)
    {
        write(
            "**" + argument.getName() + ":**\n\n" +
            "* Name: " + (argument.getName() == null ? "<none>" : argument.getName()) + "\n" +
            "* Is required: " + (argument.isRequired() ? "yes" : "no") + "\n" +
            "* Is array: " + (argument.isArray() ? "yes" : "no") + "\n" +
            "* Description: " + (argument.getDescription() == null ? "<none>" : argument.getDescription()) + "\n" +
            "* Default: `" + (argument.getDefaultValue() == null ? "" : argument.getDefaultValue().replaceAll("\\n", " ")) + "`"
        );
    }

    @Override
    protected void describeInputOption(InputOption option, DescriptorOptions options)
    {
        write(
            "**" + option.getName() + ":**\n\n" +
            "* Name: `--" + option.getName() + "`\n" +
            "* Shortcut: " + (option.getShortcut() == null ? "<none>" : "`-" + StringUtils.join(StringUtils.split(option.getShortcut(), '|'), "|")) + "\n" +
            "* Accept value: " + (option.acceptValue() ? "yes" : "no") + "\n" +
            "* Is value required: " + (option.isValueRequired() ? "yes" : "no") + "\n" +
            "* Is multiple: " + (option.isArray() ? "yes" : "no") + "\n" +
            "* Description: " + (option.getDescription() == null ? "<none>" : option.getDescription()) + "\n" +
            "* Default: `" + (option.getDefaultValue() == null ? "" : option.getDefaultValue().replaceAll("\\n", " ")) + "`"
        );
    }

    @Override
    protected void describeInputDefinition(InputDefinition definition, DescriptorOptions options)
    {
        boolean showArguments = definition.getArguments().size() > 0;
        if (showArguments) {
            write("### Arguments:");
            for (InputArgument argument : definition.getArguments()) {
                write("\n\n");
                describeInputArgument(argument, options);
            }
        }

        if (definition.getOptions().size() > 0) {
            if (showArguments) {
                write("\n\n");
            }
            write("### Options:");
            for (InputOption option : definition.getOptions()) {
                write("\n\n");
                describeInputOption(option, options);
            }
        }
    }

    @Override
    protected void describeCommand(Command command, DescriptorOptions options)
    {
        command.getSynopsis();
        command.mergeApplicationDefinition(false);

        write(
                command.getName() + "\n" +
                StringUtils.repeat("-", command.getName().length()) + "\n\n" +
                "* Description: " + (command.getDescription() == null ? "<none>" : command.getDescription()) + "\n" +
                "* Usage: `" + command.getSynopsis() + "`\n" +
                "* Aliases: " + (command.getAliases().length > 0 ? "`" + StringUtils.join(command.getAliases(), "`, `") + "`" : "<none>")
        );

        String help = command.getProcessedHelp();
        if (help != null) {
            write("\n\n");
            write(help);
        }

        InputDefinition definition = command.getNativeDefinition();
        if (definition != null) {
            write("\n\n");
            describeInputDefinition(definition, options);
        }
    }

    @Override
    protected void describeApplication(Application application, DescriptorOptions options)
    {
        String describedNamespace = options.has("namespace") ? options.get("namespace") : null;
        ApplicationDescription description = new ApplicationDescription(application, describedNamespace);

        write(application.getName() + "\n" + StringUtils.repeat("=", application.getName().length()));

        for (Map.Entry<String, List<String>> entry : description.getNamespaces().entrySet()) {
            String namespace = entry.getKey();
            if (!namespace.equals(ApplicationDescription.GLOBAL_NAMESPACE)) {
                write("\n\n");
                write("**" + namespace + ":**");
            }

            write("\n\n");
            for (String commandName : entry.getValue()) {
                write("* " + commandName + "\n");
            }
        }

        for (Command command : description.getCommands().values()) {
            write("\n\n");
            describeCommand(command, options);
        }
    }
}
