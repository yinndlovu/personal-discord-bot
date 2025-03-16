package commands;

import databases.DatesDBManager;
import java.time.DateTimeException;
import java.time.LocalDate;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class AddDateCommand extends ListenerAdapter {

    private final DatesDBManager dbManager = new DatesDBManager();
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("add-date")) {
            String description = event.getOption("description").getAsString();
            int day = event.getOption("day").getAsInt();
            int month = event.getOption("month").getAsInt();
            int year = event.getOption("year").getAsInt();
            OptionMapping option = event.getOption("emoji");
            String emoji = (option != null) ? option.getAsString() : " ";

            String formattedDate = String.format("%02d-%02d-%04d", day, month, year);

            try {
                event.reply("You've successfully added the date for "
                        + description.substring(0, 1).toUpperCase() + description.substring(1)
                        + " as " + formattedDate + ".\n\n"
                        + "You may use `/dates` to view it and other dates.").queue();
                LocalDate date = LocalDate.of(year, month, day);
                
                dbManager.addDate(description, date, emoji);
            } catch (DateTimeException ex) {
                event.reply("Date is invalid. Enter the proper date. All dates are written in numbers.").queue();
                System.out.println(ex.getMessage());
            }
        }
    }
}
