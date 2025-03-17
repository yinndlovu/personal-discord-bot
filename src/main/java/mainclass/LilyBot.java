package mainclass;

import buttons.*;
import commands.*;
import essentials.Config;
import events.*;
import tasks.GiftCardScheduler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import tasks.DateReminderScheduler;

public class LilyBot {

    private static final Config config = new Config();

    public static void main(String[] args) throws Exception {

        String token = config.getBotToken();

        JDABuilder builder = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES)
                .enableIntents(GatewayIntent.DIRECT_MESSAGE_TYPING);

        builder.addEventListeners(
                new GiftCardEvent(),
                new ForwardMessagesEvent(),
                new ClearCartEvent(),
                new CartPriceEvent(),
                new HelpCommand(),
                new GiftCardCommand(),
                new HelpButtonHandler(),
                new MoreDetailsButtonHandler(),
                new AddDateCommand(),
                new DatesCommand(),
                new DeleteDateEvent());

        JDA jda = builder.build();
        jda.awaitReady();

        DateReminderScheduler scheduler = new DateReminderScheduler(jda);
        scheduler.startScheduler();
        new GiftCardScheduler(jda);
 
        jda.updateCommands().addCommands(Commands.slash("change", "Change your monthly item")
                .addOption(OptionType.STRING, "new_item", "What do you want to change to?", true),
                Commands.slash("take-gift-card", "Take a stored gift card"),
                Commands.slash("help", "Get help with stuff"),
                Commands.slash("add-date", "Add a date to your important dates")
                        .addOption(OptionType.STRING, "description", "What is the date name/about?", true)
                        .addOption(OptionType.INTEGER, "day", "What day is it?", true)
                        .addOption(OptionType.INTEGER, "month", "What month is it?", true)
                        .addOption(OptionType.INTEGER, "year", "What year is it?", true)
                        .addOption(OptionType.STRING, "emoji", "Add a decorative emoji if you want", false),
                Commands.slash("dates", "View all your saved important dates"),
                Commands.slash("start-quiz", "Play a quiz game against each other")
                        .addOption(OptionType.USER, "opponent", "Who are you playing against?", true)).queue();
    }
}
