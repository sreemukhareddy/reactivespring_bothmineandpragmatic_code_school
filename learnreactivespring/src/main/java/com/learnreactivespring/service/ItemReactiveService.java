package com.learnreactivespring.service;

import com.learnreactivespring.modal.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ItemReactiveService {
    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    public Flux<Item> getAllItems(){
        return itemReactiveRepository.findAll().delayElements(Duration.ofSeconds(1));
    }

    public Flux<Item> getAllItemsException(){
        return itemReactiveRepository.findAll().concatWith(Mono.error(new RuntimeException("Exception occured mother fucker")));
    }


    public Mono<Item> getItem(String id) {
        return itemReactiveRepository.findById(id);
    }

    public Mono<Item> saveItem(Item item) {
        return itemReactiveRepository.save(item);
        //itemReactiveRepository.saveAll(Arrays.asList(item)).
    }

    public Mono<ResponseEntity> saveItemItems(List<Item> items) {
        return itemReactiveRepository.saveAll(items)
                .collectList()
                .map(itemsSaved -> {
                    log.info("The saved items into the db is of size " + itemsSaved.size() + " the items that were sent through the client of size " + items.size());
                    return new ResponseEntity(HttpStatus.OK);
                });
    }

    public Mono<Void> deleteItem(String id) {
        return itemReactiveRepository.deleteById(id);
    }

    public Mono<Item> updateItem(String id, Item item) {
        return itemReactiveRepository.findById(id)
                              .flatMap(itemDb -> {
                                  itemDb.setPrice(item.getPrice());
                                  itemDb.setDescription(item.getDescription());
                                  return itemReactiveRepository.save(itemDb);
                              });
    }
}
