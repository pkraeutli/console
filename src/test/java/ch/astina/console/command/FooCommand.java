package ch.astina.console.command;

import ch.astina.console.input.Input;
import ch.astina.console.output.Output;

public class FooCommand extends Command
{
    public Input input;
    public Output output;

    @Override
    protected void configure()
    {
        this
                .setName("foo:bar")
                .setAliases("afoobar")
                .setDescription("The foo:bar command")
        ;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        this.input = input;
        this.output = output;

        output.writeln("called");

        return 0;
    }

    @Override
    protected void interact(Input input, Output output)
    {
        output.writeln("interact called");
    }
}
