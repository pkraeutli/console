package ch.astina.console.formatter;

public class OutputFormatterOption
{
    private int set;

    private int unset;

    public OutputFormatterOption(int set, int unset)
    {
        this.set = set;
        this.unset = unset;
    }

    public int getSet()
    {
        return set;
    }

    public int getUnset()
    {
        return unset;
    }
}
