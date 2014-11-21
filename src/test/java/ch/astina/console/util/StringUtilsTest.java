package ch.astina.console.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest
{
    @Test
    public void testRepeat() throws Exception
    {
        String s = StringUtils.repeat("a", 5);

        Assert.assertEquals("aaaaa", s);
    }

    @Test
    public void testStripTags() throws Exception
    {
        Assert.assertEquals("aa bb", StringUtils.stripTags("aa <foo>bb</foo>"));
    }

    @Test
    public void testCount() throws Exception
    {
        Assert.assertEquals(4, StringUtils.count("abcdbdbbd", 'b'));
        Assert.assertEquals(0, StringUtils.count("abcdbdbbd", 'x'));
    }

    @Test
    public void testJoin() throws Exception
    {
        Assert.assertEquals("a,b,c", StringUtils.join(new String[]{"a", "b", "c"}, ","));
        Assert.assertEquals("a", StringUtils.join(new String[]{"a"}, ","));
    }

    @Test
    public void testLtrim() throws Exception
    {
        Assert.assertEquals("a", StringUtils.ltrim("  a", ' '));
        Assert.assertEquals(" a", StringUtils.ltrim("- a", '-'));
    }

    @Test
    public void testRtrim() throws Exception
    {
        Assert.assertEquals("a", StringUtils.rtrim("a  ", ' '));
        Assert.assertEquals("a ", StringUtils.rtrim("a -", '-'));
    }

    @Test
    public void testPadRight() throws Exception
    {
        Assert.assertEquals("a**", StringUtils.padRight("a", 3, '*'));
        Assert.assertEquals("a", StringUtils.padRight("a", 0, '*'));
    }

    @Test
    public void testPadLeft() throws Exception
    {
        Assert.assertEquals("**a", StringUtils.padLeft("a", 3, '*'));
        Assert.assertEquals("a", StringUtils.padLeft("a", 0, '*'));
    }
}