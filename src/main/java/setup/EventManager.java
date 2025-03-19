package setup;

import buttons.HelpButtonHandler;
import buttons.MoreDetailsButtonHandler;
import commands.AddDateCommand;
import commands.DatesCommand;
import commands.HelpCommand;
import events.CartPriceEvent;
import events.ClearCartEvent;
import events.DeleteDateEvent;
import events.ForwardMessagesEvent;
import giftcards.commands.GiftCardCommand;
import giftcards.events.GiftCardEvent;
import net.dv8tion.jda.api.JDA;
import quiz.buttons.ChallengeButtonsHandler;
import quiz.commands.StartQuizCommand;
import quiz.events.AddSetEvent;
import quiz.events.AnswerEvent;
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
                new AddSetEvent()
        );
    }
}
