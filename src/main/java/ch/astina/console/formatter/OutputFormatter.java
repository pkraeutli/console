package ch.astina.console.formatter;

public interface OutputFormatter
{
    public void setDecorated(boolean decorated);

    public boolean isDecorated();

    public void setStyle(String name, OutputFormatterStyle style);

    public boolean hasStyle(String name);

    public OutputFormatterStyle getStyle(String name);

    public String format(String message);
}
