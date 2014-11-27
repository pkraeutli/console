package ch.astina.console.helper;

import ch.astina.console.input.Input;
import ch.astina.console.output.Output;
import ch.astina.console.question.ChoiceQuestion;
import ch.astina.console.question.Question;

import java.io.Console;
import java.io.InputStream;
import java.util.Scanner;

public class QuestionHelper extends AbstractHelper
{
    private InputStream inputStream;

    public String ask(Input input, Output output, Question question)
    {
        if (!input.isInteractive()) {
            return question.getDefaultValue();
        }

        return doAsk(output, question);
    }

    protected String doAsk(Output output, Question question)
    {
        InputStream inputStream = this.inputStream == null ? System.in : this.inputStream;

        String message = question.getQuestion();
        if (question instanceof ChoiceQuestion) {

        }

        output.writeln(message);

        String answer;
        if (question.isHidden()) {
            Console console = System.console();
            if (console == null) {
                throw new RuntimeException("Unable to hide input (console not available)");
            }
            answer = String.valueOf(console.readPassword());
        } else {
            Scanner scanner = new Scanner(inputStream);
            answer = scanner.next();
        }

        if (answer == null || answer.isEmpty()) {
            answer = question.getDefaultValue();
        }

        return answer;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    @Override
    public String getName()
    {
        return "question";
    }
}
