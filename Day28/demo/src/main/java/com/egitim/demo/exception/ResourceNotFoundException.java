package com.egitim.demo.exception;

// "Aranan kayıt yok" durumu. RuntimeException olduğu için (unchecked)
// service'ten controller'a kadar her metoda "throws" yazmak gerekmiyor;
// GlobalExceptionHandler yakalayıp 404'e çeviriyor.
public class ResourceNotFoundException extends RuntimeException {

    // new ResourceNotFoundException("Product", 1L) → "Product 1 bulunamadı."
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " " + id + " bulunamadı.");
    }
}
