package com.smartbear.readyapi.testserver;

import com.google.common.collect.Maps;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class SwaggerCache {

    private final SwaggerParser parser;
    private Map<String,Swagger> cache = Maps.newHashMap();

    public SwaggerCache() {
        parser = new SwaggerParser();
    }

    public Swagger getSwagger(String swaggerUrl) {
        if( !cache.containsKey( swaggerUrl )){
            cache.put( swaggerUrl, parser.read( swaggerUrl ));
        }

        return cache.get( swaggerUrl );
    }
}
