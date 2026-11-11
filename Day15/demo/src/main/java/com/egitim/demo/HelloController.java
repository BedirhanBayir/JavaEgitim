package com.egitim.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Merhaba, Spring Boot!";
    }

    @GetMapping("/status")
    public String status() {
        return "Sunucu çalışıyor. Sürüm: 1.0.0";
    }

    // JSON'u görmek için: Map döndürünce Spring bunu otomatik JSON'a çeviriyor
    @GetMapping("/person")
    public Map<String, Object> person() {
        return Map.of(
                "ad", "Bedirhan",
                "soyad", "Bayır",
                "yas", 21,
                "ogrenciMi", true,
                "diller", List.of("Java", "SQL")
        );
    }
}
