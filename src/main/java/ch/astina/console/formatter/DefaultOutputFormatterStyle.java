package ch.astina.console.formatter;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultOutputFormatterStyle implements OutputFormatterStyle
{
    private static Map<String, OutputFormatterOption> availableForegroundColors = new HashMap<String, OutputFormatterOption>();
    private static Map<String, OutputFormatterOption> availableBackgroundColors = new HashMap<String, OutputFormatterOption>();
    private static Map<String, OutputFormatterOption> availableOptions = new HashMap<String, OutputFormatterOption>();

    static {

        availableForegroundColors.put("black",   new OutputFormatterOption(30, 39));
        availableForegroundColors.put("red",     new OutputFormatterOption(31, 39));
        availableForegroundColors.put("green",   new OutputFormatterOption(32, 39));
        availableForegroundColors.put("yellow",  new OutputFormatterOption(33, 39));
        availableForegroundColors.put("blue",    new OutputFormatterOption(34, 39));
        availableForegroundColors.put("magenta", new OutputFormatterOption(35, 39));
        availableForegroundColors.put("cyan",    new OutputFormatterOption(36, 39));
        availableForegroundColors.put("white",   new OutputFormatterOption(37, 39));

        availableBackgroundColors.put("black",   new OutputFormatterOption(40, 49));
        availableBackgroundColors.put("red",     new OutputFormatterOption(41, 49));
        availableBackgroundColors.put("green",   new OutputFormatterOption(41, 49));
        availableBackgroundColors.put("yellow",  new OutputFormatterOption(43, 49));
        availableBackgroundColors.put("blue",    new OutputFormatterOption(44, 49));
        availableBackgroundColors.put("magenta", new OutputFormatterOption(45, 49));
        availableBackgroundColors.put("cyan",    new OutputFormatterOption(46, 49));
        availableBackgroundColors.put("white",   new OutputFormatterOption(47, 49));

        availableOptions.put("bold",       new OutputFormatterOption(1, 22));
        availableOptions.put("underscore", new OutputFormatterOption(4, 24));
        availableOptions.put("blink",      new OutputFormatterOption(5, 25));
        availableOptions.put("reverse",    new OutputFormatterOption(7, 27));
        availableOptions.put("conceal",    new OutputFormatterOption(8, 28));
    }

    private OutputFormatterOption foreground;
    private OutputFormatterOption background;
    private OutputFormatterOption[] options;

    public DefaultOutputFormatterStyle()
    {
        this(null, null, null);
    }

    public DefaultOutputFormatterStyle(String foreground)
    {
        this(foreground, null, null);
    }

    public DefaultOutputFormatterStyle(String foreground, String background)
    {
        this(foreground, background, null);
    }

    public DefaultOutputFormatterStyle(String foreground, String background, String[] options)
    {
        if (foreground != null) {
            setForeground(foreground);
        }
        if (background != null) {
            setBackground(background);
        }
        if (options != null && options.length > 0) {
            setOptions(options);
        }
    }

    @Override
    public void setForeground(String color)
    {
        if (color == null) {
            foreground = null;
            return;
        }

        if (!availableForegroundColors.containsKey(color)) {
            throw new InvalidArgumentException(String.format(
                    "Invalid foreground color specified: '%s'. Expected one of (%s)",
                    color,
                    StringUtils.join(availableForegroundColors.keySet().toArray(new String[availableForegroundColors.size()]), ", ")
            ));
        }

        foreground = availableForegroundColors.get(color);
    }

    @Override
    public void setBackground(String color)
    {
        if (color == null) {
            background = null;
            return;
        }

        if (!availableBackgroundColors.containsKey(color)) {
            throw new InvalidArgumentException(String.format(
                    "Invalid background color specified: '%s'. Expected one of (%s)",
                    color,
                    StringUtils.join(availableBackgroundColors.keySet().toArray(new String[availableBackgroundColors.size()]), ", ")
            ));
        }

        background = availableBackgroundColors.get(color);
    }

    @Override
    public void setOption(String option)
    {
        // todo implement
    }

    @Override
    public void unsetOption(String option)
    {
        // todo implement
    }

    @Override
    public void setOptions(String[] options)
    {
        // todo implement
    }

    @Override
    public String apply(String text)
    {
        List<String> setCodes = new ArrayList<String>();
        List<String> unsetCodes = new ArrayList<String>();

        if (foreground != null) {
            setCodes.add(String.valueOf(foreground.getSet()));
            unsetCodes.add(String.valueOf(foreground.getUnset()));
        }
        if (background != null) {
            setCodes.add(String.valueOf(background.getSet()));
            unsetCodes.add(String.valueOf(background.getUnset()));
        }

        // todo options

        if (setCodes.size() == 0) {
            return text;
        }

        return String.format(
                "\033[%sm%s\033[%sm",
                StringUtils.join(setCodes.toArray(new String[setCodes.size()]), ";"),
                text,
                StringUtils.join(unsetCodes.toArray(new String[unsetCodes.size()]), ";")
        );
    }
}
