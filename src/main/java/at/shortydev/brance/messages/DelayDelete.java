package at.shortydev.brance.messages;

import net.dv8tion.jda.api.entities.Message;

import java.util.Timer;
import java.util.TimerTask;

public class DelayDelete extends TimerTask {

    private static final Timer timer = new Timer(true);

    private final Message message;

    public DelayDelete(Message message, int delay) {
        this.message = message;

        timer.schedule(this, delay * 1000L);
    }

    @Override
    public void run() {
        if (message != null) {
            message.delete().queue();
        }
    }
}
