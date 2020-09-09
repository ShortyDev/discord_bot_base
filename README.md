This code is a simple base for discord bot development with Java including an MySQL Database Connector, an Easy Embed Builder, an ServerSettings object and an Command manager.
# Create a simple embedded message with EasyEmbed
To build a simple embedded message which auto-deletes after 20 seconds you can just use this:
```java
                EasyEmbed embedBuilder = EasyEmbed.builder()
                        .title("Help - Test Bot")
                        .color(Color.GREEN)
                        .description("This is a description.")
                        .footer(EasyEmbed.Footer.builder().text("This is the footer of the embed").build())
                        .timestamp(new Date().toInstant())
                        .autoDelete(20).build();
```
This isn't enough to auto-delete a message tho. You can build the message and send it yourself (#1) or let the bot handle it for you (#2).

This sends the message in the channel and deletes it after the given amount.
###### #1
```java
                Message message = textChannel.sendMessage(embedBuilder.buildMessage()).complete();
                new DelayDelete(message, embedBuilder.getAutoDelete());
```

This does exactly the same as #1 but its easier and won't delete if you enter 0 as an auto-delete value.
###### #2
```java
                embedBuilder.buildMessageAndSend(textChannel);
```                

# Create a reaction listener with EasyReaction
To create a reaction listener you need to have a message, the emote and space for a consumer.
```java
                EasyReaction.builder()
                        .message(message)
                        .emote(textChannel.getGuild().getEmoteById("testId"))
                        .reactEvent(reaction -> textChannel.sendMessage("User " + reaction.getUserId() + " interacted with " +
                                "the reaction button! (" + reaction.getType() + " )").queue())
                        .build()
                        .registerReaction();
```
This will trigger the consumer every time someone (un-)reacts to the message with the emote provided.
The user id can be gotten with `Reaction#getUserId()` and the action performed by the user (ADD, REMOVE) with `Reaction#getType()`.
