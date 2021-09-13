package com.learnreactivespring;


import com.learnreactivespring.modal.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class RepTest {
    @Autowired
    private ItemReactiveRepository repository;

    @BeforeEach
    public void setUp(){
        repository.deleteAll()
                .thenMany(Flux.fromIterable(Arrays.asList(new Item(), new Item())))
                .flatMap(repository::save)
                .doOnNext(System.out::println)
                .blockLast();

    }

    @Test
    public void getAllItems(){
        StepVerifier.create(repository.findAll())
                    .expectSubscription()
                    .expectNextCount(0)
                    .verifyComplete();
    }

    @Test
    public void saveOne(){
        repository.save(new Item())
                  .subscribe(s -> System.out.println(s.getDescription()));
        repository.findByDescription("abc")
                  .map(i -> {
                      i.setPrice(25d);
                      return i;
                  })
                  .flatMap(i -> {
                      return repository.save(i);
                  })
                  .subscribe(System.out::println);
    }

    @Test
    public void deleteItemById(){
        repository.findById("123")
                  .map(i -> i.getId())
                  .flatMap(id -> {
                      return repository.deleteById(id);
                  })
                  .subscribe(c -> {
                      System.out.println("Hey man, the required object has been deleted");
                  });
    }

}
