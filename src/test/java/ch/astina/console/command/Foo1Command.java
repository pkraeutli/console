package ch.astina.console.command;

import ch.astina.console.input.Input;
import ch.astina.console.output.Output;

public class Foo1Command extends Command
{
    public Input input;
    public Output output;

    @Override
    protected void configure()
    {
        this
                .setName("foo:bar1")
                .setAliases("afoobar1")
                .setDescription("The foo:bar1 command")
        ;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        this.input = input;
        this.output = output;

        return 0;
    }
}
