package ch.astina.console.input;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringInput extends ArgvInput
{
    private static final String REGEX_STRING = "([^\\s]+?)(?:\\s|(?<!\\\\\\\\)\"|(?<!\\\\\\\\)\\'|$)";

    private static final String REGEX_QUOTED_STRING = "(?:\"([^\"\\\\\\\\]*(?:\\\\\\\\.[^\"\\\\\\\\]*)*)\"|\\'([^\\'\\\\\\\\]*(?:\\\\\\\\.[^\\'\\\\\\\\]*)*)\\')";

    public StringInput(String input)
    {
        super(new String[]{});

        setTokens(tokenize(input));
    }

    private List<String> tokenize(String input)
    {
        List<String> tokens = new ArrayList<>();
        int length = input.length();
        String inputPart;
        String token;
        int matchLength;
        int cursor = 0;

        Pattern whiteSpace = Pattern.compile("^\\s+");
        Matcher whiteSpaceMatcher;
        Pattern quotedOption = Pattern.compile("^([^=\"\\'\\s]+?)(=?)(" + REGEX_QUOTED_STRING + "+)");
        Matcher quotedOptionMatcher;
        Pattern quotedString = Pattern.compile("^" + REGEX_QUOTED_STRING);
        Matcher quotedStringMatcher;
        Pattern string = Pattern.compile("^" + REGEX_STRING);
        Matcher stringMatcher;

        while (cursor < length) {

            inputPart = input.substring(cursor);

            whiteSpaceMatcher = whiteSpace.matcher(inputPart);
            quotedOptionMatcher = quotedOption.matcher(inputPart);
            quotedStringMatcher = quotedString.matcher(inputPart);
            stringMatcher = string.matcher(inputPart);

            if (whiteSpaceMatcher.find()) {
                matchLength = whiteSpaceMatcher.end() - whiteSpaceMatcher.start();
            } else if (quotedOptionMatcher.find()) {
                token = quotedOptionMatcher.group(1) + quotedOptionMatcher.group(2) + StringUtils.unquote(quotedOptionMatcher.group(3).substring(1, quotedOptionMatcher.group(3).length() - 1).replaceAll("(\"')|('\")|('')|(\"\")", ""));
                tokens.add(token);
                matchLength = quotedOptionMatcher.group(0).length();
            } else if (quotedStringMatcher.find()) {
                token = quotedStringMatcher.group();
                tokens.add(StringUtils.unquote(token.substring(1, token.length() - 1)));
                matchLength = token.length();
            } else if (stringMatcher.find()) {
                token = stringMatcher.group(1);
                tokens.add(StringUtils.unquote(token));
                matchLength = stringMatcher.group(0).length();
            } else {
                // should never happen
                throw new InvalidArgumentException(String.format("Unable to parse input near '... %s ...", input.substring(cursor, cursor + 10)));
            }

            cursor += matchLength;
        }

        return tokens;
    }
}
