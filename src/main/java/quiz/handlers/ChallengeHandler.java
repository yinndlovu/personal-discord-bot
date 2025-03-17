
package quiz.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ChallengeHandler {
    
    private JDA jda;
    private String challengerId;
    private String opponentId;
    private TextChannel channel;
    
    public ChallengeHandler(JDA jda, String challengerId, String opponentId, TextChannel channel) {
        this.jda = jda;
        this.challengerId = challengerId;
        this.opponentId = opponentId;
        this.channel = channel;
    }
    public void sendChallengeMessage() {
        User opponent = jda.getUserById(opponentId);
        
        if (opponent == null) {
            System.out.println("Opponent was not found.");
            return;
        }
        Button acceptButton = Button.success("accept_quiz", "Accept");
        Button declineButton = Button.danger("decline_quiz", "Decline");
        
        
    }
}
