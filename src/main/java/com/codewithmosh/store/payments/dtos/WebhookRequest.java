package com.codewithmosh.store.payments.dtos;

import java.util.Map;

public record WebhookRequest(Map<String, String> headers, String payload) {
}
