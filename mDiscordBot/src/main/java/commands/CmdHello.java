package commands;

import jdacommands.Command;
import jdacommands.ICommandExecutor;

public class CmdHello implements ICommandExecutor {
    @Override
    public void execute(Command event) {
        event.getTextChannel().sendMessage("Hello World").queue();
    }

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String helpMessage() {
        return "A command to say hello";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
