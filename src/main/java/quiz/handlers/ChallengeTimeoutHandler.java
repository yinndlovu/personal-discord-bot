package quiz.handlers;

import java.awt.Color;
import java.util.concurrent.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ChallengeTimeoutHandler {

    private ScheduledFuture<?> timeoutTask;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public void handleTimeout(Message message, User opponent) {
        // timeout the challenge when the opponent doesn't accept
        timeoutTask = executor.schedule(() -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Challenge Timeout");
            embedBuilder.setDescription("Challenge for " + opponent.getAsMention() + " has expired.");
            embedBuilder.setColor(Color.ORANGE);

            message.editMessageEmbeds(embedBuilder.build())
                    .setActionRow(
                            Button.primary("accept_quiz", "Game on").asDisabled(),
                            Button.danger("decline_quiz", "Not now").asDisabled()
                    ).queue();
        }, 1, TimeUnit.MINUTES); // delay before timing out
    }

    /*
    this is so that the timer doesn't keep counting and time out the embed even after
    the challenge has been accepted. not useful in functionality, just looks better
     */
    public void cancelTimeout() {
        if (timeoutTask != null && !timeoutTask.isDone()) {
            timeoutTask.cancel(false);
        }
    }
}
