package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import java.util.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class HelpCommand extends ListenerAdapter {

    public static final Map<Long, Integer> userPageMap = new HashMap<>();
    public static final List<List<Button>> pages = List.of(
            /*
            storing buttons in lists and putting them in separate message pages
            to avoid limitations and expand usage
            */
            List.of(
                    Button.primary("help_redeem", "How to Redeem"),
                    Button.primary("help_change", "How to change Monthly Item"),
                    Button.primary("help_monthly_exp", "How Monthly works"),
                    Button.success("button_next", "➡ Next Page")
            ),
            List.of(
                    Button.danger("button_previous", "⬅ Previous Page"),
                    Button.primary("dates_help", "How to use Dates"),
                    Button.primary("delete_dates_how", "How to delete Dates"),
                    Button.primary("storage_help", "About stored Gift Cards"),
                    Button.success("button_next", "➡ Next Page")
            ),
            List.of(
                    Button.danger("button_previous", "⬅ Previous Page"),
                    Button.danger("quiz_help", "About Quiz Games"),
                    Button.primary("commands_list", "View Available Commands")
            )
    );

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("help")) {
            userPageMap.put(event.getUser().getIdLong(), 0);

            event.reply("What do you need help with?").addComponents(ActionRow.of(pages.get(0)))
                    .setEphemeral(false).queue();
        }
    }
}
