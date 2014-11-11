package ch.astina.console.descriptor;

import java.util.HashMap;
import java.util.Map;

public class DescriptorOptions implements Cloneable
{
    private Map<String, String> options = new HashMap<String, String>();

    public DescriptorOptions()
    {
    }

    private DescriptorOptions(DescriptorOptions options)
    {
        this.options.putAll(options.options);
    }

    public DescriptorOptions set(String name, String value)
    {
        return set(name, value, true);
    }

    public DescriptorOptions set(String name, String value, boolean overwrite)
    {
        if (overwrite || !options.containsKey(name)) {
            options.put(name, value);
        }

        return this;
    }

    public String get(String name)
    {
        return options.get(name);
    }

    public boolean has(String name)
    {
        return options.containsKey(name);
    }
}
