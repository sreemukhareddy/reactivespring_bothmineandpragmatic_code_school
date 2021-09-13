package com.learnreactivespring;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public ResponseEntity<Flux<String>> test(){
        return ResponseEntity.ok().body(
                Flux.just("A", "B", "C")
                        .delayElements(Duration.ofSeconds(2))
                        .flatMap(s -> Flux.just(s))

        );
    }

    @GetMapping(value = "/fluxStream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> testFluxStream(){
        return
                Flux.just("A", "B", "C")
                        .delayElements(Duration.ofSeconds(10));
                        //.flatMap(s -> Flux.just(s))


    }

    @GetMapping(value = "/monoStream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<Mono<String>> testMonoStream(){
        return
                ResponseEntity.ok().body(Mono.just("A")
                        .delayElement(Duration.ofSeconds(3)));
        //.flatMap(s -> Flux.just(s))


    }
}
