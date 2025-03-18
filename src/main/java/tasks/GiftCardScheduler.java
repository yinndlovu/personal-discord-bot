package tasks;

import java.time.*;
import java.util.concurrent.*;
import databases.DatabaseManager;
import essentials.Config;
import java.util.Random;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class GiftCardScheduler {

    private final Config config = new Config();
    private final JDA jda;
    private final String HER_USER_ID = config.getHerUserId();
    private final String CHANNEL_ID = config.getGiftChannelId();
    private final DatabaseManager databaseManager;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public GiftCardScheduler(JDA jda) {
        this.jda = jda;
        this.databaseManager = new DatabaseManager();
        scheduleGiftCardSending();
    }

    private void scheduleGiftCardSending() {
        long initialDelay = getInitialDelay();
        long period = Duration.ofDays(30).toMillis();

        scheduler.scheduleAtFixedRate(this::sendGiftCard, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    private long getInitialDelay() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextRun = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusMonths(1);
        }
        return Duration.between(now, nextRun).toMillis();
    }

    private void sendGiftCard() {
        try {
            LocalDate currentDate = LocalDate.now(ZoneOffset.UTC);
            String month = currentDate.getMonth().name().toLowerCase();

            String giftCard = databaseManager.retrieveGiftCard(month);
            if (giftCard != null && !giftCard.isEmpty()) {

                String[] messages = {"Heyyy <@" + HER_USER_ID + ">\n\n"
                        + "Here's your gift card for " + month.substring(0, 1).toUpperCase()
                        + month.substring(1) + ": **" + giftCard.toUpperCase() + "**.",
                    
                        "Ooooh! Would you look at that?! It's the 1st of the month again and I have got"
                        + " a little something for you <@" + HER_USER_ID + "> \n\n"
                        + "**" + giftCard.toUpperCase() + "**! You know what to do 😉",
                    
                        "Happyyyy " + month.substring(0, 1).toUpperCase() + month.substring(1)
                        + "<@" + HER_USER_ID + "> 🤍🎊\n\n"
                        + "Treat yourself with this - " + "**" +giftCard.toUpperCase() + "**.",
                    
                        ""};

                Random random = new Random();
                int randomIndex = random.nextInt(messages.length);
                String randomMessage = messages[randomIndex];

                TextChannel channel = jda.getTextChannelById(CHANNEL_ID);

                if (channel != null) {
                    channel.sendMessage(randomMessage).queue();

                    System.out.println("A gift card for " + month.substring(0, 1).toUpperCase() + month.substring(1) + 
                    " has been sent.");
                } else {
                    System.out.println("There has been an issue retrieving the channel to send the gift card.");
                }
            } else {
                System.out.println("There's an issue with the gift card.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
