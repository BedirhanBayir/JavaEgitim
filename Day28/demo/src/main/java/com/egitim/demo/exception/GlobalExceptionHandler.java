package com.egitim.demo.exception;

import com.egitim.demo.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

// Tüm @Controller / @RestController'lar için ortak hata yönetimi.
// Bir controller metodu exception fırlatınca Spring buradaki uygun @ExceptionHandler'ı çalıştırıyor.
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404: kayıt bulunamadı (Product, User ...)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(404, ex.getMessage(), request.getRequestURI()));
    }

    // 400: @Valid kuralları bozuldu → {"name": "Ürün adı boş olamaz.", ...}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e ->
                errors.merge(e.getField(), e.getDefaultMessage(), (a, b) -> a + " " + b));
        return ResponseEntity.badRequest().body(errors);
    }

    // ===== Ek: aşağıdaki iki hata eskiden Spring'in kendisi tarafından 400'e çevriliyordu.
    // Exception.class için genel bir handler yazınca onları da o yakalayıp 500 yapıyordu.

    // 400: bozuk JSON ya da yanlış tip ("price": "abc")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "İstek gövdesi okunamadı. JSON formatını ve alan tiplerini kontrol edin.",
                        request.getRequestURI()));
    }

    // 400: /api/products/abc gibi, id'ye sayı yerine yazı gelmesi
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "'" + ex.getValue() + "' değeri '" + ex.getName() + "' parametresi için geçersiz.";
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, message, request.getRequestURI()));
    }

    // Geri kalan her şey
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        // Spring'in kendi HTTP hataları (olmayan adres → 404, yanlış method → 405,
        // eksik @RequestParam → 400) zaten kendi durum kodunu biliyor; onu koru.
        if (ex instanceof org.springframework.web.ErrorResponse springError) {
            int status = springError.getStatusCode().value();
            String message = springError.getBody().getDetail();
            return ResponseEntity.status(status)
                    .body(new ErrorResponse(status, message, request.getRequestURI()));
        }

        // Gerçekten beklenmeyen hata: detay (stack trace) sadece log'a gidiyor,
        // istemciye iç sistem bilgisi sızdırılmıyor.
        log.error("Beklenmeyen hata: {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity.status(500)
                .body(new ErrorResponse(500, "Beklenmeyen bir sunucu hatası oluştu.", request.getRequestURI()));
    }
}
