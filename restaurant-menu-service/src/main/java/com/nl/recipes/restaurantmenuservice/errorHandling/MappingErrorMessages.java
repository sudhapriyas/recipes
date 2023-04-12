package com.nl.recipes.restaurantmenuservice.errorHandling;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MappingErrorMessages {
    private static final Map<String, ErrorText> errorMessagesHashMap;

    static {
        HashMap<String, ErrorText> map = new HashMap<>();
        for (ErrorText errorMessage : ErrorText.values()) {
            map.put(errorMessage.name(), errorMessage);
        }
        errorMessagesHashMap = Collections.unmodifiableMap(map);
    }

    public static ErrorText get(String key) {
        return errorMessagesHashMap.get(key);
    }
}
