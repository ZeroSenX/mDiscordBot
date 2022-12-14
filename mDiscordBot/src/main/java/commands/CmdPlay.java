package commands;

import jdacommands.*;
import lavaplayer.*;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class CmdPlay implements ICommandExecutor {
    @Override
    public void execute(Command event) {
        System.out.println("executing play " + event.getMessage().getContentRaw());

        if(!event.getMemberVoiceState().inAudioChannel()){
            System.out.println("You have to be in a voice channel for this command to work");
        }
        if(!event.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMemberVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }

        ArrayList<String> search = new ArrayList<>(Arrays.asList(event.getArgs()));
        search.remove(0);
        String link = String.join(" ", search);



        if(link.length() != 0) {
            if(!isUrl(link)){
                link = "ytsearch: " + link + " audio";
            }
            new PlayerManager();
            PlayerManager.getINSTANCE().loadAndPlay(event.getTextChannel(), link);
        }
        else
            PlayerManager.getINSTANCE().setPause(event.getTextChannel(), false);
    }

    public boolean isUrl(String url){
        try{
            new URI(url);
            return true;
        }
        catch (URISyntaxException e ){
            return false;
        }
    }
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String helpMessage() {
        return "This command is to play music.";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
