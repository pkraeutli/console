package ch.astina.console.command;

import ch.astina.console.input.Input;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputDefinition;
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

        this
            .setName("help")
            .setDescription("Displays help for a command")
            .setDefinition(definition)
            .setHelp("TODO")
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

        // todo implement

        return 0;
    }
}
