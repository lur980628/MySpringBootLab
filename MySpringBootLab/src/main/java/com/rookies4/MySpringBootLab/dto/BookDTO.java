package com.rookies4.MySpringBootLab.dto;

import com.rookies4.MySpringBootLab.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class BookDTO {

    // 도서 생성 요청을 위한 DTO
    @Getter
    @Setter
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        @Pattern(regexp = "^(978|979)[0-9]{10}$", message = "유효하지 않은 ISBN 형식입니다. (예: 9781234567890)")
        private String isbn;

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수 입력 항목입니다.")
        @PastOrPresent(message = "출판일은 현재 또는 과거 날짜여야 합니다.")
        private LocalDate publishDate;

        public Book toEntity() {
            return new Book(this.title, this.author, this.isbn, this.price, this.publishDate);
        }
    }

    // 도서 정보 업데이트 요청을 위한 DTO
    @Getter
    @Setter
    public static class BookUpdateRequest {
        private String title;
        private String author;
        private Integer price;
        private LocalDate publishDate;
    }

    // 클라이언트에게 반환될 도서 응답 DTO
    @Getter
    @Setter
    @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public static BookResponse fromEntity(Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}