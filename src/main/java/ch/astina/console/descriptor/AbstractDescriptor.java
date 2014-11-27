package ch.astina.console.descriptor;

import ch.astina.console.Application;
import ch.astina.console.error.InvalidArgumentException;
import ch.astina.console.command.Command;
import ch.astina.console.input.InputArgument;
import ch.astina.console.input.InputDefinition;
import ch.astina.console.input.InputOption;
import ch.astina.console.output.Output;
import ch.astina.console.output.OutputType;

public abstract class AbstractDescriptor implements Descriptor
{
    private Output output;

    @Override
    public void describe(Output output, Object object)
    {
        describe(output, object, new DescriptorOptions());
    }

    @Override
    public void describe(Output output, Object object, DescriptorOptions options)
    {
        this.output = output;

        if (object instanceof InputArgument) {
            describeInputArgument((InputArgument) object, options);
        } else if (object instanceof  InputOption) {
            describeInputOption((InputOption) object, options);
        } else if (object instanceof InputDefinition) {
            describeInputDefinition((InputDefinition) object, options);
        } else if (object instanceof Command) {
            describeCommand((Command) object, options);
        } else if (object instanceof Application) {
            describeApplication((Application) object, options);
        } else {
            throw new InvalidArgumentException(String.format("Object of type '%s' is not describable.", object.getClass()));
        }
    }

    protected void write(String message)
    {
        write(message, false);
    }

    protected void write(String message, boolean decorated)
    {
        output.write(message, false, decorated ? OutputType.NORMAL : OutputType.RAW);
    }

    abstract protected void describeInputArgument(InputArgument inputArgument, DescriptorOptions options);

    abstract protected void describeInputOption(InputOption inputOption, DescriptorOptions options);

    protected abstract void describeInputDefinition(InputDefinition inputDefinition, DescriptorOptions options);

    protected abstract void describeCommand(Command command, DescriptorOptions options);

    protected abstract void describeApplication(Application application, DescriptorOptions options);
}
