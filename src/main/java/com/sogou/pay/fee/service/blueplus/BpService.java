package com.sogou.pay.fee.service.blueplus;

import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.mapper.OrderMapper;
import com.sogou.pay.fee.mapper.ProductMapper;
import com.sogou.pay.fee.model.PayReturnInfo;
import com.sogou.pay.fee.model.PhoneInfo;
import com.sogou.pay.fee.service.FeeService;
import com.sogou.pay.fee.service.ProvinceUtil;
import commons.saas.RestNameService;
import commons.utils.DigestHelper;
import commons.utils.JsonHelper;
import commons.utils.MapHelper;
import commons.utils.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nahongxu on 2016/6/22.
 */
public class BpService implements FeeService {

    private String partid;
    private String secret;

    public static final int BLUEPLUS_PROVIDER_ID = 1;
    public static final String BP_NO_FORWARD = "0";
    public static final String BP_PAY_FORWARD = "1";

    @Autowired
    private RestTemplate bpRestTemplate;

    @Autowired
    private RestNameService restNameService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    public BpService(Environment env) {
        this.partid = env.getProperty("bpPartid");
        this.secret = env.getProperty("bpSecret");
    }


    public List<PhoneInfo> queryPhoneInfos(List<String> phones) {
        BpReqDataBody bpReqDataBody = new BpReqDataBody("N1001", "getPhoneInfo");
        bpReqDataBody.put("phones", phones);
        BpResponseBase<List> bpResponseBase = getBpApiResponse(bpReqDataBody);

        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            throw new BpException(bpResponseBase.getErrdesc());
        }
        List bpPhoneInfos = bpResponseBase.getDetailinfo();

        List<PhoneInfo> phoneInfos = new ArrayList<PhoneInfo>();
        for (Object bpPhoneObject : bpPhoneInfos) {
            String mapStr = JsonHelper.writeValueAsString(bpPhoneObject);
            BpPhoneInfo bpPhoneInfo = JsonHelper.readValue(mapStr, BpPhoneInfo.class);
            phoneInfos.add(new PhoneInfo(bpPhoneInfo));
        }

