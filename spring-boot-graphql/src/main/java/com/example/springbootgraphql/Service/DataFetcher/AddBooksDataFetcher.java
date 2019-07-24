package com.example.springbootgraphql.Service.DataFetcher;

import com.example.springbootgraphql.model.Book;
import com.example.springbootgraphql.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddBooksDataFetcher implements DataFetcher<Book> {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {
        String isbn = dataFetchingEnvironment.getArgument("isbn");
        String title = dataFetchingEnvironment.getArgument("title");
        String publisher = dataFetchingEnvironment.getArgument("publisher");
        String publishedDate = dataFetchingEnvironment.getArgument("publishedDate");
        List<String> authors = dataFetchingEnvironment.getArgument("authors");
        Book book = new Book(isbn,title,publisher,authors,publishedDate);
        return bookRepository.save(book);
    }
}
