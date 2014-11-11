package ch.astina.console.formatter;

public interface OutputFormatterStyle
{
    public void setForeground(String color);

    public void setBackground(String color);

    public void setOption(String option);

    public void unsetOption(String option);

    public void setOptions(String[] options);

    public String apply(String text);
}
