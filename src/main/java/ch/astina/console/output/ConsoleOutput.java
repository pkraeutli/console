package ch.astina.console.output;

public interface ConsoleOutput extends Output
{
    public Output getErrorOutput();

    public void setErrorOutput(Output error);
}
