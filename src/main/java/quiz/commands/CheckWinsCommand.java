package quiz.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import quiz.databases.QuizDatabaseManager;

import java.awt.*;

public class CheckWinsCommand extends ListenerAdapter {

    private final QuizDatabaseManager manager = new QuizDatabaseManager();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        int gamesPlayed, gamesWon;

        if (event.getName().equals("check-wins")) {
            OptionMapping option = event.getOption("player");

            if (option != null) {
                String checkedUser = option.getAsString();

                gamesPlayed = manager.getGamesPlayed(checkedUser);
                gamesWon = manager.getWinCount(checkedUser);
            } else {
                String user = event.getUser().getId();

                gamesPlayed = manager.getGamesPlayed(user);
                gamesWon = manager.getWinCount(user);
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Quiz Stats");
            embedBuilder.setDescription("Games played: " + gamesPlayed + "\n\n" +
                    "Games won: " + gamesWon);
            embedBuilder.setColor(Color.CYAN);

            event.reply("").addEmbeds(embedBuilder.build()).queue();
        }
    }
}
