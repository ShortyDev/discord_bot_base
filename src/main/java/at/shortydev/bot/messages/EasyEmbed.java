package at.shortydev.bot.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

@Builder
@Setter
public class EasyEmbed {

    private final String title;
    private final String description;
    private Author author;
    private final Color color;
    private Footer footer;
    private String imageUrl;
    private String thumbnailUrl;
    private TemporalAccessor timestamp;
    private ArrayList<MessageEmbed.Field> fields;
    @Getter
    private final int autoDelete;

    public MessageEmbed buildMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (author != null) {
            if (author.getName() != null) {
                if (author.getUrl() != null) {
                    if (author.getIconUrl() != null) {
                        embedBuilder.setAuthor(author.getName(), author.getUrl(), author.getIconUrl());
                    } else {
                        embedBuilder.setAuthor(author.getName(), author.getUrl());
                    }
                } else {
                    embedBuilder.setAuthor(author.getName());
                }
            }
        }
        embedBuilder.setColor(color);
        embedBuilder.setDescription(description);
        if (footer != null) {
            if (footer.getText() != null) {
                if (footer.getIconUrl() != null) {
                    embedBuilder.setFooter(footer.getText(), footer.getIconUrl());
                } else {
                    embedBuilder.setFooter(footer.getText());
                }
            }
        }
        if (imageUrl != null) {
            embedBuilder.setImage(imageUrl);
        }
        if (thumbnailUrl != null) {
            embedBuilder.setThumbnail(thumbnailUrl);
        }
        if (timestamp != null) {
            embedBuilder.setTimestamp(timestamp);
        }
        embedBuilder.setTitle(title);
        if (fields != null) {
            fields.forEach(embedBuilder::addField);
        }
        return embedBuilder.build();
    }

    public void buildMessageAndSend(TextChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (author != null) {
            if (author.getName() != null) {
                if (author.getUrl() != null) {
                    if (author.getIconUrl() != null) {
                        embedBuilder.setAuthor(author.getName(), author.getUrl(), author.getIconUrl());
                    } else {
                        embedBuilder.setAuthor(author.getName(), author.getUrl());
                    }
                } else {
                    embedBuilder.setAuthor(author.getName());
                }
            }
        }
        embedBuilder.setColor(color);
        embedBuilder.setDescription(description);
        if (footer != null) {
            if (footer.getText() != null) {
                if (footer.getIconUrl() != null) {
                    embedBuilder.setFooter(footer.getText(), footer.getIconUrl());
                } else {
                    embedBuilder.setFooter(footer.getText());
                }
            }
        }
        if (imageUrl != null) {
            embedBuilder.setImage(imageUrl);
        }
        if (thumbnailUrl != null) {
            embedBuilder.setThumbnail(thumbnailUrl);
        }
        if (timestamp != null) {
            embedBuilder.setTimestamp(timestamp);
        }
        embedBuilder.setTitle(title);
        if (fields != null) {
            fields.forEach(embedBuilder::addField);
        }
        Message message = textChannel.sendMessage(embedBuilder.build()).complete();
        if (autoDelete != 0) {
            new DelayDelete(message, autoDelete);
        }
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Title {
        private String title;
        private String url;
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Author {
        private String name;
        private String url;
        private String iconUrl;
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Footer {
        private String text;
        private String iconUrl;
    }
}
