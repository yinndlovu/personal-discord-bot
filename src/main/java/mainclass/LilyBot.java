package mainclass;

import essentials.Config;
import giftcards.tasks.GiftCardScheduler;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import quiz.handlers.ChallengeTimeoutHandler;
import setup.*;
import tasks.DateReminderScheduler;

public class LilyBot {

    private static final Config config = new Config();

    public static void main(String[] args) throws Exception {

        String token = config.getBotToken();
        ChallengeTimeoutHandler timeoutHandler = new ChallengeTimeoutHandler();

        JDABuilder builder = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES)
                .enableIntents(GatewayIntent.DIRECT_MESSAGE_TYPING);

        JDA jda = builder.build();
        jda.awaitReady();

        EventManager.registerEvents(jda, timeoutHandler);
        CommandManager.registerCommands(jda);

        DateReminderScheduler dateScheduler = new DateReminderScheduler(jda);
        dateScheduler.startScheduler();
        GiftCardScheduler giftCardScheduler = new GiftCardScheduler(jda);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            giftCardScheduler.shutdown();
            dateScheduler.shutdown();
            System.out.println("Gift card scheduler has shut down.");
        }));
    }
}
