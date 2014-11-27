package ch.astina.console.helper;

import ch.astina.console.formatter.OutputFormatter;

public abstract class AbstractHelper implements Helper
{
    protected HelperSet helperSet;

    private static TimeFormat[] timeFormats;

    static {
        timeFormats = new TimeFormat[]{
                new TimeFormat(0, "< 1 sec"),
                new TimeFormat(2, "1 sec"),
                new TimeFormat(59, "secs", 1),
                new TimeFormat(60, "1 min"),
                new TimeFormat(3600, "mins", 60),
                new TimeFormat(5400, "1 hr"),
                new TimeFormat(86400, "hrs", 3600),
                new TimeFormat(129600, "1 day"),
                new TimeFormat(604800, "days", 86400),
        };
    }

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

    public static String formatTime(long seconds)
    {
        for (TimeFormat timeFormat : timeFormats) {
            if (seconds >= timeFormat.getSeconds()) {
                continue;
            }

            if (timeFormat.getDiv() == null) {
                return timeFormat.getName();
            }

            return (int) Math.ceil(seconds / timeFormat.getDiv()) + " " + timeFormat.getName();
        }

        return null;
    }

    public static String formatMemory(long memory)
    {
        if (memory >= 1024 * 1024 * 1024) {
            return String.format("%s GiB", memory / 1024 / 1204 / 1024);
        }

        if (memory >= 1024 * 1024) {
            return String.format("%s MiB", memory / 1204 / 1024);
        }

        if (memory >= 1024) {
            return String.format("%s KiB", memory / 1024);
        }

        return String.format("%d B", memory);
    }

    public static int strlenWithoutDecoration(OutputFormatter formatter, String string)
    {
        boolean isDecorated = formatter.isDecorated();
        formatter.setDecorated(false);

        // remove <...> formatting
        string = formatter.format(string);
        // remove already formatted characters
        string = string.replaceAll("\\033\\[[^m]*m", "");

        formatter.setDecorated(isDecorated);

        return string.length();
    }
}
