package ch.astina.console.input;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InputDefinitionTest
{
    protected InputArgument fooArg, barArg, foo1Arg, foo2Arg;

    protected InputOption fooOpt, barOpt, foo1Opt, foo2Opt, multiOpt;

    @Test
    public void testSetArguments() throws Exception
    {
        initializeArguments();

        List<InputArgument> arguments = new ArrayList<InputArgument>();
        arguments.add(fooArg);
        InputDefinition definition = new InputDefinition();
        definition.setArguments(arguments);
        Assert.assertArrayEquals("setArguments() sets all InputArgument objects", arguments.toArray(), definition.getArguments().toArray());

        arguments = new ArrayList<InputArgument>();
        arguments.add(barArg);
        definition.setArguments(arguments);
        Assert.assertArrayEquals("setArguments() clears all InputArgument objects", arguments.toArray(), definition.getArguments().toArray());
    }

    @Test
    public void testAddArguments() throws Exception
    {
        initializeArguments();

        List<InputArgument> argumentsA = new ArrayList<InputArgument>();
        argumentsA.add(fooArg);
        InputDefinition definition = new InputDefinition();
        definition.addArguments(argumentsA);
        Assert.assertArrayEquals("addArguments() adds InputArgument objects", argumentsA.toArray(), definition.getArguments().toArray());

        List<InputArgument> argumentsB = new ArrayList<InputArgument>();
        argumentsB.add(barArg);
        definition.addArguments(argumentsB);

        List<InputArgument> argumentsAll = new ArrayList<InputArgument>();
        argumentsAll.addAll(argumentsA);
        argumentsAll.addAll(argumentsB);

        Assert.assertArrayEquals("addArguments() does not clear existing InputArgument objects", argumentsAll.toArray(), definition.getArguments().toArray());
    }

    @Test
    public void testAddArgument() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        List<InputArgument> arguments = new ArrayList<InputArgument>();
        arguments.add(fooArg);
        Assert.assertArrayEquals("addArgument() adds an InputArgument object", definition.getArguments().toArray(), arguments.toArray());

        definition.addArgument(barArg);
        arguments.add(barArg);
        Assert.assertArrayEquals("addArgument() adds an InputArgument object", definition.getArguments().toArray(), arguments.toArray());
    }

    @Test(expected = LogicException.class)
    public void testArgumentsMustHaveDifferentNames() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        definition.addArgument(foo1Arg);
    }

    @Test(expected = LogicException.class)
    public void testArrayArgumentHasToBeLast() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(new InputArgument("fooArray", InputArgument.IS_ARRAY));
        definition.addArgument(new InputArgument("anotherBar"));
    }

    @Test(expected = LogicException.class)
    public void testRequiredArgumentCannotFollowAnOptionalOne() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        definition.addArgument(foo2Arg);
    }

    @Test
    public void testGetArgument() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        Assert.assertEquals("getArgument() returns an InputArgument by its name", fooArg, definition.getArgument("foo"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetInvalidArgument() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        definition.getArgument("bar");
    }

    @Test
    public void testHasArgument() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        Assert.assertTrue("hasArgument() returns true if an InputArgument exists for the given name", definition.hasArgument("foo"));
        Assert.assertFalse("hasArgument() returns false if no InputArgument exists for the given name", definition.hasArgument("bar"));
    }

    @Test
    public void testGetArguments() throws Exception
    {

    }

    @Test
    public void testGetArgumentCount() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(fooArg);
        Assert.assertEquals("getArgumentCount() returns the number of arguments", definition.getArgumentCount(), 1);
        definition.addArgument(barArg);
        Assert.assertEquals("getArgumentCount() returns the number of arguments", definition.getArgumentCount(), 2);
    }

    @Test
    public void testGetArgumentRequiredCount() throws Exception
    {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument(foo2Arg);
        Assert.assertEquals("getArgumentCount() returns the number of required arguments", definition.getArgumentRequiredCount(), 1);
        definition.addArgument(fooArg);
        Assert.assertEquals("getArgumentCount() returns the number of required arguments", definition.getArgumentRequiredCount(), 1);
    }

    @Test
    public void testGetArgumentDefaults() throws Exception
    {

    }

    @Test
    public void testSetOptions() throws Exception
    {

    }

    @Test
    public void testAddOptions() throws Exception
    {

    }

    @Test
    public void testAddOption() throws Exception
    {

    }

    @Test
    public void testGetOption() throws Exception
    {

    }

    @Test
    public void testHasOption() throws Exception
    {

    }

    @Test
    public void testGetOptions() throws Exception
    {

    }

    @Test
    public void testHasShortcut() throws Exception
    {

    }

    @Test
    public void testGetOptionForShortcut() throws Exception
    {

    }

    @Test
    public void testGetOptionDefaults() throws Exception
    {

    }

    @Test
    public void testGetSynopsis() throws Exception
    {

    }

    protected void initializeArguments()
    {
        fooArg = new InputArgument("foo");
        barArg = new InputArgument("bar");
        foo1Arg = new InputArgument("foo");
        foo2Arg = new InputArgument("foo2", InputArgument.REQUIRED);
    }

    protected void initializeOptions()
    {
        fooOpt = new InputOption("foo", "f");
        barOpt = new InputOption("bar", "b");
        foo1Opt = new InputOption("fooBis", "f");
        foo2Opt = new InputOption("foo", "p");
        multiOpt = new InputOption("multi", "m|mm|mmm");
    }
}