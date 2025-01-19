package model;

public class Comment {
    private String text;
    private String author;

    public Comment() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String name) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
