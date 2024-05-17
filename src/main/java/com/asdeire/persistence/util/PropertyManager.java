package com.asdeire.persistence.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Manages application properties.
 */
@Component
public final class PropertyManager {
    private static final Properties PROPERTIES = new Properties();
    private final Logger LOGGER = LoggerFactory.getLogger(PropertyManager.class);

    /**
     * Constructs a PropertyManager and loads properties from the default file.
     */
    public PropertyManager() {
        loadProperties();
    }

    /**
     * Constructs a PropertyManager with properties loaded from the specified input stream.
     *
     * @param applicationProperties The input stream containing application properties.
     */
    public PropertyManager(InputStream applicationProperties) {
        try (applicationProperties) {
            PROPERTIES.load(applicationProperties);
        } catch (IOException e) {
            LOGGER.error("Failed to read properties: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the value associated with the specified key from the properties.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or null if no value is found.
     */
    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties() {
        try (InputStream applicationProperties =
                     PropertyManager.class
                             .getClassLoader()
                             .getResourceAsStream("application.properties")) {
            PROPERTIES.load(applicationProperties);
        } catch (IOException e) {
            LOGGER.error("Failed to read properties: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
