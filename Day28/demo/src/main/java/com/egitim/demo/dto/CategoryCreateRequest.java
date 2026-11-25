package com.egitim.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// POST /api/categories → {"name": "Elektronik", "description": "Bilgisayar, telefon ve aksesuarlar"}
public class CategoryCreateRequest {

    @NotBlank(message = "Kategori adı boş olamaz.")
    @Size(max = 100, message = "Kategori adı en fazla 100 karakter olabilir.")
    private String name;

    // İsteğe bağlı; ama veritabanındaki varchar(255)'i aşmasın (Day 26'daki 500 hatası)
    @Size(max = 255, message = "Açıklama en fazla 255 karakter olabilir.")
    private String description;

    public CategoryCreateRequest() {}

    public String getName()        { return name; }
    public String getDescription() { return description; }

    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
