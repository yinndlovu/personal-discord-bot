package giftcards.tasks;

import java.time.*;
import java.util.concurrent.*;
import databases.DatabaseManager;
import essentials.Config;
import giftcards.messages.MessageProvider;
import java.awt.Color;
import java.util.Random;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.of("+8"));
        ZonedDateTime nextRun = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusMonths(1);
        }
        return Duration.between(now, nextRun).toMillis();
    }

    public void sendGiftCard() {
        try {
            LocalDate currentDate = LocalDate.now(ZoneOffset.of("+8"));
            String monthName = currentDate.getMonth().name().toLowerCase();
            String giftCard = databaseManager.retrieveGiftCard(monthName);

            if (giftCard != null && !giftCard.isEmpty()) {
                String month = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
                MessageProvider messageProvider = new MessageProvider(HER_USER_ID, month);
                String[] messages = messageProvider.getMessages();

                Random random = new Random();
                int randomIndex = random.nextInt(messages.length);
                String randomMessage = messages[randomIndex];

                TextChannel channel = jda.getTextChannelById(CHANNEL_ID);

                if (channel != null) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(month + " Gift Card");
                    embedBuilder.setDescription("Your monthly gift card is in! Click the button to claim it.");
                    embedBuilder.setFooter("Enjoy 🤍");
                    embedBuilder.setColor(Color.MAGENTA);

                    Button claimButton = Button.success("claim_button:" + monthName, "Claim");
                    MessageEmbed messageEmbed = embedBuilder.build();

                    channel.sendMessage(randomMessage).queue();
                    channel.sendMessageEmbeds(messageEmbed).setActionRow(claimButton).queue();

                    System.out.println("A gift card for " + month + " has been sent.");
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
