package at.shortydev.bot.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServerSettings {

    private long id;
    private long botJoined;
    private String prefix;
    private int commandsUsed;
}