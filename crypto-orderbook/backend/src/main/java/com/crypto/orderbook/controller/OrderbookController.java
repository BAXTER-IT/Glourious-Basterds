package com.crypto.orderbook.controller;

import com.crypto.orderbook.model.Orderbook;
import com.crypto.orderbook.service.BinanceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderbook")
public class OrderbookController {

    private final BinanceApiService binanceApiService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public OrderbookController(BinanceApiService binanceApiService, SimpMessagingTemplate messagingTemplate) {
        this.binanceApiService = binanceApiService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<Orderbook> getOrderbook() {
        return ResponseEntity.ok(binanceApiService.getLatestOrderbook());
    }

    @GetMapping("/normalized")
    public ResponseEntity<Orderbook> getNormalizedOrderbook(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(binanceApiService.getNormalizedOrderbook(limit));
    }

    @Scheduled(fixedRate = 10000)
    public void sendOrderbookUpdates() {
        Orderbook normalizedOrderbook = binanceApiService.getNormalizedOrderbook(5);
        messagingTemplate.convertAndSend("/topic/orderbook", normalizedOrderbook);
    }
}