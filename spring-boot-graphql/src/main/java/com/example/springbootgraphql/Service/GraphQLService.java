package com.example.springbootgraphql.Service;

import com.example.springbootgraphql.Service.DataFetcher.AddBooksDataFetcher;
import com.example.springbootgraphql.Service.DataFetcher.AllBooksDataFetcher;
import com.example.springbootgraphql.Service.DataFetcher.BookDataFetcher;
import com.example.springbootgraphql.model.Book;
import com.example.springbootgraphql.repository.BookRepository;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    @Value("classpath:books.graphql")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    @Autowired
    private AddBooksDataFetcher addBooksDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        
        loadDatainHSQL();
        
        File schemaFile = resource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry  = new SchemaParser().parse(schemaFile);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);
        graphQL =  GraphQL.newGraphQL(graphQLSchema).build();
    }

    private void loadDatainHSQL() {

        List<String> authors = new ArrayList<>();
        authors.add("Roger s pressman");
        authors.add("Peter bare galvin");
        Stream.of(
            new Book("1","SE","Test",authors,"date"),
                new Book("123","OS","MAcgrawhill",authors,"12-july-2019"),
                new Book("tt2t","DBMS","TAklj",authors,"19-may-2019")
        ).forEach(book->{
            bookRepository.save(book);
        });
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query",typeWiring->
                    typeWiring.
                            dataFetcher("allBooks",allBooksDataFetcher)
                            .dataFetcher("book",bookDataFetcher))
                .type("Mutation", typeWiring ->
                        typeWiring.dataFetcher("newBook", addBooksDataFetcher))
                .build();

    }

    public GraphQL getGraphQL(){
        return graphQL;
    }
}
