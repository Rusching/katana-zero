package edu.uchicago.gerber._03objects;

import lombok.Getter;

import java.util.ArrayList;

class Message {
    @Getter
    private final String recipient;
    private final String sender;
    private ArrayList<String> messageBody;
    public Message(String recipient, String sender) {
        this.recipient = recipient;
        this.sender = sender;
        this.messageBody = new ArrayList<>();
    }

    public void append(String text) {
        this.messageBody.add(text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("From: %s \n", this.sender));
        sb.append(String.format("To: %s \n", this.recipient));
        sb.append("\n");
        for (String textLine: this.messageBody) {
            sb.append(textLine + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}

class MailBox {
    private ArrayList<Message> messages;

    public MailBox() {
        this.messages = new ArrayList<>();
    }
    public void addMessage(Message m) {
        this.messages.add(m);
    }
    public Message getMessage(int i) {
        return this.messages.get(i);
    }
    public void removeMessage(int i) {
        this.messages.remove(i);
    }

}
public class P8_16 {
    public static void main(String[] args) {
        MailBox mailBox = new MailBox();

        Message m1 = new Message("Alice", "Bob");
        m1.append("Hello this is Bob.");
        m1.append("Let's meet next wednesday!");
        m1.append("Bye.");
        System.out.print(m1);

        Message m2 = new Message("David", "Alice");
        m2.append("Hello this is Alice.");
        m2.append("How are you doing?");
        m2.append("Bye.");
        System.out.print(m2);

        Message m3 = new Message("Bob", "Chris");
        m3.append("Hello this is Chirs.");
        m3.append("Good morning!");
        m3.append("Bye.");
        System.out.print(m3);

        mailBox.addMessage(m1);
        mailBox.addMessage(m2);
        mailBox.addMessage(m3);

        System.out.print(mailBox.getMessage(1));
        mailBox.removeMessage(1);
        System.out.print(mailBox.getMessage(1));
    }
}
