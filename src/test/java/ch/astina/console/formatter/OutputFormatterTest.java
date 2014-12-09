package ch.astina.console.formatter;

import ch.astina.console.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class OutputFormatterTest
{
    @Test
    public void testTemptyTag()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);
        Assert.assertEquals("foo<>bar", formatter.format("foo<>bar"));
    }

    @Test
    public void testLGCharEscaping()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals("foo<bar", formatter.format("foo\\<bar"));
        Assert.assertEquals("<info>some info</info>", formatter.format("\\<info>some info\\</info>"));
        Assert.assertEquals("\\<info>some info\\</info>", DefaultOutputFormatter.escape("<info>some info</info>"));
        Assert.assertEquals("\033[33mAstina Console does work very well!\033[39m", formatter.format("<comment>Astina Console does work very well!</comment>"));
    }

    @Test
    public void testBundledStyles()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertTrue(formatter.hasStyle("error"));
        Assert.assertTrue(formatter.hasStyle("info"));
        Assert.assertTrue(formatter.hasStyle("comment"));
        Assert.assertTrue(formatter.hasStyle("question"));

        Assert.assertEquals("\033[37;41msome error\033[39;49m", formatter.format("<error>some error</error>"));
        Assert.assertEquals("\033[32msome info\033[39m", formatter.format("<info>some info</info>"));
        Assert.assertEquals("\033[33msome comment\033[39m", formatter.format("<comment>some comment</comment>"));
        Assert.assertEquals("\033[30;46msome question\033[39;49m", formatter.format("<question>some question</question>"));
    }

    @Test
    public void testNestedStyles()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals("\033[37;41msome \033[39;49m\033[32msome info\033[39m\033[37;41m error\033[39;49m", formatter.format("<error>some <info>some info</info> error</error>"));
    }

    @Test
    public void testAdjacentStyles()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals("\033[37;41msome error\033[39;49m\033[32msome info\033[39m", formatter.format("<error>some error</error><info>some info</info>"));
    }

    @Test
    public void testStyleMatchingNotGreedy()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals("(\033[32m>=2.0,<2.3\033[39m)", formatter.format("(<info>>=2.0,<2.3</info>)"));
    }

    @Test
    public void testStyleEscaping()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals("(\033[32mz>=2.0,<a2.3\033[39m)", formatter.format("(<info>" + DefaultOutputFormatter.escape("z>=2.0,<a2.3") + "</info>)"));
    }

    @Test
    public void testDeepNestedStyles()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals(
            "\033[37;41merror\033[39;49m\033[32minfo\033[39m\033[33mcomment\033[39m\033[37;41merror\033[39;49m",
            formatter.format("<error>error<info>info<comment>comment</info>error</error>")
        );
    }

    @Test
    public void testNewStyle()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        OutputFormatterStyle style = new DefaultOutputFormatterStyle("blue", "white");
        formatter.setStyle("test", style);

        Assert.assertEquals(style, formatter.getStyle("test"));
        Assert.assertNotEquals(style, formatter.getStyle("info"));

        style = new DefaultOutputFormatterStyle("blue", "white");
        formatter.setStyle("b", style);

        Assert.assertEquals(
            "\033[34;47msome \033[39;49m\033[34;47mcustom\033[39;49m\033[34;47m msg\033[39;49m",
            formatter.format("<test>some <b>custom</b> msg</test>")
        );
    }

    @Test
    public void testRedefineStyle()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        OutputFormatterStyle style = new DefaultOutputFormatterStyle("blue", "white");
        formatter.setStyle("info", style);

        Assert.assertEquals("\033[34;47msome custom msg\033[39;49m", formatter.format("<info>some custom msg</info>"));
    }

    @Test
    public void testInlineStyle()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals("\033[34;41msome text\033[39;49m", formatter.format("<fg=blue;bg=red>some text</>"));
        Assert.assertEquals("\033[34;41msome text\033[39;49m", formatter.format("<fg=blue;bg=red>some text</fg=blue;bg=red>"));
        Assert.assertEquals("\033[34;41;1msome text\033[39;49;22m", formatter.format("<fg=blue;bg=red;bold=bold>some text</>"));
    }

    @Test
    public void testNonStyleTag()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        Assert.assertEquals(
            "\033[32msome \033[39m\033[32m<tag>\033[39m\033[32m \033[39m\033[32m<setting=value>\033[39m\033[32m styled \033[39m\033[32m<p>\033[39m\033[32msingle-char tag\033[39m\033[32m</p>\033[39m",
            formatter.format("<info>some <tag> <setting=value> styled <p>single-char tag</p></info>")
        );
    }

    @Test
    public void testFormatLongString()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        String longStr = StringUtils.repeat("\\", 14000);
        Assert.assertEquals("\033[37;41msome error\033[39;49m" + longStr, formatter.format("<error>some error</error>" + longStr));
    }

    @Test
    public void testNotDecoratedFormatter()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(false);

        Assert.assertTrue(formatter.hasStyle("error"));
        Assert.assertTrue(formatter.hasStyle("info"));
        Assert.assertTrue(formatter.hasStyle("comment"));
        Assert.assertTrue(formatter.hasStyle("question"));

        Assert.assertEquals("some error", formatter.format("<error>some error</error>"));
        Assert.assertEquals("some info", formatter.format("<info>some info</info>"));
        Assert.assertEquals("some comment", formatter.format("<comment>some comment</comment>"));
        Assert.assertEquals("some question", formatter.format("<question>some question</question>"));

        formatter.setDecorated(true);

        Assert.assertEquals("\033[37;41msome error\033[39;49m", formatter.format("<error>some error</error>"));
        Assert.assertEquals("\033[32msome info\033[39m", formatter.format("<info>some info</info>"));
        Assert.assertEquals("\033[33msome comment\033[39m", formatter.format("<comment>some comment</comment>"));
        Assert.assertEquals("\033[30;46msome question\033[39;49m", formatter.format("<question>some question</question>"));
    }

    @Test
    public void testContentWithLineBreaks()
    {
        OutputFormatter formatter = new DefaultOutputFormatter(true);

        String nl = System.getProperty("line.separator");

        Assert.assertEquals(
            nl + "\033[32m" + nl + "some text\033[39m" + nl ,
            formatter.format(nl + "<info>" + nl + "some text</info>" + nl
        ));

        Assert.assertEquals(
            nl + "\033[32msome text" + nl + "\033[39m" + nl ,
            formatter.format(nl + "<info>some text" + nl + "</info>" + nl
        ));

        Assert.assertEquals(
            nl + "\033[32m" + nl + "some text" + nl + "\033[39m" + nl ,
            formatter.format(nl + "<info>" + nl + "some text" + nl + "</info>" + nl
        ));

        Assert.assertEquals(
            nl + "\033[32m" + nl + "some text" + nl + "more text" + nl + "\033[39m" + nl ,
            formatter.format(nl + "<info>" + nl + "some text" + nl + "more text" + nl + "</info>" + nl
        ));
    }
}
