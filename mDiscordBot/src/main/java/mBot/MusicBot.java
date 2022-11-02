package mBot;

import commands.CmdHello;
import commands.CmdPlay;
import io.github.cdimascio.dotenv.Dotenv;
import jdacommands.JDACommands;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.Collection;

public class MusicBot {

    public static void main(String[] args) {

        JDACommands jdaCommands = new JDACommands("!");
        jdaCommands.registerCommand(new CmdHello());
        jdaCommands.registerCommand(new CmdPlay());


        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Am i a robot?"));
        builder.enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT);
        ShardManager shardManager = builder.build();

        // Register a new event listener
        //shardManager.addEventListener(new EventListener());
        shardManager.addEventListener(jdaCommands);
    }
}