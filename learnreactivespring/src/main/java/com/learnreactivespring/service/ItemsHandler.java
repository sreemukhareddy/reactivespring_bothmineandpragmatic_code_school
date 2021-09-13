package com.learnreactivespring.service;

import com.learnreactivespring.modal.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {
    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(itemReactiveRepository.findAll().doOnNext(System.out::println), Item.class);

    }

    public Mono<ServerResponse> getAllItemsException(ServerRequest serverRequest) {
        System.out.println("QQQQQQQQQQQQQQQQQQQQ");
        throw new RuntimeException("Exception has occured mate");
    }

    public Mono<ServerResponse> getItem(ServerRequest serverRequest) {
        String itemId = serverRequest.pathVariable("id");
        return itemReactiveRepository.findById(itemId)
                              .flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(item)))
                              .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveItem(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Item.class)
                     .flatMap(itemReactiveRepository::save)
                     .flatMap(item -> ServerResponse.ok().body(BodyInserters.fromValue(item)));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        String itemId = serverRequest.pathVariable("id");
        return itemReactiveRepository.deleteById(itemId)
                .flatMap(nthng -> ServerResponse.ok().body(nthng, Void.class));
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        String itemId = serverRequest.pathVariable("id");
       return itemReactiveRepository.findById(itemId)
                              .flatMap(dbItem -> {
                                  return serverRequest.bodyToMono(Item.class) // got the item from client
                                               .flatMap(clientItem -> {
                                                   dbItem.setDescription(clientItem.getDescription());
                                                   dbItem.setPrice(clientItem.getPrice());
                                                   return itemReactiveRepository.save(dbItem); // saving the item into the client
                                               });
                              })
                              .flatMap(updatedITem -> {
                                 return ServerResponse.ok().body(BodyInserters.fromValue(updatedITem));
                              });

    }
}
