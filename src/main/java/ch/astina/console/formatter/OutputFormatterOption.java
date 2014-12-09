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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof OutputFormatterOption)) return false;

        OutputFormatterOption that = (OutputFormatterOption) o;

        if (set != that.set) return false;
        if (unset != that.unset) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = set;
        result = 31 * result + unset;
        return result;
    }
}
