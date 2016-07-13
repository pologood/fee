package commons.utils;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

public class MapHelper {
  public static Map<String, Object> make(Object ... varArgs) {
    Map<String, Object> map = new HashMap<>();

    for (int i = 0; i < varArgs.length; i+=2) {
      String key = (String) varArgs[i];
      map.put(key, varArgs[i+1]);
    }

    return map;
  }

  public static MultiValueMap<String, Object> makeMulti(Object ... varArgs) {
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

    for (int i = 0; i < varArgs.length; i+=2) {
      String key = (String) varArgs[i];
      map.add(key, varArgs[i+1]);
    }

    return map;
  }
}
