package ch.astina.console.command;

import ch.astina.console.input.Input;
import ch.astina.console.output.Output;

public class TestCommand extends Command
{
    @Override
    protected void configure()
    {
        this
                .setName("namespace:name")
                .setAliases("name")
                .setDescription("description")
                .setHelp("help")
        ;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        output.writeln("execute called");

        return 0;
    }

    @Override
    protected void interact(Input input, Output output)
    {
        output.writeln("interact called");
    }
}
