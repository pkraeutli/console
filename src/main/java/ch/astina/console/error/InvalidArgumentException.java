package ch.astina.console.error;

public class InvalidArgumentException extends RuntimeException
{
    public InvalidArgumentException(String message)
    {
        super(message);
    }
}
