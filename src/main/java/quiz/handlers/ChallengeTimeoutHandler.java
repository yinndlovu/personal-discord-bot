public class ChallengeTimeoutHandler {

	public void handleTimeout(Message message, User opponent) {
		// timeout the challenge when the opponent doesn't accept
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		executor.schedule(() -> {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("Challenge Timeout");
			embedBuilder.setDescription("Challenge for " + opponent.getAsMention() + " has expired.");
			embedBuilder.setColor(Color.RED);


			message.editMessageEmbeds(embedBuilder.build())
			.setActionRow(
				Button.primary("accept_quiz", "Game on").asDisabled(),
				Button.danger("decline_quiz", "Not now").asDisabled()
			).queue();
		}, 1, TimeUnit.MINUTES); // delay before timing out
}