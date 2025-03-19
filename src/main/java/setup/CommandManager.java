package setup;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandManager {

    public static void registerCommands(JDA jda) {
        jda.updateCommands().addCommands(
                Commands.slash("change", "Change your monthly item")
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
                        .addOption(OptionType.USER, "opponent", "Who are you playing against?", true)
        ).queue();
    }
}
