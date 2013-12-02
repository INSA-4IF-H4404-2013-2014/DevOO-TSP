package Controller.Command;

import Controller.MainWindowController;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public class InverseCommand extends Command {

    private Command command;

    void Apply() {
        command.Reverse();
    }

    void Reverse() {
        command.Apply();
    }

    public InverseCommand(Command command) {
        super(command.getController());
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
