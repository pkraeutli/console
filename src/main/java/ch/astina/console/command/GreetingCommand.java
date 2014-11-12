package ch.astina.console.command;

import ch.astina.console.helper.QuestionHelper;
import ch.astina.console.input.Input;
import ch.astina.console.input.InputArgument;
import ch.astina.console.output.Output;
import ch.astina.console.question.Question;

public class GreetingCommand extends Command
{
    @Override
    protected void configure()
    {
        this
            .setName("greet")
            .setDescription("Outputs a greeting.")
            .addArgument("name", InputArgument.OPTIONAL, "Name of the person to greet")
        ;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        String name = input.getArgument("name");
        if (name == null) {
            name = "stranger";
        }

        output.writeln(String.format("Greetings, %s!", name));

        return 0;
    }

    @Override
    protected void interact(Input input, Output output)
    {
        if (input.getArgument("name") == null) {
            String name = ((QuestionHelper) getHelper("question"))
                    .ask(input, output, new Question("What is your name?"));
            input.setArgument("name", name);
        }
    }
}
