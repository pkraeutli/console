package ch.astina.console;

import ch.astina.console.command.Command;
import ch.astina.console.command.CommandExecutor;
import ch.astina.console.command.GreetingCommand;
import ch.astina.console.helper.ProgressBar;
import ch.astina.console.input.Input;
import ch.astina.console.input.StringInput;
import ch.astina.console.output.Output;
import ch.astina.console.output.SystemOutput;

import java.util.Scanner;

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

        app.run(args);

//        app.setAutoExit(false);
//        Scanner scanner = new Scanner(System.in);
//        Output output = new SystemOutput();
//
//        while (true) {
//            output.write("> ");
//            String line = scanner.nextLine();
//            app.run(new StringInput(line), output);
//            output.write("\n\n");
//        }
    }
}
