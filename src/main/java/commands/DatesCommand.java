package commands;

import data.DateEntry;
import databases.DatesDBManager;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class DatesCommand extends ListenerAdapter {

    private final DatesDBManager dbManager = new DatesDBManager();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        try {
            if (event.getName().equals("dates")) {
                event.deferReply().queue();
                List<DateEntry> dates = dbManager.getDates();

                if (dates.isEmpty()) {
                    event.getHook().editOriginal("You haven't added any dates yet. Use `add-date` to "
                            + "start adding.").queue();
                    return;
                }

                StringBuilder message = new StringBuilder("**List of your saved important dates:**\n\n");
                LocalDate today = LocalDate.now(ZoneOffset.UTC);
                DateEntry upcomingDate = null;
                long daysUntilUpcoming = Long.MAX_VALUE;

                for (int i = 0; i < dates.size(); i++) {
                    String description = dates.get(i).getDescription();
                    LocalDate date = dates.get(i).getDate();
                    String emoji = dates.get(i).getEmoji();

                    message.append(i + 1).append(". ").append(description.substring(0, 1).toUpperCase())
                            .append(description.substring(1)).append(": ")
                            .append(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy ")))
                            .append(emoji)
                            .append("\n");

                    LocalDate nextOccurence = date.withYear(today.getYear());
                    if (nextOccurence.isBefore(today) || nextOccurence.isEqual(today)) {
                        nextOccurence = nextOccurence.plusYears(1);
                    }

                    long daysUntil = ChronoUnit.DAYS.between(today, nextOccurence);
                    if (daysUntil < daysUntilUpcoming) {
                        upcomingDate = dates.get(i);
                        daysUntilUpcoming = daysUntil;
                    }
                }

                if (upcomingDate != null && daysUntilUpcoming <= 31) {
                    message.append("\n**⏳ Upcoming ⏳**\n\n");

                    message.append(upcomingDate.getDescription().substring(0, 1).toUpperCase())
                            .append(upcomingDate.getDescription().substring(1))
                            .append("\n")
                            .append(upcomingDate.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy\n")));
                    message.append("-# (in ").append(daysUntilUpcoming).append(" days)");
                }
                event.getHook().editOriginal(message.toString())
                        .setActionRow(Button.secondary("more_details", "Detailed Dates"))
                        .queue(sentMessage -> {
                            scheduler.schedule(() -> {
                                sentMessage.editMessageComponents().queue();
                            }, 2, TimeUnit.MINUTES);
                        });
            }
        } catch (ErrorResponseException ex) {
            System.out.println(ex.getMessage());
            event.getHook().editOriginal("It took longer than expected to get the dates. Please try again.").queue();
        }
    }
}
