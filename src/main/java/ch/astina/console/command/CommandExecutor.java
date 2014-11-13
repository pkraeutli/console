package ch.astina.console.command;

import ch.astina.console.input.Input;
import ch.astina.console.output.Output;

public interface CommandExecutor
{
    public int execute(Input input, Output output);
}
