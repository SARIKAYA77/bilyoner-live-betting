package com.example.bilyoner.core.chain;

import com.example.bilyoner.core.domain.Event;
import java.util.Random;

/**
 * First handler in the chain that randomly updates odds values
 * for events to simulate real-time odds changes
 */
public class RandomOddsHandler extends OddsUpdateHandler {
    private final Random random = new Random();
    
    // Min and max variation percentage for odds updates
    private static final double MIN_VARIATION = -0.05; // -5%
    private static final double MAX_VARIATION = 0.05;  // +5%
    
    @Override
    public void handle(Event event) {
        // Only update odds for active events
        if (event.isActive()) {
            // Apply random variations to each odds
            double newHomeWinOdds = applyRandomVariation(event.getHomeWinOdds());
            double newDrawOdds = applyRandomVariation(event.getDrawOdds());
            double newAwayWinOdds = applyRandomVariation(event.getAwayWinOdds());
            
            // Update the event with new odds
            event.updateOdds(newHomeWinOdds, newDrawOdds, newAwayWinOdds);
        }
        
        // Continue to the next handler
        handleNext(event);
    }
    
    private double applyRandomVariation(double currentOdds) {
        // Generate random variation percentage between MIN_VARIATION and MAX_VARIATION
        double variationPercentage = MIN_VARIATION + random.nextDouble() * (MAX_VARIATION - MIN_VARIATION);
        
        // Apply variation to current odds
        double newOdds = currentOdds * (1 + variationPercentage);
        
        // Ensure odds are not below 1.01 (minimum legal odds)
        return Math.max(1.01, Math.round(newOdds * 100.0) / 100.0); // Round to 2 decimal places
    }
} 