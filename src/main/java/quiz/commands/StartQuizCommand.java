package quiz.commands;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import quiz.handlers.ChallengeTimeoutHandler;

public class StartQuizCommand extends ListenerAdapter {

    private final ChallengeTimeoutHandler timeoutHandler;

    public StartQuizCommand(ChallengeTimeoutHandler timeoutHandler) {
        this.timeoutHandler = timeoutHandler;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("start-quiz")) {
            User opponent = event.getOption("opponent").getAsUser();
            String opponentId = opponent.getId();
            String challengerId = event.getUser().getId();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Quiz Challenge");
            embedBuilder.setDescription(event.getUser().getAsMention()
                    + " has challenged you into a quiz challenge.");
            embedBuilder.setColor(Color.GRAY);

            Button acceptButton = Button.success("accept_quiz:" + challengerId + ":" + opponentId, "Game on");
            Button declineButton = Button.danger("decline_quiz:" + challengerId + ":" + opponentId, "Not now");

            event.reply("Challenge for " + opponent.getAsMention()).addEmbeds(embedBuilder.build())
                    .addActionRow(acceptButton, declineButton).queue(hook -> {
                hook.retrieveOriginal().queue(message -> {
                    timeoutHandler.handleTimeout(message, opponent);
                });
            });
        }
    }
}
