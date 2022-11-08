package commands;

import jdacommands.Command;
import jdacommands.ICommandExecutor;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class CmdPause implements ICommandExecutor {
    @Override
    public void execute(Command event) {
        System.out.println("Executing pause: " + event.getMessage().getContentRaw());
        if(!event.getMemberVoiceState().inAudioChannel()){
            System.out.println("You have to be in a voice channel for this command to work");
        }
        if(!event.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMemberVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }

        PlayerManager.getINSTANCE().setPause(event.getTextChannel(), true);
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String helpMessage() {
        return "This command is to pause music";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
