package ch.astina.console.helper;

public class TimeFormat
{
    private int seconds;
    private String name;
    private Integer div;

    public TimeFormat(int seconds, String name)
    {
        this.seconds = seconds;
        this.name = name;
    }

    public TimeFormat(int seconds, String name, Integer div)
    {
        this.seconds = seconds;
        this.name = name;
        this.div = div;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public String getName()
    {
        return name;
    }

    public Integer getDiv()
    {
        return div;
    }
}
