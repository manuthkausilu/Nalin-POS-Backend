package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.LogDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody LogDTO dto) {
        Response response = logService.createLog(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = logService.getLogById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = logService.getAllLogs();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody LogDTO dto) {
        Response response = logService.updateLog(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = logService.deleteLog(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
