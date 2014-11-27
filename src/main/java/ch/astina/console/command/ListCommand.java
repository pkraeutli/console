package ch.astina.console.command;

import ch.astina.console.descriptor.DescriptorOptions;
import ch.astina.console.helper.DescriptorHelper;
import ch.astina.console.input.Input;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputDefinition;
import ch.astina.console.input.InputOption;
import ch.astina.console.output.Output;

public class ListCommand extends Command
{
    @Override
    protected void configure()
    {
        this
            .setName("list")
            .setDefinition(createDefinition())
            .setDescription("Lists commands")
            .setHelp("The <info>%command.name%</info> command lists all commands:\n" +
                    "\n" +
                    "  <info>%command.name%</info>\n" +
                    "\n" +
                    "You can also display the commands for a specific namespace:\n" +
                    "\n" +
                    "  <info>%command.name% test</info>\n" +
                    "\n" +
                    "You can also output the information in other formats by using the <comment>--format</comment> option:\n" +
                    "\n" +
                    "  <info>%command.name% --format=xml</info>\n" +
                    "\n" +
                    "It's also possible to get raw list of commands (useful for embedding command runner):\n" +
                    "\n" +
                    "  <info>%command.name% --raw</info>\n")
        ;
    }

    @Override
    public InputDefinition getNativeDefinition()
    {
        return createDefinition();
    }

    @Override
    protected int execute(Input input, Output output)
    {
        if (input.getOption("xml") != null) {
            input.setOption("format", "xml");
        }

        DescriptorHelper helper = new DescriptorHelper();
        helper.describe(output, getApplication(), (new DescriptorOptions())
                .set("format", input.getOption("format"))
                .set("raw_text", input.getOption("raw"))
                .set("namespace", input.getArgument("namespace"))
        );

        return 0;
    }

    private InputDefinition createDefinition()
    {
        InputDefinition definition = new InputDefinition();
        definition.addArgument(new InputArgument("namespace", InputArgument.OPTIONAL, "The namespace name"));
        definition.addOption(new InputOption("xml", null, InputOption.VALUE_NONE, "To output list as XML"));
        definition.addOption(new InputOption("raw", null, InputOption.VALUE_NONE, "To output raw command list"));
        definition.addOption(new InputOption("format", null, InputOption.VALUE_REQUIRED, "To output list in other formats", "txt"));

        return definition;
    }
}
