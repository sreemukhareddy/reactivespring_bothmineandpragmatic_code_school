package com.learnreactivespring;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SampleHandlerFunction {

    public Mono<ServerResponse> flux(ServerRequest serverRequest) {
        return ServerResponse.ok()
                             .contentType(MediaType.APPLICATION_NDJSON)
                             .body(Flux.just("ABC", "DEF"), String.class);
    }

    public Mono<ServerResponse> mono(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(Mono.just("ABC"), String.class);
    }
}
