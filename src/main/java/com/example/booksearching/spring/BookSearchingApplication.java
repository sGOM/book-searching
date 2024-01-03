package com.example.booksearching.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://github.com/spring-projects/spring-boot/issues/36669
// https://discuss.elastic.co/t/caused-by-java-lang-nosuchmethoderror-void-co-elastic-clients-transport-rest-client-restclienttransport-init/339573/9
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration.class)
public class BookSearchingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSearchingApplication.class, args);
    }

}
