// src/main/java/com/example/demo/repository/BookRepository.java
package com.rookies4.MySpringBootLab.repository;

import com.rookies4.MySpringBootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // ISBN으로 도서 조회
    Optional<Book> findByIsbn(String isbn);

    // 저자명으로 도서 목록 조회
    List<Book> findByAuthor(String author);
}