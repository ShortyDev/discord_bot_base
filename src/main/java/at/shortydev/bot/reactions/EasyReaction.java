package at.shortydev.bot.reactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Builder
@Getter
public class EasyReaction {

    public static final List<EasyReaction> REACTIONS = new ArrayList<>();

    private final Message message;
    private final Emote emote;
    private final Consumer<Reaction> onReact;

    public void registerReaction() {
        REACTIONS.add(this);
    }

    public void unregisterReaction() {
        REACTIONS.remove(this);
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Reaction {
        private final String userId;
        private final Type type;

        public enum Type {
            ADD, REMOVE
        }
    }
}
