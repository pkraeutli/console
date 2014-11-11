package ch.astina.console.formatter;

import ch.astina.console.error.InvalidArgumentException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultOutputFormatter implements OutputFormatter
{
    private boolean decorated;

    private Map<String, OutputFormatterStyle> styles = new HashMap<String, OutputFormatterStyle>();

    private OutputFormatterStyleStack styleStack;

    public static String escape(String text)
    {
        return text.replaceAll("([^\\\\\\\\]?)<", "$1\\<");
    }

    public DefaultOutputFormatter()
    {
        this(false, new HashMap<String, OutputFormatterStyle>());
    }

    public DefaultOutputFormatter(boolean decorated, Map<String, OutputFormatterStyle> styles)
    {
        this.decorated = decorated;

        setStyle("error", new DefaultOutputFormatterStyle("white", "red"));
        setStyle("info", new DefaultOutputFormatterStyle("green"));
        setStyle("comment", new DefaultOutputFormatterStyle("yellow"));
        setStyle("question", new DefaultOutputFormatterStyle("black", "cyan"));

        this.styles.putAll(styles);

        styleStack = new OutputFormatterStyleStack();
    }

    @Override
    public void setDecorated(boolean decorated)
    {
        this.decorated = decorated;
    }

    @Override
    public boolean isDecorated()
    {
        return decorated;
    }

    @Override
    public void setStyle(String name, OutputFormatterStyle style)
    {
        styles.put(name.toLowerCase(), style);
    }

    @Override
    public boolean hasStyle(String name)
    {
        return styles.containsKey(name.toLowerCase());
    }

    @Override
    public OutputFormatterStyle getStyle(String name)
    {
        if (!hasStyle(name)) {
            throw new InvalidArgumentException(String.format("Undefined style: %s", name));
        }

        return styles.get(name);
    }

    @Override
    public String format(String message)
    {
        if (message == null) {
            return "";
        }

        int offset = 0;
        StringBuilder output = new StringBuilder();

        String tagRegex = "[a-z][a-z0-9_=;-]*";
        Pattern pattern = Pattern.compile("<((" + tagRegex + ")|/(" + tagRegex + ")?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(message);

        boolean open = false;
        String tag = "";
        OutputFormatterStyle style;
        while (matcher.find()) {
            int pos = matcher.start();
            String text = matcher.group();

            // add the text up to the next tag
            output.append(applyCurrentStyle(message.substring(offset, pos)));
            offset = pos + text.length();

            // opening tag?
            open = text.charAt(1) != '/';
            if (open) {
                tag = matcher.group(2);
            } else {
                tag = matcher.group(3);
            }

            if (!open && tag.isEmpty()) {
                // </>
                styleStack.pop();
            } else if (pos > 0 && message.charAt(pos - 1) == '\\') {
                // escaped tag
                output.append(applyCurrentStyle(text));
            } else {
                style = createStyleFromString(tag.toLowerCase());
                if (open) {
                    styleStack.push(style);
                } else {
                    styleStack.pop(style);
                }
            }
        }

        output.append(applyCurrentStyle(message.substring(offset)));

        return output.toString().replaceAll("\\<", "<");
    }

    private OutputFormatterStyle createStyleFromString(String string)
    {
        if (styles.containsKey(string)) {
            return styles.get(string);
        }

        // todo implement fg=... bg=... stuff

        return null;
    }

    private String applyCurrentStyle(String text)
    {
        if (isDecorated() && text.length() > 0) {
            return styleStack.getCurrent().apply(text);
        }

        return text;
    }
}
