package net.satyamthakur.librarybooksmanagementapi.controller;

import net.satyamthakur.librarybooksmanagementapi.dto.ApiResponse;
import net.satyamthakur.librarybooksmanagementapi.dto.BookRequestDto;
import net.satyamthakur.librarybooksmanagementapi.dto.BookResponseDto;
import net.satyamthakur.librarybooksmanagementapi.dto.BookUpdateDto;
import net.satyamthakur.librarybooksmanagementapi.model.Book;
import net.satyamthakur.librarybooksmanagementapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    private BookResponseDto toResponseDto(Book book) {

        return new BookResponseDto(
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.isAvailable()
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {

        List<Book> books = bookService.getAllBooks();

        List<BookResponseDto> list = new ArrayList<>();

        for (Book book : books) {
            list.add(toResponseDto(book));
        }

        if (list.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>("No book found", list));
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable Long isbn) {

        Optional<Book> optionalBook =
                bookService.getBookByIsbn(isbn);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>("No book found", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Book found",
                        toResponseDto(optionalBook.get())
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> addNewBook(
            @RequestBody BookRequestDto book) {

        if (book.getIsbn() == null || book.getIsbn() <= 0 ||
                book.getTitle() == null || book.getTitle().isBlank() ||
                book.getAuthor() == null || book.getAuthor().isBlank() ||
                book.getPrice() <= 0) {

            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(
                            "Bad request : isbn must be greater than 0,title/author can't be blank,price must be greater than 0",
                            null
                    ));
        }

        if (bookService.existsByIsbn(book.getIsbn())) {

            return ResponseEntity.status(409)
                    .body(new ApiResponse<>(
                            "Conflict : book with provided isbn already exists",
                            null
                    ));
        }

        Book newBook = new Book();

        newBook.setIsbn(book.getIsbn());
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setPrice(book.getPrice());
        newBook.setAvailable(true);

        bookService.saveBook(newBook);

        return ResponseEntity.status(201)
                .body(new ApiResponse<>(
                        "Book added succesfully",
                        toResponseDto(newBook)
                ));
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long isbn,
            @RequestBody BookUpdateDto book) {

        Optional<Book> optionalBook =
                bookService.getBookByIsbn(isbn);

        if (optionalBook.isEmpty()) {

            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(
                            "No book found",
                            null
                    ));
        }

        if (book.getTitle() == null || book.getTitle().isBlank() ||
                book.getAuthor() == null || book.getAuthor().isBlank() ||
                book.getPrice() <= 0) {

            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(
                            "Bad request : title/author can't be blank,price must be greater than 0",
                            null
                    ));
        }

        Book existingBook = optionalBook.get();

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());

        bookService.saveBook(existingBook);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Book updated successfully",
                        toResponseDto(existingBook)
                )
        );
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable Long isbn) {

        Optional<Book> optionalBook =
                bookService.getBookByIsbn(isbn);

        if (optionalBook.isEmpty()) {

            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(
                            "No book found",
                            null
                    ));
        }

        bookService.deleteBook(isbn);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Book deleted successfully",
                        toResponseDto(optionalBook.get())
                )
        );
    }

    @PatchMapping("/{isbn}/borrow")
    public ResponseEntity<?> borrowBook(
            @PathVariable Long isbn) {

        Optional<Book> optionalBook =
                bookService.getBookByIsbn(isbn);

        if (optionalBook.isEmpty()) {

            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(
                            "No book found",
                            null
                    ));
        }

        Book book = optionalBook.get();

        if (!book.isAvailable()) {

            return ResponseEntity.status(409)
                    .body(new ApiResponse<>(
                            "Conflict : Book is already borrowed",
                            toResponseDto(book)
                    ));
        }

        book.setAvailable(false);

        bookService.saveBook(book);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Book borrowed successfully",
                        toResponseDto(book)
                )
        );
    }

    @PatchMapping("/{isbn}/return")
    public ResponseEntity<?> returnBook(
            @PathVariable Long isbn) {

        Optional<Book> optionalBook =
                bookService.getBookByIsbn(isbn);

        if (optionalBook.isEmpty()) {

            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(
                            "No book found",
                            null
                    ));
        }

        Book book = optionalBook.get();

        if (book.isAvailable()) {

            return ResponseEntity.status(409)
                    .body(new ApiResponse<>(
                            "Conflict : Book is already available",
                            toResponseDto(book)
                    ));
        }

        book.setAvailable(true);

        bookService.saveBook(book);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Book returned successfully",
                        toResponseDto(book)
                )
        );
    }
}



