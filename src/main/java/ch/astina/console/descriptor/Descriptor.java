package ch.astina.console.descriptor;

import ch.astina.console.output.Output;

public interface Descriptor
{
    public void describe(Output output, Object object);

    public void describe(Output output, Object object, DescriptorOptions options);
}
