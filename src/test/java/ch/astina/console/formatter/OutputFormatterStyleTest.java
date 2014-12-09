package ch.astina.console.formatter;

import ch.astina.console.error.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class OutputFormatterStyleTest
{
    @Test
    public void testConstructor()
    {
        OutputFormatterStyle style = new DefaultOutputFormatterStyle("green", "black", "bold", "underscore");
        Assert.assertEquals("\033[32;40;1;4mfoo\033[39;49;22;24m", style.apply("foo"));

        style = new DefaultOutputFormatterStyle("red", null, "blink");
        Assert.assertEquals("\033[31;5mfoo\033[39;25m", style.apply("foo"));

        style = new DefaultOutputFormatterStyle(null, "white");
        Assert.assertEquals("\033[47mfoo\033[49m", style.apply("foo"));
    }

    @Test
    public void testForeground()
    {
        OutputFormatterStyle style = new DefaultOutputFormatterStyle();

        style.setForeground("black");
        Assert.assertEquals("\033[30mfoo\033[39m", style.apply("foo"));

        style.setForeground("blue");
        Assert.assertEquals("\033[34mfoo\033[39m", style.apply("foo"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidForeground()
    {
        OutputFormatterStyle style = new DefaultOutputFormatterStyle();
        style.setForeground("undefined-color");
    }

    @Test
    public void testBackground()
    {
        OutputFormatterStyle style = new DefaultOutputFormatterStyle();

        style.setBackground("black");
        Assert.assertEquals("\033[40mfoo\033[49m", style.apply("foo"));

        style.setBackground("yellow");
        Assert.assertEquals("\033[43mfoo\033[49m", style.apply("foo"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidBackground()
    {
        OutputFormatterStyle style = new DefaultOutputFormatterStyle();
        style.setBackground("undefined-color");
    }

    @Test
    public void testOptions()
    {
        OutputFormatterStyle style = new DefaultOutputFormatterStyle();

        style.setOptions("reverse", "conceal");
        Assert.assertEquals("\033[7;8mfoo\033[27;28m", style.apply("foo"));

        style.setOption("bold");
        Assert.assertEquals("\033[7;8;1mfoo\033[27;28;22m", style.apply("foo"));

        style.unsetOption("reverse");
        Assert.assertEquals("\033[8;1mfoo\033[28;22m", style.apply("foo"));

        style.setOption("bold");
        Assert.assertEquals("\033[8;1mfoo\033[28;22m", style.apply("foo"));

        style.setOptions("bold");
        Assert.assertEquals("\033[1mfoo\033[22m", style.apply("foo"));

        try {
            style.setOption("foo");
            Assert.fail(".setOption() throws an InvalidArgumentException when the option does not exist in the available options");
        } catch (InvalidArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid option specified: 'foo'"));
        }

        try {
            style.unsetOption("foo");
            Assert.fail(".unsetOption() throws an InvalidArgumentException when the option does not exist in the available options");
        } catch (InvalidArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid option specified: 'foo'"));
        }
    }
}
