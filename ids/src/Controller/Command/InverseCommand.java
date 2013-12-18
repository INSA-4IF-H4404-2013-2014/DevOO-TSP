package controller.command;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public class InverseCommand extends Command {

    /** the command to inverse */
    private Command command;

    /**
     * implements command.Apply() to reverse the command to inverse
     */
    public void Apply() {
        command.Reverse();
    }

    /**
     * implements command.Reverse() to Apply the command to inverse
     */
    public void Reverse() {
        command.Apply();
    }

    /**
     * Constructor
     * @param command the command to inverse
     */
    public InverseCommand(Command command) {
        super(command.getController());
        this.command = command;
    }

    /**
     * Gets the command to inverse
     * @return the command to inverse
     */
    public Command getCommand() {
        return command;
    }
}
