package com.codewithmosh.store.services;

import java.util.Map;

public record WebhookRequest(Map<String, String> headers, String payload) {
}
