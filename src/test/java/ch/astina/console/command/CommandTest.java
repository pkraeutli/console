package ch.astina.console.command;

import ch.astina.console.Application;
import ch.astina.console.CommandTester;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;
import ch.astina.console.helper.QuestionHelper;
import ch.astina.console.input.*;
import ch.astina.console.output.NullOutput;
import ch.astina.console.output.Output;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CommandTest
{
    @Test
    public void testConstructor()
    {
        Command command = new Command("foo:bar");
        Assert.assertEquals("Constructor takes the command name as its first argument", "foo:bar", command.getName());
    }

    @Test(expected = LogicException.class)
    public void testCommandNameCannotBeEmpty()
    {
        new Command();
    }

    @Test
    public void testSetApplication()
    {
        Application application = new Application();
        Command command = new TestCommand();
        command.setApplication(application);
        Assert.assertEquals("setApplication() sets the current application", application, command.getApplication());
    }

    @Test
    public void testSetGetDefinition()
    {
        Command command = new TestCommand();
        InputDefinition definition = new InputDefinition();
        command.setDefinition(definition);
        Assert.assertEquals("setDefinition() sets the current InputDefinition instance.", definition, command.getDefinition());
    }

    @Test
    public void testAddArgument()
    {
        Command command = new TestCommand();
        command.addArgument("foo");
        Assert.assertTrue("addArgument() adds an argument to the command's definition", command.getDefinition().hasArgument("foo"));
    }

    @Test
    public void testAddOption()
    {
        Command command = new TestCommand();
        command.addOption("foo");
        Assert.assertTrue("addOption() adds an option to the command's definition", command.getDefinition().hasOption("foo"));
    }

    @Test
    public void testGetNamespaceGetNameSetName()
    {
        Command command = new TestCommand();
        Assert.assertEquals("getName() returns the command name", "namespace:name", command.getName());
        command.setName("foo");
        Assert.assertEquals("setName() sets the command name", "foo", command.getName());

        command.setName("foobar:bar");
        Assert.assertEquals("setName() sets the command name", "foobar:bar", command.getName());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidCommandName()
    {
        new Command("foo:");
    }

    @Test
    public void testGetSetDescription()
    {
        Command command = new TestCommand();
        Assert.assertEquals("getDescription() returns the description", "description", command.getDescription());
        command.setDescription("description1");
        Assert.assertEquals("setDescription sets the description", "description1", command.getDescription());
    }

    @Test
    public void testGetSetHelp()
    {
        Command command = new TestCommand();
        Assert.assertEquals("getHelp() returns the help", "help", command.getHelp());
        command.setHelp("help1");
        Assert.assertEquals("setHelp sets the help", "help1", command.getHelp());
    }

    @Test
    public void testGetProcessedHelp()
    {
        Command command = new TestCommand();
        command.setHelp("The %command.name% command does...");
        Assert.assertEquals("getProcessedHelp() replaces %command.name% correctly", "The namespace:name command does...", command.getProcessedHelp());
    }

    @Test
    public void testGetSetAliases()
    {
        Command command = new TestCommand();
        Assert.assertArrayEquals(new String[]{"name"}, command.getAliases());
        command.setAliases("name1");
        Assert.assertArrayEquals(new String[]{"name1"}, command.getAliases());
    }

    @Test
    public void testGetSynopsis()
    {
        Command command = new TestCommand();
        command.addOption("foo");
        command.addArgument("foo");
        Assert.assertEquals("getSynopsis() returns the synopsis", "namespace:name [--foo] [foo]", command.getSynopsis());
    }

    @Test
    public void testGetHelper()
    {
        Application application = new Application();
        Command command = new TestCommand();
        command.setApplication(application);
        QuestionHelper helper = new QuestionHelper();
        Assert.assertEquals("getHelper() returns the correct helper", helper.getName(), command.getHelper("question").getName());
    }

    @Test
    public void testMergeApplicationDefinition()
    {
        Application application = new Application();
        application.getDefinition().addArgument(new InputArgument("foo"));
        application.getDefinition().addOption(new InputOption("bar"));

        InputDefinition definition = new InputDefinition();
        definition.addArgument(new InputArgument("bar"));
        definition.addOption(new InputOption("foo"));

        Command command = new TestCommand();
        command.setApplication(application);
        command.setDefinition(definition);

        command.mergeApplicationDefinition();
        Assert.assertTrue("mergeApplicationDefinition() merges the application arguments and the command arguments", command.getDefinition().hasArgument("foo"));
        Assert.assertTrue("mergeApplicationDefinition() merges the application arguments and the command arguments", command.getDefinition().hasArgument("bar"));
        Assert.assertTrue("mergeApplicationDefinition() merges the application options and the command options", command.getDefinition().hasOption("foo"));
        Assert.assertTrue("mergeApplicationDefinition() merges the application options and the command options", command.getDefinition().hasOption("bar"));

        command.mergeApplicationDefinition();
        Assert.assertEquals("mergeApplicationDefinition() does not try to merge twice the application arguments and options", 3, command.getDefinition().getArgumentCount());
    }

    @Test
    public void testMergeApplicationDefinitionWithoutArgsThenWithArgsAddsArgs()
    {
        Application application = new Application();
        application.getDefinition().addArgument(new InputArgument("foo"));
        application.getDefinition().addOption(new InputOption("bar"));

        Command command = new TestCommand();
        command.setApplication(application);
        command.setDefinition(new InputDefinition());

        command.mergeApplicationDefinition(false);
        Assert.assertFalse("mergeApplicationDefinition(false) does not merge the application arguments and the command arguments", command.getDefinition().hasArgument("foo"));

        command.mergeApplicationDefinition(true);
        Assert.assertTrue("mergeApplicationDefinition(true) merges the application arguments and the command arguments", command.getDefinition().hasArgument("foo"));
    }

    @Test
    public void testRunInteractive()
    {
        CommandTester tester = new CommandTester(new TestCommand());

        tester.execute(new HashMap<String, String>(), true, null, null);

        Assert.assertEquals("run() calls the interact method if the input is interactive", "interact called\nexecute called\n", tester.getDisplay());
    }

    @Test
    public void testRunNonInteractive()
    {
        CommandTester tester = new CommandTester(new TestCommand());

        tester.execute(new HashMap<String, String>(), false, null, null);

        Assert.assertEquals("run() does not call the interact method if the input is not interactive", "execute called\n", tester.getDisplay());
    }

    @Test(expected = LogicException.class)
    public void testExecuteMethodNeedsToBeOverridden()
    {
        Command command = new Command("foo");
        command.run(new ArrayInput(new String[]{}), new NullOutput());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testRunWithInvalidOption()
    {
        CommandTester tester = new CommandTester(new TestCommand());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("--bar", "true");
        tester.execute(parameters);
    }

    @Test
    public void testSetCommandExecutor()
    {
        Command command = new TestCommand();
        command.setExecutor(new CommandExecutor()
        {
            @Override
            public int execute(Input input, Output output)
            {
                output.writeln("from the code...");

                return 0;
            }
        });

        CommandTester tester = new CommandTester(command);
        tester.execute(new HashMap<String, String>());
        Assert.assertEquals("interact called\nfrom the code...\n", tester.getDisplay());
    }
}
