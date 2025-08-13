package com.ryan.ieltsplatform.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Utility class for managing correlation IDs across request threads.
 * Used for request tracing and log correlation.
 */
public class CorrelationIdUtil {

    private static final String CORRELATION_ID_KEY = "correlationId";

    /**
     * Generate a new correlation ID.
     * 
     * @return A new UUID-based correlation ID
     */
    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Set the correlation ID for the current thread.
     * 
     * @param correlationId The correlation ID to set
     */
    public static void setCorrelationId(String correlationId) {
        MDC.put(CORRELATION_ID_KEY, correlationId);
    }

    /**
     * Get the correlation ID for the current thread.
     * 
     * @return The current correlation ID, or null if not set
     */
    public static String getCurrentCorrelationId() {
        return MDC.get(CORRELATION_ID_KEY);
    }

    /**
     * Clear the correlation ID for the current thread.
     */
    public static void clearCorrelationId() {
        MDC.remove(CORRELATION_ID_KEY);
    }

    /**
     * Initialize correlation ID if not already set.
     * 
     * @return The correlation ID (either existing or newly generated)
     */
    public static String initializeCorrelationId() {
        String correlationId = getCurrentCorrelationId();
        if (correlationId == null) {
            correlationId = generateCorrelationId();
            setCorrelationId(correlationId);
        }
        return correlationId;
    }
}
