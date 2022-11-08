package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManager;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManager = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
        INSTANCE = this;
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManager.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void setPause(TextChannel textChannel, boolean pause){
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        musicManager.audioPlayer.setPaused(pause);
        if(pause) textChannel.sendMessage("Music paused").queue();
        else textChannel.sendMessage("Music resumed").queue();

    }

    public void loadAndPlay(TextChannel textChannel, String trackURL){
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        if(!trackURL.equals("")) {
            this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    musicManager.scheduler.queue(audioTrack);

                    StringBuilder s = new StringBuilder();
                    s.append("Adding to the queue **")
                            .append(audioTrack.getInfo().title)
                            .append("'** by **'")
                            .append(audioTrack.getInfo().author)
                            .append("'**");

                    textChannel.sendMessage(s).queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    final List<AudioTrack> tracks = audioPlaylist.getTracks();
                    if (!tracks.isEmpty()) {
                        musicManager.scheduler.queue(tracks.get(0));
                        StringBuilder s = new StringBuilder();
                        s.append("Adding to the queue **")
                                .append(tracks.get(0).getInfo().title)
                                .append("'** by **'")
                                .append(tracks.get(0).getInfo().author)
                                .append("'**");

                        textChannel.sendMessage(s).queue();
                    }

                }

                @Override
                public void noMatches() {

                }

                @Override
                public void loadFailed(FriendlyException e) {

                }
            });
        }
        else{
            System.out.println("sto riprendendo da !play vuoto");
            PlayerManager.getINSTANCE().setPause(textChannel, false);
        }

    }

    public static PlayerManager getINSTANCE(){
        if(INSTANCE == null){
            return new PlayerManager();
        }
        return INSTANCE;
    }

}
