package com.rookies4.MySpringBootLab.service;

import com.rookies4.MySpringBootLab.dto.BookDTO;
import com.rookies4.MySpringBootLab.entity.Book;
import com.rookies4.MySpringBootLab.exception.BusinessException;
import com.rookies4.MySpringBootLab.repository.BookRepository;
import com.rookies4.MySpringBootLab.dto.BookDTO;
import com.rookies4.MySpringBootLab.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional // 쓰기 작업에만 별도로 트랜잭션 설정
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        // ISBN 중복 검증 로직
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new BusinessException("이미 등록된 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }
        Book newBook = request.toEntity();
        Book savedBook = bookRepository.save(newBook);
        return BookDTO.BookResponse.fromEntity(savedBook);
    }

    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.fromEntity(book);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("ISBN이 " + isbn + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.fromEntity(book);
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        
        // 변경이 필요한 필드만 업데이트
        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }
        
        Book updatedBook = bookRepository.save(existBook);
        return BookDTO.BookResponse.fromEntity(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}