package setup;

import buttons.*;
import commands.*;
import events.*;
import events.ForwardMessagesEvent;
import giftcards.buttons.ClaimButtonHandler;
import giftcards.commands.GiftCardCommand;
import giftcards.events.GiftCardEvent;
import net.dv8tion.jda.api.JDA;
import quiz.buttons.ChallengeButtonsHandler;
import quiz.commands.*;
import quiz.events.*;
import quiz.handlers.ChallengeTimeoutHandler;

public class EventManager {

    public static void registerEvents(JDA jda, ChallengeTimeoutHandler timeoutHandler) {
        jda.addEventListener(
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
                new DeleteDateEvent(),
                new AnswerEvent(),
                new ChallengeButtonsHandler(timeoutHandler),
                new StartQuizCommand(timeoutHandler),
                new AddSetEvent(),
                new ClaimButtonHandler(),
                new CheckWinsCommand()
        );
    }
}
