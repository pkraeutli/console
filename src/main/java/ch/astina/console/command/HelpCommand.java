package ch.astina.console.command;

import ch.astina.console.descriptor.DescriptorOptions;
import ch.astina.console.helper.DescriptorHelper;
import ch.astina.console.input.Input;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputDefinition;
import ch.astina.console.input.InputOption;
import ch.astina.console.output.Output;

public class HelpCommand extends Command
{
    private Command command;

    @Override
    protected void configure()
    {
        ignoreValidationErrors();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(new InputArgument("command_name", InputArgument.OPTIONAL, "The command name", "help"));
        definition.addOption(new InputOption("xml", null, InputOption.VALUE_NONE, "To output help as XML"));
        definition.addOption(new InputOption("format", null, InputOption.VALUE_REQUIRED, "To output help in other formats", "txt"));
        definition.addOption(new InputOption("raw", null, InputOption.VALUE_NONE, "To output raw command help"));

        this
            .setName("help")
            .setDescription("Displays help for a command")
            .setDefinition(definition)
            .setHelp("The <info>%command.name%</info> command displays help for a given command:\n" +
                    "\n" +
                    "  <info>%command.name% list</info>\n" +
                    "\n" +
                    "You can also output the help in other formats by using the <comment>--format</comment> option:\n" +
                    "\n" +
                    "  <info>%command.name% --format=xml list</info>\n" +
                    "\n" +
                    "To display the list of available commands, please use the <info>list</info> command.\n")
        ;
    }

    public void setCommand(Command command)
    {
        this.command = command;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        if (command == null) {
            command = getApplication().find(input.getArgument("command_name"));
        }

        if (input.getOption("xml") != null) {
            input.setOption("format", "xml");
        }

        DescriptorHelper helper = new DescriptorHelper();
        helper.describe(output, command, (new DescriptorOptions())
                .set("format", input.getOption("format"))
                .set("raw", input.getOption("raw"))
        );

        return 0;
    }
}
