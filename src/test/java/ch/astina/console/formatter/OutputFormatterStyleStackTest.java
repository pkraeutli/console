package ch.astina.console.formatter;

import ch.astina.console.error.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class OutputFormatterStyleStackTest
{
    @Test
    public void testPush()
    {
        OutputFormatterStyle s1 = new DefaultOutputFormatterStyle("white", "black");
        OutputFormatterStyle s2 = new DefaultOutputFormatterStyle("yellow", "blue");

        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        stack.push(s1);
        stack.push(s2);

        OutputFormatterStyle s3 = new DefaultOutputFormatterStyle("green", "red");
        stack.push(s3);

        Assert.assertEquals(s3, stack.getCurrent());
    }

    @Test
    public void testPop()
    {
        OutputFormatterStyle s1 = new DefaultOutputFormatterStyle("white", "black");
        OutputFormatterStyle s2 = new DefaultOutputFormatterStyle("yellow", "blue");

        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        stack.push(s1);
        stack.push(s2);

        Assert.assertEquals(s2, stack.pop());
        Assert.assertEquals(s1, stack.pop());
    }

    @Test
    public void testPopEmpty()
    {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        DefaultOutputFormatterStyle style = new DefaultOutputFormatterStyle();

        Assert.assertEquals(style, stack.pop());
    }

    @Test
    public void testPopNotLast()
    {
        OutputFormatterStyle s1 = new DefaultOutputFormatterStyle("white", "black");
        OutputFormatterStyle s2 = new DefaultOutputFormatterStyle("yellow", "blue");
        OutputFormatterStyle s3 = new DefaultOutputFormatterStyle("green", "red");

        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        stack.push(s1);
        stack.push(s2);
        stack.push(s3);

        Assert.assertEquals(s2, stack.pop(s2));
        Assert.assertEquals(s1, stack.pop());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidPop()
    {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        stack.push(new DefaultOutputFormatterStyle("white", "black"));
        stack.pop(new DefaultOutputFormatterStyle("yellow", "blue"));
    }
}
