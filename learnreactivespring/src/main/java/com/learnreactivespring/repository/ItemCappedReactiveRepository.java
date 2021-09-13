package com.learnreactivespring.repository;

import com.learnreactivespring.modal.Item;
import com.learnreactivespring.modal.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<ItemCapped, String> {
    @Tailable
    Flux<ItemCapped> findItemsBy();
}
