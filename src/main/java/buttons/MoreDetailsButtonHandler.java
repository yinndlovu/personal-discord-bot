package buttons;

import data.DateEntry;
import databases.DatesDBManager;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MoreDetailsButtonHandler extends ListenerAdapter {

    private final DatesDBManager dbManager = new DatesDBManager();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("more_details")) {
            event.deferEdit().queue();
            List<DateEntry> dates = dbManager.getDates();

            if (dates.isEmpty()) {
                event.getHook().editOriginal("No detailed dates to show. Use `/add-date` to start adding.").queue();
                return;
            }

            StringBuilder message = new StringBuilder("**List of your saved important dates:**\n\n");
            LocalDate today = LocalDate.now();
            long daysUntilUpcoming = Long.MAX_VALUE;
            DateEntry upcomingDate = null;

            for (int i = 0; i < dates.size(); i++) {
                String description = dates.get(i).getDescription();
                LocalDate date = dates.get(i).getDate();
                String emoji = dates.get(i).getEmoji();

                Period sincePeriod = Period.between(date, today);
                long daysSince = ChronoUnit.DAYS.between(date, today);

                LocalDate nextOccurence = date.withYear(today.getYear());
                if (nextOccurence.isBefore(today) || nextOccurence.isEqual(today)) {
                    nextOccurence = nextOccurence.plusYears(1);
                }
                Period untilPeriod = Period.between(today, nextOccurence);

                message.append(i + 1).append(". ").append(description.substring(0, 1).toUpperCase())
                        .append(description.substring(1))
                        .append(": ")
                        .append(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy ")))
                        .append(emoji).append("\n");
                if (daysSince > 0) {
                    message.append("-# It has been: ");

                    if (sincePeriod.getYears() == 1) {
                        message.append(sincePeriod.getYears()).append(" year ");
                    } else if (sincePeriod.getYears() > 1) {
                        message.append(sincePeriod.getYears()).append(" years ");
                    }

                    if (sincePeriod.getMonths() == 1) {
                        message.append(sincePeriod.getMonths()).append(" month ");
                    } else if (sincePeriod.getMonths() > 1) {
                        message.append(sincePeriod.getMonths()).append(" months ");
                    }

                    if (sincePeriod.getDays() == 1) {
                        message.append(sincePeriod.getDays()).append(" day");
                    } else {
                        message.append(sincePeriod.getDays()).append(" days");
                    }
                    message.append("\n");
                }
                message.append("-# Time until next: ");

                if (untilPeriod.getMonths() == 1) {
                    message.append(untilPeriod.getMonths()).append(" month ");
                } else if (untilPeriod.getMonths() > 1) {
                    message.append(untilPeriod.getMonths()).append(" months ");
                }

                if (untilPeriod.getDays() == 1) {
                    message.append(untilPeriod.getDays()).append(" day");
                } else {
                    message.append(sincePeriod.getDays()).append(" days");
                }
                message.append("\n\n");

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

                message.append(upcomingDate.getDescription().toUpperCase())
                        .append("\n")
                        .append(upcomingDate.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                message.append("\n")
                        .append("-# Happening in ").append(daysUntilUpcoming).append(" days");
            }
            event.getHook().editOriginal(message.toString()).queue();
        }
    }
}
