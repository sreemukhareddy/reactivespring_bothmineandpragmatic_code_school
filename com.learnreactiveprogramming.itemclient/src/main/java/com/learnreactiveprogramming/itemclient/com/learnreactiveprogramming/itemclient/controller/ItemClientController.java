package com.learnreactiveprogramming.itemclient.com.learnreactiveprogramming.itemclient.controller;

import com.learnreactiveprogramming.itemclient.com.learnreactiveprogramming.itemclient.domain.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping(value = "/client/retrieve", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Item> getItemsClient(){
        /*
        return webClient.get()
                .uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class);
*/
        return webClient.get()
                .uri("/v1/items")
                .exchangeToFlux(clientResponse -> {
                    return clientResponse.bodyToFlux(Item.class);
                });

    }

    @GetMapping(value = "/client/retrieve/exception", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Item> getItemsClientException(){
        return webClient.get()
                .uri("/v1/items/exception")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(err -> {
                        System.out.println("error is being fired man " + err);
                        throw new RuntimeException(err);
                    });
                })
                .bodyToFlux(Item.class);
    }

    @GetMapping(value = "/client/retrieve/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Item> getItemClient(@PathVariable String id){
        return webClient.get()
                .uri("/v1/items/"+id)
                .retrieve()
                .bodyToMono(Item.class);

    }

   @PostMapping(value = "/client", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Item> saveItem(@RequestBody Item item) {
        return webClient.post()
                        .uri("/v1/items")
                        .body(item, Item.class)
                        .exchangeToMono(clientResponse -> {
                            return clientResponse.bodyToMono(Item.class);
                        });

   }

    @PutMapping(value = "/client/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        return webClient.put()
                .uri("/v1/items/"+id)
                .bodyValue(item)
                .exchangeToMono(clientResponse -> {
                    return clientResponse.bodyToMono(Item.class);
                });

    }

    @DeleteMapping(value = "/client/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<String> deleteItemClient(@PathVariable String id){
        return webClient.delete()
                .uri("/v1/items/"+id)
                .exchangeToMono(clientResponse -> {
                    return Mono.just("Deleted");
                });


    }

}
