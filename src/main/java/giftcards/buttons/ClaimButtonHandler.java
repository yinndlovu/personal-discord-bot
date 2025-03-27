package giftcards.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ClaimButtonHandler extends ListenerAdapter {

    private final String HER_USER_ID = "";

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getComponentId().equals("claim_button")) {
            if (event.getUser().getId().equals(HER_USER_ID)) {
                String giftCard = "".toUpperCase(); // store the gift card

                event.getChannel().sendTyping().queue();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

                event.getChannel().sendMessage("**" + giftCard + "**").queue();
            } else {
                event.getChannel().sendTyping().queue();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

                event.getChannel().sendMessage("Hoooold your horses bucko! Only <@" + HER_USER_ID + "> "
                        + "can claim the gift card.").queue();
            }
        }
    }
}
