package javase.collection;


import java.util.Objects;

public class Book implements Comparable<Book> {

    private String bookName;
    private Double bookPrica;
    private String bookAuthor;

    public Book() {
        super();
    }

    public Book(String bookName, Double bookPrica, String bookAuthor) {
        this.bookName = bookName;
        this.bookPrica = bookPrica;
        this.bookAuthor = bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Double getBookPrica() {
        return bookPrica;
    }

    public void setBookPrica(Double bookPrica) {
        this.bookPrica = bookPrica;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        Book book = (Book) o;
        return getBookName().equals(book.getBookName()) &&
                getBookPrica().equals(book.getBookPrica()) &&
                getBookAuthor().equals(book.getBookAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookName(), getBookPrica(), getBookAuthor());
    }

    @Override
    public int compareTo(Book o) {
        return (int)(o.getBookPrica() - this.getBookPrica());
    }
}
