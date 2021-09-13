package com.learnreactivespring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class ReactorTest {

    @Test
    public void test1(){
        Flux.just("abc,def,ghi")
            .flatMap(s -> Flux.just(s.split(",")))
            .flatMap(s -> Flux.just(s.split("")))
             .subscribe(System.out::println);
    }


}
