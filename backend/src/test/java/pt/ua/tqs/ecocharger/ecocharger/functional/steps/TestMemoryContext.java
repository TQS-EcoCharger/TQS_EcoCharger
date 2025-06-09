package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import java.util.HashMap;
import java.util.Map;

public class TestMemoryContext {
  private static final Map<String, Object> data = new HashMap<>();

  public static void put(String key, Object value) {
    data.put(key, value);
  }

  public static Object get(String key) {
    return data.get(key);
  }

  public static void clear() {
    data.clear();
  }
}
