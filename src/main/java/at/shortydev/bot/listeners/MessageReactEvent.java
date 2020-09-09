package at.shortydev.bot.listeners;

import at.shortydev.bot.reactions.EasyReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactEvent extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        for (EasyReaction easyReaction : EasyReaction.reactions) {
            if (easyReaction.getMessage() != null && easyReaction.getMessage().getId().equals(event.getMessageId())) {
                if (easyReaction.getEmote().equals(event.getReactionEmote().getEmote())) {
                    easyReaction.getReactEvent().accept(EasyReaction.Reaction.builder().userId(event.getUserId()).type(EasyReaction.Reaction.Type.ADD).build());
                }
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        for (EasyReaction easyReaction : EasyReaction.reactions) {
            if (easyReaction.getMessage() != null && easyReaction.getMessage().getId().equals(event.getMessageId())) {
                if (easyReaction.getEmote().equals(event.getReactionEmote().getEmote())) {
                    easyReaction.getReactEvent().accept(EasyReaction.Reaction.builder().userId(event.getUserId()).type(EasyReaction.Reaction.Type.REMOVE).build());
                }
            }
        }
    }
}