//@RestController
//@RequestMapping("/books")
//public class BookController {
//
//    private Map<Long, Book> map=new ConcurrentHashMap<>();
//
//    private BookResponseDto toResponseDto(Book book){
//
//        return new BookResponseDto(
//                book.getIsbn(),
//                book.getTitle(),
//                book.getAuthor(),
//                (int) book.getPrice(),
//                book.isAvailable()
//        );
//    }
//
//    @GetMapping
//    public ResponseEntity<?>getAllBooks(){
//        List<BookResponseDto>list=new ArrayList<>();
//
//        for (Book book : map.values()){
//            list.add(toResponseDto(book));
//        }
//
//        if (list.isEmpty()){
//            return ResponseEntity.status(404).body(new ApiResponse<>("No book found",list));
//        }
//
//        return ResponseEntity.status(200).body(list);
//    }
//
//    @GetMapping("/{isbn}")
//    public ResponseEntity<?> getBookByIsbn(@PathVariable Long isbn){
//        Book book = map.get(isbn);
//
//        if(book==null){
//            return ResponseEntity.status(404).body(new ApiResponse<>("No book found",null));
//        }
//
//        return ResponseEntity.status(200).body(new ApiResponse<>("Book found",toResponseDto(book)));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> addNewBook(@RequestBody BookRequestDto book){
//        if (book.getIsbn()==null ||book.getIsbn()<=0 ||
//                book.getTitle()==null || book.getTitle().isBlank() ||
//                    book.getAuthor()==null || book.getAuthor().isBlank() ||
//                        book.getPrice()<=0){
//            return ResponseEntity.status(400).body(new ApiResponse<>("Bad request : isbn must be greater than 0,title/author can't be blank,price must be greater than 0",null));
//        }
//
//        if (map.containsKey(book.getIsbn())){
//            return ResponseEntity.status(409).body(new ApiResponse<>("Conflict : book with provided isbn already exists",null));
//        }
//
//        Book newBook = new Book();
//        newBook.setIsbn(book.getIsbn());
//        newBook.setTitle(book.getTitle());
//        newBook.setAuthor(book.getAuthor());
//        newBook.setPrice(book.getPrice());
//        newBook.setAvailable(true);
//
//        map.put(newBook.getIsbn(),newBook);
//        return ResponseEntity.status(201).body(new ApiResponse<>("Book added succesfully",toResponseDto(newBook)));
//    }
//
//    @PutMapping("/{isbn}")
//    public ResponseEntity<?> updateBook(@PathVariable Long isbn, @RequestBody BookUpdateDto book){
//        Book existingBook=map.get(isbn);
//
//        if (existingBook==null){
//            return ResponseEntity.status(404).body(new ApiResponse<>("No book found",null));
//        }
//
//        if(book.getTitle()==null || book.getTitle().isBlank() ||
//                book.getAuthor()==null || book.getAuthor().isBlank() ||
//                    book.getPrice()<=0){
//            return ResponseEntity.status(400).body(new ApiResponse<>("Bad request : title/author can't be blank,price must be greater than 0",null));
//        }
//
//        existingBook.setTitle(book.getTitle());
//        existingBook.setAuthor(book.getAuthor());
//        existingBook.setPrice(book.getPrice());
//
//        map.put(isbn,existingBook);
//        return ResponseEntity.status(200).body(new ApiResponse<>("Book updated successfully",toResponseDto(existingBook)));
//    }
//
//    @DeleteMapping("/{isbn}")
//    public ResponseEntity<?> deleteBook(@PathVariable Long isbn){
//        Book deleteBook=map.get(isbn);
//
//        if (deleteBook==null){
//            return ResponseEntity.status(404).body(new ApiResponse<>("No book found",null));
//        }
//
//        map.remove(isbn);
//        return ResponseEntity.status(200).body(new ApiResponse<>("Book deleted successfully",toResponseDto(deleteBook)));
//    }
//
//    @PatchMapping("/{isbn}/borrow")
//    public ResponseEntity<?> borrowBook(@PathVariable Long isbn){
//        Book book=map.get(isbn);
//
//        if(book==null){
//            return ResponseEntity.status(404).body(new ApiResponse<>("No book found",null));
//        }
//
//        if(!book.isAvailable()){
//            return ResponseEntity.status(409).body(new ApiResponse<>("Conflict : Book is already borrowed",toResponseDto(book)));
//        }
//
//        book.setAvailable(false);
//        map.put(isbn,book);
//
//        return ResponseEntity.status(200).body(new ApiResponse<>("Book borrowed successfully",toResponseDto(book)));
//    }
//
//    @PatchMapping("/{isbn}/return")
//    public ResponseEntity<?> returnBook(@PathVariable Long isbn){
//        Book book=map.get(isbn);
//
//        if (book==null){
//            return ResponseEntity.status(404).body(new ApiResponse<>("No book found",null));
//        }
//
//        if(book.isAvailable()){
//            return ResponseEntity.status(409).body(new ApiResponse<>("Conflict : Book is already available",toResponseDto(book)));
//        }
//
//        book.setAvailable(true);
//        map.put(isbn,book);
//
//        return ResponseEntity.status(200).body(new ApiResponse<>("Book returned successfully",toResponseDto(book)));
//    }
//}
