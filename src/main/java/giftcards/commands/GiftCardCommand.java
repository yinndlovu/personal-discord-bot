package giftcards.commands;

import databases.DatabaseManager;
import essentials.Config;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GiftCardCommand extends ListenerAdapter {

    private final Config config = new Config();
    private final String MY_USER_ID = config.getMyUserId();
    private final String HER_USER_ID = config.getHerUserId();
    private final DatabaseManager manager = new DatabaseManager();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("take-gift-card")) {
            if (event.getUser().getId().equals(HER_USER_ID)) {
                String giftCard = manager.getRegularGiftCard();

                if (!giftCard.isEmpty()) {
                    event.reply("You have successfully taken a gift card!"
                            + "\n\n"
                            + "Code: **" + giftCard + "**").queue();

                    String message = "A gift card has been retrieved.";

                    // alert me
                    event.getJDA().retrieveUserById(MY_USER_ID).queue(user -> {
                        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
                    });
                    
                    // delete the fetched gift card
                    manager.deleteRegularGiftCard(giftCard);
                } else {
                    event.reply("Sorry! There are not any gift cards stored at the moment.").queue();

                    String message = "There has been an unsuccessful gift card retrieval attempt.";

                    // alert me
                    event.getJDA().retrieveUserById(MY_USER_ID).queue(user -> {
                        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
                    });
                }
            } else {
                event.reply("Only she can claim a gift card.").queue();
            }
        } else if (event.getName().equals("change")) {
            String channelName = event.getChannel().getName();
            String newItem = event.getOption("new_item").getAsString();

            if (event.isFromGuild()) {
                if (channelName.equalsIgnoreCase("item-change-🤍")) {
                    event.reply("You have changed your item for the next month to " + newItem).queue();

                    if (!event.getUser().getId().equals(MY_USER_ID)) {
                        System.out.println("The monthly item has been changed to " + newItem + ".");

                        String message = "Monthly item successfully configured to: " + newItem;
                        event.getJDA().retrieveUserById(MY_USER_ID).queue(user -> {
                            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
                        });
                    }
                } else {
                    event.reply("You can only change to the item you want in the #item-change-🤍 channel").queue();
                }
            } else {
                event.reply("You have changed your item for the next month to " + newItem).queue();

                if (!event.getUser().getId().equals(MY_USER_ID)) {
                    System.out.println("The monthly item has been changed to " + newItem + ".");

                    String message = "Monthly item successfully configured to: " + newItem;
                    event.getJDA().retrieveUserById(MY_USER_ID).queue(user -> {
                        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
                    });
                }
            }
        }
    }
}
