package ch.astina.console.helper;

import ch.astina.console.descriptor.MarkdownDescriptor;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.descriptor.Descriptor;
import ch.astina.console.descriptor.DescriptorOptions;
import ch.astina.console.descriptor.TextDescriptor;
import ch.astina.console.output.Output;

import java.util.HashMap;
import java.util.Map;

public class DescriptorHelper extends AbstractHelper
{
    private Map<String, Descriptor> descriptors = new HashMap<String, Descriptor>();

    public DescriptorHelper()
    {
        register("txt", new TextDescriptor());
        register("md", new MarkdownDescriptor());
    }

    public void describe(Output output, Object object, DescriptorOptions options)
    {
        options.set("raw_text", Boolean.FALSE.toString(), false);
        options.set("format", "txt", false);

        if (!descriptors.containsKey(options.get("format"))) {
            throw new InvalidArgumentException(String.format("Unsupported format '%s'.", options.get("format")));
        }

        Descriptor descriptor = descriptors.get(options.get("format"));
        descriptor.describe(output, object, options);
    }

    private DescriptorHelper register(String format, Descriptor descriptor)
    {
        descriptors.put(format, descriptor);

        return this;
    }

    @Override
    public String getName()
    {
        return "descriptor";
    }
}
