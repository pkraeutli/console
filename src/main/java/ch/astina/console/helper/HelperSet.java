package ch.astina.console.helper;

import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.command.Command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HelperSet implements Iterable<Helper>
{
    private Map<String, Helper> helpers = new HashMap<String, Helper>();

    private Command command;

    public HelperSet()
    {
    }

    public HelperSet(List<Helper> helpers)
    {
        for (Helper helper : helpers) {
            set(helper);
        }
    }

    public void set(Helper helper)
    {
        set(helper, null);
    }

    public void set(Helper helper, String alias)
    {
        helpers.put(helper.getName(), helper);
        if (alias != null) {
            helpers.put(alias, helper);
        }

        helper.setHelperSet(this);
    }

    public boolean has(String name)
    {
        return helpers.containsKey(name);
    }

    public Helper get(String name)
    {
        if (!has(name)) {
            throw new InvalidArgumentException(String.format("The helper '%s' is not defined.", name));
        }

        return helpers.get(name);
    }

    public Command getCommand()
    {
        return command;
    }

    public void setCommand(Command command)
    {
        this.command = command;
    }

    public Iterator<Helper> iterator()
    {
        return helpers.values().iterator();
    }
}
