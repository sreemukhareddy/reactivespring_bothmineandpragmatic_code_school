package com.learnreactivespring;

import com.learnreactivespring.modal.Item;
import com.learnreactivespring.modal.ItemCapped;
import com.learnreactivespring.repository.ItemCappedReactiveRepository;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

   // @Autowired
    private MongoOperations mongoOperations;


    MongoTemplate mongoTemplate;

    @Autowired
    private ItemCappedReactiveRepository itemCappedReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialdataSetup();
        createCappedCollections();
    }

    private void createCappedCollections() {
       // mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "myDatabase"));
       // mongoTemplate.dropCollection(ItemCapped.class);
       // mongoTemplate.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
       /* itemCappedReactiveRepository.deleteAll()
                .flatMapMany(v -> {
                    return Flux.interval(Duration.ofSeconds(1))
                            .take(25)
                            .map(i -> new ItemCapped(null, "Dummy " + i, i.doubleValue()));
                })
                .flatMap(itemCapped -> {
                    return itemCappedReactiveRepository.save(itemCapped);
                })
            .subscribe(System.out::println);*/
        itemCappedReactiveRepository.deleteAll()

                                    .subscribe(v -> {
                                        System.out.println("Deleted all the entries");
                                        Flux.interval(Duration.ofSeconds(1))
                                                .take(25)
                                                .map(i -> new ItemCapped(null, "Dummy " + i, i.doubleValue()))
                                                .subscribe(ic -> {
                                                    itemCappedReactiveRepository.save(ic)
                                                            .subscribe();
                                                });
                                    });
    }

    private void initialdataSetup(){
        List<Item> data = new ArrayList();
        data.add(new Item(null, "TV", 45d));
        data.add(new Item(null, "AC", 90d));
        data.add(new Item(null, "FRIDGE", 75d));
        data.add(new Item(null, "MiXi", 45d));
        itemReactiveRepository.deleteAll()
                              .thenMany(Flux.fromIterable(data))
                              .flatMap(itemReactiveRepository::save)
                              .subscribe(System.out::println);

    }
}
