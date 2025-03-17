
package quiz.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChallengeButtonsHandler extends ListenerAdapter {
    
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String compId = event.getComponentId();
        
        if (compId.equals("accept_quiz")) {
            
        } else if (compId.equals("reject_quiz")) {
            
        }
    }
}
