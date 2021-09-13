package com.learnreactivespring;

import com.learnreactivespring.modal.Item;
import com.learnreactivespring.service.ItemReactiveService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;

@RestController
@Log4j2
public class ItemController {
    @Autowired
    private ItemReactiveService itemReactiveService;

    @GetMapping(value = "/v1/items",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Item> getAllItems(){
        return itemReactiveService.getAllItems()
                                  .onErrorMap(exception -> {
                                      throw  new RuntimeException("Exception has occured bugger");
                                  });
    }

    @GetMapping(value = "/v1/items/exception",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Item> getAllItemsException(){
        return itemReactiveService.getAllItemsException()
                                  .onErrorMap(exception -> {
                                      log.error("asdasdsadasd");
                                      throw new RuntimeException("zxcvbbn");
                                  });
    }

    @GetMapping(value = "/v1/items/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ResponseEntity<?>> getItem(@PathVariable String id) {
        return itemReactiveService.getItem(id)
                                  .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                                  .defaultIfEmpty(new ResponseEntity(null,HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/v1/items",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ResponseEntity<Item>> saveItem(@RequestBody Item item){
            return itemReactiveService.saveItem(item)
                                      .map(ResponseEntity::ok)
                                      .onErrorMap(ex -> {
                                          throw new RuntimeException("Heelooo");
                                      });
    }

    @DeleteMapping(value = "/v1/items/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemReactiveService.deleteItem(id);
    }

    @PutMapping(value = "/v1/items/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemReactiveService.updateItem(id, item);
    }

}
