package ch.astina.console.question;

import ch.astina.console.error.InvalidArgumentException;

public class Question
{
    private String question;
    private Integer attempts;
    private boolean hidden = false;
    private boolean hiddenFallback = true;
    private String defaultValue;

    public Question(String question)
    {
        this(question, null);
    }

    public Question(String question, String defaultValue)
    {
        this.question = question;
        this.defaultValue = defaultValue;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public Question setHidden(boolean hidden)
    {
        this.hidden = hidden;

        return this;
    }

    public boolean isHiddenFallback()
    {
        return hiddenFallback;
    }

    public Question setHiddenFallback(boolean hiddenFallback)
    {
        this.hiddenFallback = hiddenFallback;

        return this;
    }

    public Question setMaxAttempts(Integer attempts)
    {
        if (attempts != null && attempts < 1) {
            throw new InvalidArgumentException("Maximum number of attempts must be a positive value.");
        }

        this.attempts = attempts;

        return this;
    }

    public Integer getMaxAttempts()
    {
        return attempts;
    }
}
