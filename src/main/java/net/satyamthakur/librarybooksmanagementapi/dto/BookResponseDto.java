package net.satyamthakur.librarybooksmanagementapi.dto;

public class BookResponseDto {
    private Long isbn;
    private String title;
    private String author;
    private int price;
    private boolean available;

    public BookResponseDto(){

    }

    public BookResponseDto(Long isbn,String title,String author,int price,boolean available){
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.price=price;
        this.available=available;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
