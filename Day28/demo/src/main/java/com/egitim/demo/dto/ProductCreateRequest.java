package com.egitim.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// POST/PUT /api/products → {"name": "Laptop", "price": 15000, "categoryId": 1}
// Day 28: kategori adı (String) yerine kategorinin id'si geliyor.
public class ProductCreateRequest {

    @NotBlank(message = "Ürün adı boş olamaz.")
    @Size(min = 2, max = 100, message = "Ürün adı 2-100 karakter olmalıdır.")
    private String name;

    // double değil Double: gönderilmezse null olsun, @NotNull yakalayabilsin
    @NotNull(message = "Fiyat zorunludur.")
    @Min(value = 0, message = "Fiyat negatif olamaz.")
    private Double price;

    @NotNull(message = "Kategori (categoryId) zorunludur.")
    private Long categoryId;

    public ProductCreateRequest() {}

    public String getName()     { return name; }
    public Double getPrice()    { return price; }
    public Long getCategoryId() { return categoryId; }

    public void setName(String name)          { this.name = name; }
    public void setPrice(Double price)        { this.price = price; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
