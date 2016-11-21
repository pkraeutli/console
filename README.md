Astina Console
==============

Java port of the famous [Symfony Console](http://symfony.com/doc/current/components/console/index.html).

*Note*: This is just an experiment. For something production ready, check out [CRaSH](http://www.crashub.org/).

**Done**
- Defining commands
- Running commands
- Basic interactions
- Text output

**Todo**
- Array arguments
- Interaction validation
- Choice interactions
- JSON, XML, ... output
- Events

### Usage

Create your command classes by extending the `ch.astina.console.command.Command` base class:

```java
import ch.astina.console.*;

public class FileListCommand extends Command
{
    @Override
    protected void configure()
    {
        this
            .setName("ls")
            .setDescription("Displays files in the given directory")
            .addArgument("dir", InputArgument.REQUIRED, "Directory name")
            .addOption("long", "l", InputOption.VALUE_NONE, "List in long format.")
            .setHelp("Some help text ...")
        ;
    }

    @Override
    protected int execute(Input input, Output output)
    {
        // todo implement

        return 0;
    }
}
```

Prepare the console:

```java

import ch.astina.console.Application;

public class Console
{
    public static void main(String[] args)
    {
        Application app = new Application();
        app.add(new FileListCommand());
        app.add((new Command("foo")).setExecutor(new CommandExecutor()
        {
            @Override
            public int execute(Input input, Output output)
            {
                output.writeln("<info>Bar!</info>");

                return 0;
            }
        }));

        int exitCode = app.run(args);

        System.exit(exitCode);
    }
}
```

Run the commands:

`$ java -jar console.jar [arguments] [options]`

