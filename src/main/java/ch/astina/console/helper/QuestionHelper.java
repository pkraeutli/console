package ch.astina.console.helper;

import ch.astina.console.input.Input;
import ch.astina.console.output.Output;
import ch.astina.console.question.Question;

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

        output.writeln(message);

        Scanner scanner = new Scanner(inputStream);
        String answer = scanner.next();

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
