package commons.saas;

import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

public class RestNameService {
  Map<String, String> map = new HashMap<>();
  
  public RestNameService(Environment env) {
    String token = env.getRequiredProperty("rest.token");
    
    for (int i = 1; i < 101; ++i) {
      String value = env.getProperty("rest.nameservice." + String.valueOf(i));
      if (value == null) continue;

      String parts[] = value.split(":", 2);
      map.put(parts[0], parts[1].replace("__token__", token));
    }
  }

  public String lookup(String name) {
    String value = map.get(name);
    if (value == null) {
      throw new RuntimeException(name + " is not found in " + map.toString());
    }
    return value;
  }
}
