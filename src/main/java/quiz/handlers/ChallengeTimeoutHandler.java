package quiz.handlers;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
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
    
    public void cancelTimeout() {
        if (timeoutTask != null && !timeoutTask.isDone()) {
            timeoutTask.cancel(false);
        }
    }
}
