package ch.astina.console.helper;

public abstract class AbstractHelper implements Helper
{
    protected HelperSet helperSet;

    @Override
    public HelperSet getHelperSet()
    {
        return helperSet;
    }

    @Override
    public void setHelperSet(HelperSet helperSet)
    {
        this.helperSet = helperSet;
    }


}
