package com.learnreactivespring.exception;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.util.Map;
import java.util.Optional;

//@Component
//@Order(-2)
class RestWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public RestWebExceptionHandler(ErrorAttributes errorAttributes,
                                   WebProperties.Resources resourceProperties,
                                   ApplicationContext context,
                                   ServerCodecConfigurer codecConfigurer
                                ){
        super(errorAttributes, resourceProperties, context);
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest serverRequest){
       Map<String, Object> errorAttributesMap =  getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        int status = (Integer) Optional.ofNullable(errorAttributesMap.get("status")).orElse(500);
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }
}