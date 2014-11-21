package ch.astina.console;

import ch.astina.console.command.Command;
import ch.astina.console.command.CommandExecutor;
import ch.astina.console.command.GreetingCommand;
import ch.astina.console.helper.ProgressBar;
import ch.astina.console.input.Input;
import ch.astina.console.output.Output;

public class Console
{
    public static void main(String[] args)
    {
        Application app = new Application("Astina Console", "1.0.0-SNAPSHOT");
        app.add(new GreetingCommand());
        app.add((new Command("test")).setExecutor(new CommandExecutor()
        {
            @Override
            public int execute(Input input, Output output)
            {
                output.writeln("<info>Prosim!</info>");

                ProgressBar bar = new ProgressBar(output, 500);
                for (int i = 0; i < 500; i++) {
                    bar.advance();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                bar.finish();

                return 0;
            }
        }));
        int exitCode = app.run(args);

        System.exit(exitCode);
    }
}
