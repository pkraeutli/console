Astina Console
==============

Java port of the famous [Symfony Console](http://symfony.com/doc/current/components/console/index.html).

### Usage

Create your command classes by extending the `ch.astina.console.Command` base class:

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

        int exitCode = app.run(args);

        System.exit(exitCode);
    }
}
```

Run the commands:

`$ java -jar console.jar [arguments] [options]`

