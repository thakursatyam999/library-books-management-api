package net.satyamthakur.librarybooksmanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.satyamthakur.librarybooksmanagementapi.dto.BookRequestDto;
import net.satyamthakur.librarybooksmanagementapi.dto.BookUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddNewBookSuccess() throws Exception {

        Long isbn = System.currentTimeMillis();

        BookRequestDto book =
                new BookRequestDto(isbn,
                        "1984",
                        "George Orwell",
                        500);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Book added succesfully"))
                .andExpect(jsonPath("$.data.title")
                        .value("1984"));
    }

    @Test
    void testAddDuplicateBook() throws Exception {

        Long isbn = System.currentTimeMillis();

        BookRequestDto book =
                new BookRequestDto(isbn,
                        "1984",
                        "George Orwell",
                        500);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetBookNotFound() throws Exception {

        mockMvc.perform(get("/books/999999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("No book found"));
    }

    @Test
    void testBorrowBookSuccess() throws Exception {

        Long isbn = System.currentTimeMillis();

        BookRequestDto book =
                new BookRequestDto(isbn,
                        "Atomic Habits",
                        "James Clear",
                        700);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        mockMvc.perform(patch("/books/" + isbn + "/borrow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Book borrowed successfully"))
                .andExpect(jsonPath("$.data.available")
                        .value(false));
    }

    @Test
    void testReturnBookSuccess() throws Exception {

        Long isbn = System.currentTimeMillis();

        BookRequestDto book =
                new BookRequestDto(isbn,
                        "Clean Code",
                        "Robert Martin",
                        900);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        mockMvc.perform(patch("/books/" + isbn + "/borrow"));

        mockMvc.perform(patch("/books/" + isbn + "/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available")
                        .value(true));
    }

    @Test
    void testUpdateBookSuccess() throws Exception {

        Long isbn = System.currentTimeMillis();

        BookRequestDto book =
                new BookRequestDto(isbn,
                        "Old Title",
                        "Author",
                        400);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        BookUpdateDto updateDto =
                new BookUpdateDto(
                        "New Title",
                        "New Author",
                        600
                );

        mockMvc.perform(put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title")
                        .value("New Title"));
    }

    @Test
    void testDeleteBookSuccess() throws Exception {

        Long isbn = System.currentTimeMillis();

        BookRequestDto book =
                new BookRequestDto(isbn,
                        "Delete Me",
                        "Author",
                        300);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        mockMvc.perform(delete("/books/" + isbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Book deleted successfully"));
    }
}