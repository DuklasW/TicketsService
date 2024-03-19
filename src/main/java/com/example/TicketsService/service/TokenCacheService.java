package com.example.TicketsService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TokenCacheService {
    private final static String TOKEN_CACHE_NAME = "bererTokenPayPall";

    private final PayPallService payPallService;

    private final CacheManager cacheManager;

    @Autowired
    public TokenCacheService(PayPallService payPallService, CacheManager cacheManager) {
        this.payPallService = payPallService;
        this.cacheManager = cacheManager;
    }

    public String getToken(){
        Cache cache = cacheManager.getCache(TOKEN_CACHE_NAME);
        String token;
        if(cache != null){
            Cache.ValueWrapper valueWrapper = cache.get(TOKEN_CACHE_NAME);
            if(valueWrapper != null){
                token = (String) valueWrapper.get();
                if(token != null){
                    return token;
                }
            }
        }

        token = payPallService.getAccessToken()
                        .flatMap(response -> Mono.just(response.getAccess_token()))
                                .block();

        if (cache != null) {
            cache.put(TOKEN_CACHE_NAME, token);
        }
        return token;
}
}

