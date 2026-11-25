package com.egitim.demo.dto;

import java.time.LocalDateTime;

// Bütün hata cevaplarının ortak şekli:
// {"timestamp": "...", "status": 404, "message": "Product 99 bulunamadı.", "path": "/api/products/99"}
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;

    public ErrorResponse(int status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus()              { return status; }
    public String getMessage()          { return message; }
    public String getPath()             { return path; }
}
