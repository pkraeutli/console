package ch.astina.console.question;

import java.util.HashMap;

public class ChoiceQuestion extends Question
{
    private HashMap<String, String> choices;
    private boolean multiselect = false;
    private String prompt = " > ";
    private String errorMessage = "Value '%s' is invalid";

    public ChoiceQuestion(String question, HashMap<String, String> choices, String defaultValue)
    {
        super(question, defaultValue);
        this.choices = choices;
    }

}