        return phoneInfos;
    }

    @Override
    public List<Product> queryPhoneProducts(Product.FeeType feeType, PhoneInfo.Operator operator, String province) {

        BpReqDataBody bpReqDataBody = buildBpProductsReqData(feeType, operator, province);
        BpResponseBase<List> bpResponseBase = getBpApiResponse(bpReqDataBody);

        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            throw new BpException(bpResponseBase.getErrdesc());
        }

        List bpProducts = bpResponseBase.getDetailinfo();
        List<Product> products = new ArrayList<Product>();
        Product productExist;
        Product productNew;
        for (Object bpProductObject : bpProducts) {
            String mapStr = JsonHelper.writeValueAsString(bpProductObject);
            BpProductInfo bpProduct = JsonHelper.readValue(mapStr, BpProductInfo.class);
            productNew = new Product(bpProduct);

            productExist = productMapper.findByOuterId(productNew.getOuterId());
            if (productExist == null) {
                productMapper.add(productNew);
            } else {
                Tuple2<Product, Boolean> diff = BpService.compareProduct(productExist, productNew);
                if (diff.s) {
                    productMapper.update(diff.f);
                }
                productNew.setProductId(productExist.getProductId());
            }
            products.add(productNew);
        }

        return products;
    }

    @Override
    public PayReturnInfo createOrder(Order order, Product product, String province, PhoneInfo.Operator operator) {
        BpReqDataBody bpReqDataBody = buildBpPayReqData(order, product, operator, province);

        BpResponseBase<BpPaymentInfo> bpResponseBase = getBpApiResponse(bpReqDataBody);
        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            throw new BpException(bpResponseBase.getErrdesc());
        }

        String mapStr = JsonHelper.writeValueAsString(bpResponseBase.getDetailinfo());
        BpPaymentInfo bpPaymentInfo = JsonHelper.readValue(mapStr, BpPaymentInfo.class);
        String bpPayReturnInfo = bpPaymentInfo.getReturninfo();
        String bpPayReturnType = bpPaymentInfo.getReturntype();
        String bpOrderId = bpPaymentInfo.getOrderid();

        order.setOuterId(bpOrderId);

        order.setStatus(Order.Status.TOPAY);
        orderMapper.add(order);

        PayReturnInfo.PayReturnType returnType = convFromBpRetTypeNew(bpPayReturnType);
        PayReturnInfo payReturnInfo = new PayReturnInfo(order, bpPayReturnInfo, returnType);
        return payReturnInfo;
    }

    @Override
    public Order.Status queryOrderStatus(Order order) {
        String bpOrderId = order.getOuterId();
        String bpMonth = order.getCreateTime().toString().substring(0, 8).replaceAll("-", "");

        BpReqDataBody bpReqDataBody = new BpReqDataBody("N1006", "getOrderDetail");

        bpReqDataBody.put("orderid", bpOrderId);
        bpReqDataBody.put("month", bpMonth);

        BpResponseBase<BpOrderDetail> bpResponseBase = getBpApiResponse(bpReqDataBody);
        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            throw new BpException(bpResponseBase.getErrdesc());
        }

        String mapStr = JsonHelper.writeValueAsString(bpResponseBase.getDetailinfo());
        BpOrderDetail bpOrderDetail = JsonHelper.readValue(mapStr, BpOrderDetail.class);
        String bpOrderStatus = bpOrderDetail.getOrderstate();
        Order.Status orderStatus = convFromBpStatusNew(bpOrderStatus);
        return orderStatus;
    }


    public BpResponseBase getBpApiResponse(BpReqDataBody bpReqDataBody) {
        String partid = getPartid();
        String dataToEncrypt = JsonHelper.writeValueAsString(bpReqDataBody);
        String curTime = String.valueOf(System.currentTimeMillis());
        String data = "";
        String sign = "";
        try {
            data = BpAesUtil.encrypt(dataToEncrypt, getSecret());
            sign = DigestHelper.md5(BpAesUtil.getSign(partid, data, curTime).getBytes("utf-8"));

        } catch (Exception e) {
            throw new BpException(e);
        }

        BpResponseBase bpResponseBase = bpRestTemplate.postForObject(restNameService.lookup("BLUEPLUS_SERVICE"),
                MapHelper.makeMulti("partid", partid,
                        "data", data,
                        "time", curTime,
                        "sign", sign),
                BpResponseBase.class);


        return bpResponseBase;

    }

    public BpReqDataBody buildBpProductsReqData(Product.FeeType feeType, PhoneInfo.Operator operator, String province) {
        BpReqDataBody bpReqDataBody = new BpReqDataBody("N1002", "getProdList");
        bpReqDataBody.put("prodtype", convToBpFeeTypeNew(feeType));
        bpReqDataBody.put("ownoperator", BpService.convToBpOperatorNew(operator));
        bpReqDataBody.put("ownprovince", BpProvince.getProCodeByBpName(ProvinceUtil.getProvinceByCode(province)));

        return bpReqDataBody;

    }

    public BpReqDataBody buildBpPayReqData(Order order, Product product, PhoneInfo.Operator operator, String province) {
        String bpRequstType = convToBpRqTypeNew(order.getPayTerminal());
        String bpForwarType = BP_PAY_FORWARD;
        String bpBillId = order.getPhone();
        String bpAccountCode = order.getPhone();
        String bpBillType = "1";
        String bpProductId = product.getOuterId();
        String bpProductName = product.getProductName();
        String bpProType = convToBpFeeTypeNew(order.getFeeType());
        String bpProAmount = String.valueOf(order.getTotalAmount());
        String bpOperator = BpService.convToBpOperatorNew(operator);
        String bpProvince = BpProvince.getProCodeByBpName(ProvinceUtil.getProvinceByCode(province));
        String bpDenominationprice = String.valueOf(product.getDenominationprice());
        String bpPayappcode = convToBpChannelNew(order.getPayChanel());
        String bpReturnUrl = "https://pay.sogou.com";
//        String bpCallbackUrl="https://pay.sogou.com";

        BpReqDataBody bpReqDataBody = new BpReqDataBody("N1007", "payment");
        bpReqDataBody.put("requesttype", bpRequstType);
        bpReqDataBody.put("forwardtype", bpForwarType);
        bpReqDataBody.put("billid", bpBillId);
        bpReqDataBody.put("acctcode", bpAccountCode);
        bpReqDataBody.put("billtype", bpBillType);
        bpReqDataBody.put("prodid", bpProductId);
        bpReqDataBody.put("prodname", bpProductName);
        bpReqDataBody.put("prodtype", bpProType);
        bpReqDataBody.put("prodamount", bpProAmount);
        bpReqDataBody.put("ownoperator", bpOperator);
        bpReqDataBody.put("ownprovince", bpProvince);
        bpReqDataBody.put("proddenominationprice", bpDenominationprice);
        bpReqDataBody.put("payappcode", bpPayappcode);
        bpReqDataBody.put("returnurl", bpReturnUrl);
//        bpReqDataBody.put("callbackurl",bpCallbackUrl);

        return bpReqDataBody;
    }

    public String getPartid() {
        return partid;
    }

    public void setPartid(String partid) {
        this.partid = partid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static String convToBpFeeTypeNew(Product.FeeType feeType) {
        switch (feeType) {
            case ALL:
                return "-1";
            case PHONE:
                return "1";
            case FLOW:
                return "2";
            default:
                return null;
        }
    }

    public static Product.FeeType convFromBpFeeTypeNew(String bpFeeType) {
        switch (bpFeeType) {
            case "-1":
                return Product.FeeType.ALL;
            case "1":
                return Product.FeeType.PHONE;
            case "2":
                return Product.FeeType.FLOW;
            default:
                return Product.FeeType.UNKNOWN;
        }
    }

    public static PhoneInfo.Operator convFromBpOperatorNew(String bpOperator) {
        switch (bpOperator) {
            case "001":
                return PhoneInfo.Operator.CHINA_TELECOM;
            case "010":
                return PhoneInfo.Operator.CHINA_UNICOM;
            case "100":
                return PhoneInfo.Operator.CHINA_MOBILE;
            default:
                return PhoneInfo.Operator.UNKNOWN;
        }
    }

    public static String convToBpOperatorNew(PhoneInfo.Operator feeOperator) {
        switch (feeOperator) {
            case CHINA_TELECOM:
                return "001";
            case CHINA_UNICOM:
                return "010";
            case CHINA_MOBILE:
                return "100";
            default:
                return null;
        }
    }

    public static Order.Status convFromBpStatusNew(String bpStatus) {
        switch (bpStatus) {
            case "1":
                return Order.Status.TOPAY;
            case "2":
                return Order.Status.PAY_SUCCESS;
            case "3":
                return Order.Status.PAY_FAILED;
            case "4":
                return Order.Status.FEEING;
            case "5":
                return Order.Status.FEE_SUCCESS;
            case "6":
                return Order.Status.FEE_FAILED;
            case "7":
                return Order.Status.REFUNDING;
            case "8":
                return Order.Status.REFUND_SUCCESS;
            case "9":
                return Order.Status.EXPIERED;
            default:
                return Order.Status.UNKNOWN;

        }
    }

    public static String convToBpChannelNew(Order.Channel payChannel) {
        switch (payChannel) {
            case ZHIFUBAO:
                return "ZFB";
            case YINLIAN:
                return "YL";
            case BAIFUBAO:
                return "BFB";
            default:
                return null;
        }
    }

    public static String convToBpRqTypeNew(Order.PayTerminal feePayTemin) {
        switch (feePayTemin) {
            case PC:
                return "0";
            case WAP:
                return "2";
            case APP:
                return "1";
            case OTHERS:
                return "3";
            default:
                return null;
        }
    }

    public static PayReturnInfo.PayReturnType convFromBpRetTypeNew(String bpRetType) {
        switch (bpRetType) {
            case "0":
                return PayReturnInfo.PayReturnType.STR;
            case "1":
                return PayReturnInfo.PayReturnType.URL;
            case "2":
                return PayReturnInfo.PayReturnType.HTML;
            default:
                return PayReturnInfo.PayReturnType.UNKNOWN;
        }

    }

    public static PhoneInfo.WarnCode convFromBpWarnCode(String bpWarnCode) {
        switch (bpWarnCode) {
            case "0":
                return PhoneInfo.WarnCode.NORMAL;
            case "1":
                return PhoneInfo.WarnCode.BLACKLIST;
            case "3":
                return PhoneInfo.WarnCode.PROVINCE_OPERRATING;
            default:
                return null;
        }
    }

    public static PhoneInfo.OpType convFromBpOpType(String bpOpType) {
        boolean phoneOp = (bpOpType.substring(0, 1)) == "1";
        boolean flowOp = (bpOpType.substring(1, 2)) == "1";
        if (phoneOp && flowOp) {
            return PhoneInfo.OpType.ALL;
        } else if (phoneOp) {
            return PhoneInfo.OpType.PHONE_OP;
        } else if (flowOp) {
            return PhoneInfo.OpType.FLOW_OP;
        } else {
            return PhoneInfo.OpType.NONE;
        }

    }

    static Tuple2<Product, Boolean> compareProduct(Product oldPro, Product newPro) {
        Product productDiff = new Product();
        productDiff.setProductId(oldPro.getProductId());
        productDiff.setProviderId(-1);
        productDiff.setStandardPrice(-1);
        productDiff.setRealPrice(-1);
        productDiff.setDenominationprice(-1);
        productDiff.setOuterId(oldPro.getOuterId());
        Boolean needUpdate = false;

        if (!oldPro.getProductName().equals(newPro.getProductName())) {
            productDiff.setProductName(newPro.getProductName());
            needUpdate = true;
        }

        if (oldPro.getProviderId() != newPro.getProviderId()) {
            productDiff.setProviderId(newPro.getProviderId());
            needUpdate = true;
        }

        if (oldPro.getStandardPrice() != newPro.getStandardPrice()) {
            productDiff.setStandardPrice(newPro.getStandardPrice());
            needUpdate = true;
        }

        if (oldPro.getRealPrice() != newPro.getRealPrice()) {
            productDiff.setRealPrice(newPro.getRealPrice());
            needUpdate = true;
        }

        if (oldPro.getStatus() != newPro.getStatus()) {
            productDiff.setStatus(newPro.getStatus());
            needUpdate = true;
        }

        return new Tuple2<Product, Boolean>(productDiff, needUpdate);
    }
}