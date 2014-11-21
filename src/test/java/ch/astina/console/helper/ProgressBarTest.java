package ch.astina.console.helper;

import ch.astina.console.error.LogicException;
import ch.astina.console.output.Output;
import ch.astina.console.output.StreamOutput;
import ch.astina.console.output.Verbosity;
import ch.astina.console.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;

public class ProgressBarTest
{
    @Test
    public void testMultipleStart()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.advance();
        bar.start();

        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]") +
                generateOutput("    1 [->--------------------------]") +
                generateOutput("    0 [>---------------------------]"),
                getOutputString(output)
        );
    }

    @Test
    public void testAdvance()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.advance();

        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]") +
                generateOutput("    1 [->--------------------------]"),
                getOutputString(output)
        );
    }

    @Test
    public void testAdvanceWithStep()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.advance(5);

        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]") +
                generateOutput("    5 [----->----------------------]"),
                getOutputString(output)
        );
    }

    @Test
    public void testAdvanceMultipleTimes()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.advance(3);
        bar.advance(2);

        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]") +
                generateOutput("    3 [--->------------------------]") +
                generateOutput("    5 [----->----------------------]"),
                getOutputString(output)
        );
    }

    @Test
    public void testAdvanceOverMax()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 10);
        bar.setProgress(9);
        bar.advance();
        bar.advance();

        Assert.assertEquals(
                generateOutput("  9/10 [=========================>--]  90%") +
                generateOutput(" 10/10 [============================] 100%") +
                generateOutput(" 11/11 [============================] 100%"),
                getOutputString(output)
        );
    }

    @Test
    public void testCustomizations()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 10);
        bar.setBarWidth(10);
        bar.setBarCharacter("_");
        bar.setEmptyBarCharacter(" ");
        bar.setProgressCharacter("/");
        bar.setFormat(" %current%/%max% [%bar%] %percent:3s%%");
        bar.start();
        bar.advance();

        Assert.assertEquals(
                generateOutput("  0/10 [/         ]   0%") +
                generateOutput("  1/10 [_/        ]  10%"),
                getOutputString(output)
        );
    }

    @Test
    public void testDisplayWithoutStart()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.display();

        Assert.assertEquals(
                generateOutput("  0/50 [>---------------------------]   0%"),
                getOutputString(output)
        );
    }

    @Test
    public void testDisplayWithQuietVerbosity()
    {
        StreamOutput output = getOutputStream(true, Verbosity.QUIET);
        ProgressBar bar = new ProgressBar(output, 50);
        bar.display();

        Assert.assertEquals(
                "",
                getOutputString(output)
        );
    }

    @Test
    public void testFinishWithoutStart()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.finish();

        Assert.assertEquals(
                generateOutput(" 50/50 [============================] 100%"),
                getOutputString(output)
        );
    }

    @Test
    public void testPercent()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.start();
        bar.display();
        bar.advance();
        bar.advance();

        Assert.assertEquals(
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput("  1/50 [>---------------------------]   2%") +
                generateOutput("  2/50 [=>--------------------------]   4%"),
                getOutputString(output)
        );
    }

    @Test
    public void testOverwriteWithShorterLine()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.setFormat(" %current%/%max% [%bar%] %percent:3s%%");
        bar.start();
        bar.display();
        bar.advance();

        // set shorter format
        bar.setFormat(" %current%/%max% [%bar%]");
        bar.advance();

        Assert.assertEquals(
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput("  1/50 [>---------------------------]   2%") +
                generateOutput("  2/50 [=>--------------------------]     "),
                getOutputString(output)
        );
    }

    @Test
    public void testStartWithMax()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.setFormat("%current%/%max% [%bar%]");
        bar.start(50);
        bar.advance();

        Assert.assertEquals(
                generateOutput(" 0/50 [>---------------------------]") +
                generateOutput(" 1/50 [>---------------------------]"),
                getOutputString(output)
        );
    }

    @Test
    public void testSetCurrentProgress()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.start();
        bar.display();
        bar.advance();
        bar.setProgress(15);
        bar.setProgress(25);

        Assert.assertEquals(
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput("  1/50 [>---------------------------]   2%") +
                generateOutput(" 15/50 [========>-------------------]  30%") +
                generateOutput(" 25/50 [==============>-------------]  50%"),
                getOutputString(output)
        );
    }

    @Test
    public void testSetCurrentBeforeStarting()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.setProgress(15);

        Assert.assertNotNull(bar.getStartTime());
    }

    @Test(expected = LogicException.class)
    public void testRegressProgress()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.start();
        bar.setProgress(15);
        bar.setProgress(10);
    }

    @Test
    public void testRedrawFrequency()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 6);
        bar = spy(bar);

        bar.setRedrawFrequency(2);
        bar.start();
        bar.setProgress(1);
        bar.advance(2);
        bar.advance(2);
        bar.advance(1);

        verify(bar, times(4)).display();
    }

    @Test
    public void testMultiByteSupport()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.setBarCharacter("■");
        bar.advance(3);

        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]") +
                generateOutput("    3 [■■■>------------------------]"),
                getOutputString(output)
        );
    }

    @Test
    public void testClear()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 50);
        bar.start();
        bar.setProgress(25);
        bar.clear();

        Assert.assertEquals(
                generateOutput("  0/50 [>---------------------------]   0%") +
                generateOutput(" 25/50 [==============>-------------]  50%") +
                generateOutput("                                          "),
                getOutputString(output)
        );
    }

    @Test
    public void testPercentNotHundredBeforeComplete()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 200);
        bar.start();
        bar.display();
        bar.advance(199);
        bar.advance();

        Assert.assertEquals(
                generateOutput("   0/200 [>---------------------------]   0%") +
                generateOutput("   0/200 [>---------------------------]   0%") +
                generateOutput(" 199/200 [===========================>]  99%") +
                generateOutput(" 200/200 [============================] 100%"),
                getOutputString(output)
        );
    }

    @Test
    public void testNonDecoratedOutput()
    {
        StreamOutput output = getOutputStream(false, Verbosity.NORMAL);
        ProgressBar bar = new ProgressBar(output, 200);
        bar.start();

        for (int i = 0; i < 200; i++) {
            bar.advance();
        }

        bar.finish();

        Assert.assertEquals(
                "   0/200 [>---------------------------]   0%\n" +
                "  20/200 [==>-------------------------]  10%\n" +
                "  40/200 [=====>----------------------]  20%\n" +
                "  60/200 [========>-------------------]  30%\n" +
                "  80/200 [===========>----------------]  40%\n" +
                " 100/200 [==============>-------------]  50%\n" +
                " 120/200 [================>-----------]  60%\n" +
                " 140/200 [===================>--------]  70%\n" +
                " 160/200 [======================>-----]  80%\n" +
                " 180/200 [=========================>--]  90%\n" +
                " 200/200 [============================] 100%",
                getOutputString(output)
        );
    }

    @Test
    public void testNonDecoratedOutputWithClear()
    {
        StreamOutput output = getOutputStream(false, Verbosity.NORMAL);
        ProgressBar bar = new ProgressBar(output, 50);
        bar.start();
        bar.setProgress(25);
        bar.clear();
        bar.setProgress(50);
        bar.finish();

        Assert.assertEquals(
                "  0/50 [>---------------------------]   0%\n" +
                " 25/50 [==============>-------------]  50%\n" +
                " 50/50 [============================] 100%",
                getOutputString(output)
        );
    }

    @Test
    public void testNonDecoratedOutputWithoutMax()
    {
        StreamOutput output = getOutputStream(false, Verbosity.NORMAL);
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.advance();

        Assert.assertEquals(
                "    0 [>---------------------------]\n" +
                "    1 [->--------------------------]",
                getOutputString(output)
        );
    }

    @Test
    public void testParallelBars()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar1 = new ProgressBar(output, 2);
        ProgressBar bar2 = new ProgressBar(output, 3);
        bar2.setProgressCharacter("#");
        ProgressBar bar3 = new ProgressBar(output);

        bar1.start();
        output.write("\n");
        bar2.start();
        output.write("\n");
        bar3.start();

        for (int i = 1; i <= 3; i++) {
            // up two lines
            output.write("\033[2A");
            if (i <= 2) {
                bar1.advance();
            }
            output.write("\n");
            bar2.advance();
            output.write("\n");
            bar3.advance();
        }
        output.write("\033[2A");
        output.write("\n");
        output.write("\n");
        bar3.finish();

        Assert.assertEquals(
                generateOutput(" 0/2 [>---------------------------]   0%") + "\n" +
                generateOutput(" 0/3 [#---------------------------]   0%") + "\n" +
                StringUtils.rtrim(generateOutput("    0 [>---------------------------]")) +

                "\033[2A" +
                generateOutput(" 1/2 [==============>-------------]  50%") + "\n" +
                generateOutput(" 1/3 [=========#------------------]  33%") + "\n" +
                StringUtils.rtrim(generateOutput("    1 [->--------------------------]")) +

                "\033[2A" +
                generateOutput(" 2/2 [============================] 100%") + "\n" +
                generateOutput(" 2/3 [==================#---------]  66%") + "\n" +
                StringUtils.rtrim(generateOutput("    2 [-->-------------------------]")) +

                "\033[2A" +
                "\n" +
                generateOutput(" 3/3 [============================] 100%") + "\n" +
                StringUtils.rtrim(generateOutput("    3 [--->------------------------]")) +

                "\033[2A" +
                "\n" +
                "\n" +
                StringUtils.rtrim(generateOutput("    3 [============================]")),
                getOutputString(output)
        );
    }

    @Test
    public void testWithoutMax()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.start();
        bar.advance();
        bar.advance();
        bar.advance();
        bar.finish();

        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]") +
                generateOutput("    1 [->--------------------------]") +
                generateOutput("    2 [-->-------------------------]") +
                generateOutput("    3 [--->------------------------]") +
                generateOutput("    3 [============================]"),
                getOutputString(output)
        );
    }

    @Test
    public void testAddingPlaceholderFormatter()
    {
        ProgressBar.setPlaceholderFormatter("remaining_steps", new PlaceholderFormatter()
        {
            @Override
            public String format(ProgressBar bar, Output output)
            {
                return String.valueOf(bar.getMaxSteps() - bar.getProgress());
            }
        });

        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 3);
        bar.setFormat(" %remaining_steps% [%bar%]");

        bar.start();
        bar.advance();
        bar.finish();

        Assert.assertEquals(
                generateOutput(" 3 [>---------------------------]") +
                generateOutput(" 2 [=========>------------------]") +
                generateOutput(" 0 [============================]"),
                getOutputString(output)
        );
    }

    @Test
    public void testMultilineFormat()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 3);
        bar.setFormat("%bar%\nfoobar");

        bar.start();
        bar.advance();
        bar.clear();
        bar.finish();

        Assert.assertEquals(
                generateOutput(">---------------------------\nfoobar") +
                generateOutput("=========>------------------\nfoobar                      ") +
                generateOutput("                            \n                            ") +
                generateOutput("============================\nfoobar                      "),
                getOutputString(output)
        );
    }

    @Test
    public void testAnsiColorsAndEmojis()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output, 15);
        ProgressBar.setPlaceholderFormatter("memory", new PlaceholderFormatter()
        {
            private int i = 0;

            @Override
            public String format(ProgressBar bar, Output output)
            {
                long mem = 100000 * i++;
                String colors ="44;37";

                return "\033[" + colors  + "m " + AbstractHelper.formatMemory(mem) + " \033[0m";
            }
        });
        bar.setFormat(" \033[44;37m %title:-37s% \033[0m\n %current%/%max% %bar% %percent:3s%%\n \uD83C\uDFC1  %remaining:-10s% %memory:37s%");
        String done = "\033[32m●\033[0m";
        bar.setBarCharacter(done);
        String empty = "\033[31m●\033[0m";
        bar.setEmptyBarCharacter(empty);
        String progress = "\033[32m➤ \033[0m";
        bar.setProgressCharacter(progress);

        bar.setMessage("Starting the demo... fingers crossed", "title");
        bar.start();
        bar.setMessage("Looks good to me...", "title");
        bar.advance(4);
        bar.setMessage("Thanks, bye", "title");
        bar.finish();

        Assert.assertEquals(
                generateOutput(
                        " \033[44;37m Starting the demo... fingers crossed  \033[0m\n" +
                        "  0/15 " + progress + StringUtils.padRight("", 26, empty) + "   0%\n" +
                        " \uD83C\uDFC1  1 sec                          \033[44;37m 0 B \033[0m"
                ) +
                generateOutput(
                        " \033[44;37m Looks good to me...                   \033[0m\n" +
                        "  4/15 " + StringUtils.padRight("", 7, done)  + progress + StringUtils.padRight("", 19, empty) + "  26%\n" +
                        " \uD83C\uDFC1  1 sec                       \033[44;37m 97 KiB \033[0m"
                ) +
                generateOutput(
                        " \033[44;37m Thanks, bye                           \033[0m\n" +
                        " 15/15 " + StringUtils.padRight("", 28, done) + " 100%\n" +
                        " \uD83C\uDFC1  1 sec                      \033[44;37m 195 KiB \033[0m"
                ),
                getOutputString(output)
        );
    }

    @Test
    public void testSetFormat()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.setFormat("normal");
        bar.start();
        Assert.assertEquals(
                generateOutput("    0 [>---------------------------]"),
                getOutputString(output)
        );

        output = getOutputStream();
        bar = new ProgressBar(output, 10);
        bar.setFormat("normal");
        bar.start();
        Assert.assertEquals(
                generateOutput("  0/10 [>---------------------------]   0%"),
                getOutputString(output)
        );
    }

    @Test
    public void testFormatsWithoutMax()
    {
        StreamOutput output = getOutputStream();
        ProgressBar bar = new ProgressBar(output);
        bar.setFormat("normal");
        bar.start();
        Assert.assertNotEquals("", getOutputString(output));

        output = getOutputStream();
        bar = new ProgressBar(output);
        bar.setFormat("verbose");
        bar.start();
        Assert.assertNotEquals("", getOutputString(output));

        output = getOutputStream();
        bar = new ProgressBar(output);
        bar.setFormat("very_verbose");
        bar.start();
        Assert.assertNotEquals("", getOutputString(output));

        output = getOutputStream();
        bar = new ProgressBar(output);
        bar.setFormat("debug");
        bar.start();
        Assert.assertNotEquals("", getOutputString(output));
    }

    private StreamOutput getOutputStream()
    {
        return new StreamOutput(new ByteArrayOutputStream());
    }

    private StreamOutput getOutputStream(boolean decorated, Verbosity verbosity)
    {
        return new StreamOutput(new ByteArrayOutputStream(), verbosity, decorated);
    }
    
    private String getOutputString(StreamOutput output)
    {
        return new String(((ByteArrayOutputStream) output.getStream()).toByteArray());
    }

    private String generateOutput(String expected)
    {
        int count = StringUtils.count(expected, '\n');

        return "\r" + (count > 0 ? String.format("\033[%dA", count) : "") + expected;
    }
}
