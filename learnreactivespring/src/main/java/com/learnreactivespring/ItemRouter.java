package com.learnreactivespring;


import com.learnreactivespring.service.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemRouter {

    @Bean
    public RouterFunction<ServerResponse> itemControllerFunctional(ItemsHandler itemHandler){

        return RouterFunctions
                .route(RequestPredicates.GET("/v2/items").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::getAllItems)
                .andRoute(RequestPredicates.GET("/v2/items/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::getItem)
                .andRoute(RequestPredicates.POST("/v2/items").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::saveItem)
                .andRoute(RequestPredicates.DELETE("/v2/items/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::deleteItem)
                .andRoute(RequestPredicates.PUT("/v2/items/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::updateItem)
                .andRoute(RequestPredicates.GET("/v2/items/exception").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::getAllItemsException);
    }

    /*@Bean
    public RouterFunction<ServerResponse> itemControllerFunctionalExc(ItemsHandler itemHandler){
        return RouterFunctions
                .route(RequestPredicates.GET("/v2/items/exception").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), itemHandler::getAllItemsException);
    }*/

}
