package pl.mineclub.bot.objects;


import net.dv8tion.jda.api.entities.MessageEmbed;

public class Ankieta {

    private MessageEmbed messageId;
    private int clickOne;
    private int clickTwo;

    private long date;

    public Ankieta(MessageEmbed messageId, int clickOne, int clickTwo, long date) {
        this.messageId = messageId;
        this.clickOne = clickOne;
        this.clickTwo = clickTwo;
        this.date = date;
    }

    public int getClickOne() {
        return clickOne;
    }

    public void setClickOne(int clickOne) {
        this.clickOne = clickOne;
    }

    public int getClickTwo() {
        return clickTwo;
    }

    public void setClickTwo(int clickTwo) {
        this.clickTwo = clickTwo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public MessageEmbed getMessageId() {
        return messageId;
    }

    public void setMessageId(MessageEmbed messageId) {
        this.messageId = messageId;
    }
}
