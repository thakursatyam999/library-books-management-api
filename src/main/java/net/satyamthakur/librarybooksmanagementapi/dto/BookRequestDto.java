package net.satyamthakur.librarybooksmanagementapi.dto;

public class BookRequestDto {
    private Long isbn;
    private String title;
    private String author;
    private int price;

    public BookRequestDto(){

    }

    public BookRequestDto(Long isbn,String title,String author,int price){
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.price=price;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
