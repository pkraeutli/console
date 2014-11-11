package ch.astina.console;

import ch.astina.console.command.Command;
import ch.astina.console.input.Input;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputOption;
import ch.astina.console.output.Output;

public class FileListCommand extends Command
{
    @Override
    protected void configure()
    {
        this
            .setName("ls")
            .setDescription("Displays files in the given directory")
            .addArgument("dir", InputArgument.REQUIRED, "Directory name")
            .addOption("long", "l", InputOption.VALUE_NONE, "List in long format.")
            .setHelp("Some help text ...")
        ;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        // todo implement

        return 0;
    }
}
