package com.egitim.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// İstemcinin ürün eklerken/güncellerken gönderdiği alanlar ve kuralları.
// id yok: id'yi veritabanı veriyor.
public class ProductCreateRequest {

    @NotBlank(message = "Ürün adı boş olamaz.")
    @Size(min = 2, max = 100, message = "Ürün adı 2-100 karakter olmalıdır.")
    private String name;

    // double değil Double: primitive double gönderilmezse 0.0 olurdu,
    // @NotNull "fiyat hiç gönderilmedi" durumunu yakalayamazdı.
    @NotNull(message = "Fiyat zorunludur.")
    @Min(value = 0, message = "Fiyat negatif olamaz.")
    private Double price;

    @NotBlank(message = "Kategori boş olamaz.")
    private String category;

    public ProductCreateRequest() {}

    public String getName()     { return name; }
    public Double getPrice()    { return price; }
    public String getCategory() { return category; }

    public void setName(String name)         { this.name = name; }
    public void setPrice(Double price)       { this.price = price; }
    public void setCategory(String category) { this.category = category; }
}
