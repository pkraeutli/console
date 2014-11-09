package ch.astina.console;

public class InvalidArgumentException extends RuntimeException
{
    public InvalidArgumentException(String message)
    {
        super(message);
    }
}
