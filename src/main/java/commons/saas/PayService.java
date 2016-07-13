package commons.saas;

import java.util.Map;

public abstract class PayService {
  public static class PrePayCtx {
    public Object prePayId;
    public String publicKey;
  }

  public static class PostPayCtx {
    public String payId;
    public String orderId;
    public String money;
  }

  public static enum SignType {
    MD5, SHA;
  }

  public static enum Channel {
    WEIXIN(1), ZHIFUBAO(2);

    private int value;
    Channel(int value) { this.value = value; }
    public int getValue() { return this.value; }
  }

  public static class Config {
    public String   appid;
    public String   appkey;
    public String   apppayUrl;
    public String   callbackUrl;
    public String   notifyUrl;
    public String   version;
    public SignType signType;
  }

  protected Config  config = new Config();
  protected String  orderId;
  protected String  productName;
  protected String  money;
  protected Channel channel;

  public PayService newInstance() {
    try {
      PayService payService = (PayService) this.getClass().newInstance();
      payService.config = config;
      return payService;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public PayService setAppid(String appid) {
    this.config.appid = appid;
    return this;
  }

  public PayService setAppkey(String appkey) {
    this.config.appkey = appkey;
    return this;
  }

  public PayService setApppayUrl(String payUrl) {
    this.config.apppayUrl = payUrl;
    return this;
  }

  public PayService setCallbackUrl(String callbackUrl) {
    this.config.callbackUrl = callbackUrl;
    return this;
  }
  
  public PayService setNotifyUrl(String notifyUrl) {
    this.config.notifyUrl = notifyUrl;
    return this;
  }

  public PayService setVersion(String version) {
    this.config.version = version;
    return this;
  }

  public PayService setSignType(SignType signType) {
    this.config.signType = signType;
    return this;
  }

  public PayService setOrderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  public PayService setProductName(String productName) {
    this.productName = productName;
    return this;
  }

  public PayService setMoney(String money) {
    this.money = money;
    return this;
  }

  public PayService setChannel(Channel channel) {
    this.channel = channel;
    return this;
  }

  public abstract PrePayCtx appPrePay();
  public abstract PostPayCtx postPay(Map<String, String> params);
}
