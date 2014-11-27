package ch.astina.console;

import ch.astina.console.command.Command;
import ch.astina.console.command.Foo1Command;
import ch.astina.console.command.FooCommand;
import ch.astina.console.command.HelpCommand;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.error.LogicException;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ApplicationTest
{
    @Test
    public void testConstructor()
    {
        Application application = new Application("foo", "bar");
        Assert.assertEquals("Constructor takes the application name as its first argument", "foo", application.getName());
        Assert.assertEquals("Constructor takes the application version as its second argument", "bar", application.getVersion());
    }

    @Test
    public void testSetGetName()
    {
        Application application = new Application();
        application.setName("foo");
        Assert.assertEquals("setName() sets the name of the application", "foo", application.getName());
    }

    @Test
    public void testSetGetVersion()
    {
        Application application = new Application();
        application.setVersion("bar");
        Assert.assertEquals("setVersion() sets the version of the application", "bar", application.getVersion());
    }

    @Test
    public void testGetLongVersion()
    {
        Application application = new Application("foo", "bar");
        Assert.assertEquals("getLongVersion() sets the long version of the application", "<info>foo</info> version <comment>bar</comment>", application.getLongVersion());
    }

    @Test
    public void testAll()
    {
        Application application = new Application();
        Map<String, Command> commands = application.all();
        Assert.assertTrue("all() returns the registered commands", commands.containsKey("help"));

        application.add(new FooCommand());
        commands = application.all("foo");
        Assert.assertEquals("all() rakes a namespace as its first argument", 1, commands.size());
    }

    @Test
    public void testRegister()
    {
        Application application = new Application();
        Command command = application.register("foo");
        Assert.assertEquals("register() registers a new command", "foo", command.getName());
    }

    @Test
    public void testAdd()
    {
        Application application = new Application();
        Command foo = new FooCommand();
        application.add(foo);
        Map<String, Command> commands = application.all();
        Assert.assertEquals("add() registers a command", foo, commands.get("foo:bar"));


        application = new Application();
        Command foo1 = new Foo1Command();
        application.addCommands(foo, foo1);
        commands = application.all();
        Assert.assertTrue("addCommands() registers a list of commands", commands.containsKey("foo:bar"));
        Assert.assertTrue("addCommands() registers a list of commands", commands.containsKey("foo:bar1"));
    }

    @Test(expected = LogicException.class)
    public void testAddCommandWithEmptyConstructor()
    {
        Application application = new Application();
        application.add(new Command());
    }

    @Test
    public void testHasGet() throws NoSuchFieldException, IllegalAccessException
    {
        Application application = new Application();
        Assert.assertTrue("has() returns true if a named command is registered", application.has("list"));
        Assert.assertFalse("has() returns false if a named command is not registered", application.has("afoobar"));

        Command foo = new FooCommand();
        application.add(foo);
        Assert.assertTrue("has() returns true if an alias is registered", application.has("afoobar"));
        Assert.assertEquals("get() returns a command by name", foo, application.get("foo:bar"));
        Assert.assertEquals("get() returns a command by alias", foo, application.get("afoobar"));

        application = new Application();
        application.add(foo);
        // simulate --help
        Field wantHelps = Application.class.getDeclaredField("wantHelps");
        wantHelps.setAccessible(true);
        wantHelps.set(application, true);
        Assert.assertSame(HelpCommand.class, application.get("foo:bar").getClass());
    }

    @Test
    public void testSilentHelp()
    {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("-h", "true");
        parameters.put("-q", "true");

        ApplicationTester tester = new ApplicationTester(application);
        tester.run(parameters, null, false, null);

        Assert.assertEquals("", tester.getDisplay());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGetInvalidCommand()
    {
        Application application = new Application();
        application.get("foofoo");
    }

    @Test
    public void testGetNamespaces()
    {
        Application application = new Application();
        application.add(new FooCommand());
        application.add(new Foo1Command());
        Assert.assertArrayEquals("getNamespaces() returns an array of unique used namespaces", new String[]{"foo"}, application.getNamespaces());
    }

    /*@Test
    public void testFindNamespace()
    {
        Application application = new Application();
        application.add(new FooCommand());
        Assert.assertEquals("findNamespace() returns the given namespace if it exists", "foo", application.findNamespace("foo"));
    }*/
}
