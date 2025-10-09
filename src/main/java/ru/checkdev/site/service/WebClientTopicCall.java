package ru.checkdev.site.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
@Slf4j
public class WebClientTopicCall {
    private final WebClient webClient;

    public <T> T get(String url, Class<T> clazz) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(clazz)
                .block();
    }
}
