package ch.astina.console.formatter;

import ch.astina.console.error.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;

public class OutputFormatterStyleStack
{
    List<OutputFormatterStyle> styles;

    OutputFormatterStyle emptyStyle;

    public OutputFormatterStyleStack()
    {
        this(new DefaultOutputFormatterStyle());
    }

    public OutputFormatterStyleStack(OutputFormatterStyle emptyStyle)
    {
        this.emptyStyle = emptyStyle;
        reset();
    }

    public void reset()
    {
        styles = new ArrayList<OutputFormatterStyle>();
    }

    public void push(OutputFormatterStyle style)
    {
        styles.add(style);
    }

    public OutputFormatterStyle pop()
    {
        return pop(null);
    }

    public OutputFormatterStyle pop(OutputFormatterStyle style)
    {
        if (styles.size() == 0) {
            return emptyStyle;
        }

        if (style == null) {
            return styles.remove(styles.size() - 1);
        }

        OutputFormatterStyle stackedStyle;
        for (int i = (styles.size() - 1); i >= 0; i--) {
            stackedStyle = styles.get(i);
            if (style.apply("").equals(stackedStyle.apply(""))) {
                styles = styles.subList(0, i);

                return stackedStyle;
            }
        }

        throw new InvalidArgumentException("Incorrectly nested style tag found.");
    }

    public OutputFormatterStyle getCurrent()
    {
        if (styles.size() == 0) {
            return emptyStyle;
        }

        return styles.get(styles.size() - 1);
    }

    public OutputFormatterStyle getEmptyStyle()
    {
        return emptyStyle;
    }

    public void setEmptyStyle(OutputFormatterStyle emptyStyle)
    {
        this.emptyStyle = emptyStyle;
    }
}
