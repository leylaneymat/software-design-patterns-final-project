package com.ada.patterncommerce.domain.pricing;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PricingStrategyRegistry {
    private final Map<String, PricingStrategy> strategies;

    public PricingStrategyRegistry(List<PricingStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(PricingStrategy::name, Function.identity()));
    }

    public PricingStrategy resolve(String code) {
        if (code == null || code.isBlank()) {
            return strategies.get("STANDARD");
        }
        PricingStrategy strategy = strategies.get(code.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown pricing strategy: " + code);
        }
        return strategy;
    }
}
