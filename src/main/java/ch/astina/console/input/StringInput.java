package ch.astina.console.input;

import java.util.ArrayList;
import java.util.List;

public class StringInput extends ArgvInput
{
    public StringInput(String input)
    {
        super(new String[]{});

        setTokens(tokenize(input));
    }

    private List<String> tokenize(String input)
    {
        // todo implement tokenize()

        return new ArrayList<>();
    }
}
