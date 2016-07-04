package com.sogou.pay.fee.service;

import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.entity.ProvinceUtil;
import com.sogou.pay.fee.mapper.OrderMapper;
import com.sogou.pay.fee.mapper.ProductMapper;
import com.sogou.pay.fee.model.*;
import com.sogou.pay.fee.service.blueplus.*;
import commons.saas.RestNameService;
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
public class BpService implements FeeService{

    private String partid;
    private String secret;

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

    @Override
    public List<PhoneInfo> queryPhoneInfos(List<String> phones){
        BpPhoneInfoReqData bpPhoneInfoReq = new BpPhoneInfoReqData(phones);
        BpResponseBase<List> bpResponseBase = getBpApiResponse(bpPhoneInfoReq);

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
    public List<Product> queryPhoneProducts(FeeType feeType, Operator operator, String province) {
        BpProductsReqData bpProductsReqData = buildBpProductsReqData(feeType, operator, province);
        BpResponseBase<List> bpResponseBase = getBpApiResponse(bpProductsReqData);

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
                Tuple2<Product, Boolean> diff = FeeService.compareProduct(productExist, productNew);
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
    public PayReturnInfo createOrder(Order order,  Product product,String province, Operator operator) {
        BpPaymentReqData bpPaymentReqData = buildBpPayReqData(order, product, operator, province);

        BpResponseBase<BpPaymentInfo> bpResponseBase = getBpApiResponse(bpPaymentReqData);
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

        PayReturnType returnType = BpReturnType.convFromBpRetType(Integer.parseInt(bpPayReturnType));
        PayReturnInfo payReturnInfo = new PayReturnInfo(order, bpPayReturnInfo, returnType);
        return payReturnInfo;
    }

    @Override
    public Order.Status queryOrderStatus(Order order){
        String bpOrderId = order.getOuterId();
        String bpMonth = order.getCreateTime().toString().substring(0, 8).replaceAll("-", "");
        BpOrderDetailReqData bpOrderDetailReqData = new BpOrderDetailReqData(bpOrderId, bpMonth);
        BpResponseBase<BpOrderDetail> bpResponseBase = getBpApiResponse(bpOrderDetailReqData);
        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            throw new BpException(bpResponseBase.getErrdesc());
        }

        String mapStr = JsonHelper.writeValueAsString(bpResponseBase.getDetailinfo());
        BpOrderDetail bpOrderDetail = JsonHelper.readValue(mapStr, BpOrderDetail.class);
        String bpOrderStatus = bpOrderDetail.getOrderstate();
        Order.Status orderStatus = BpOrderStatus.convFromBpStatus(Integer.parseInt(bpOrderStatus));
        return orderStatus;
    }


    public BpResponseBase getBpApiResponse(BluePlusBaseData bluePlusBaseData) {
        String partid = getPartid();
        String dataToEncrypt = JsonHelper.writeValueAsString(bluePlusBaseData);
        String curTime = String.valueOf(System.currentTimeMillis());
        String data = "";
        String sign = "";
        try {
            data = BpAesUtil.encrypt(dataToEncrypt, getSecret());
            sign = BpSignUtil.getSignAndMD5(partid, data, curTime);
        } catch (Exception e) {
            throw new BpException(e);
        }


        BluePlusRequestBody requestBody = new BluePlusRequestBody(partid, data, curTime, sign);


        BpResponseBase bpResponseBase = bpRestTemplate.postForObject(restNameService.lookup("BLUEPLUS_SERVICE"),
                MapHelper.makeMulti("partid", requestBody.getPartid(),
                        "data", requestBody.getData(),
                        "time", requestBody.getTime(),
                        "sign", requestBody.getSign()),
                BpResponseBase.class);


        return bpResponseBase;

    }

    public BpProductsReqData buildBpProductsReqData(FeeType feeType, Operator operator, String province) {
        String prodtype = String.valueOf(BpFeeType.convToBpFeeType(feeType).getValue());
        String ownoperator = BpOperator.convToBpOperator(operator).getCode();
        String ownprovince = BpProvince.getProCodeByBpName(ProvinceUtil.getProvinceByCode(province));

        BpProductsReqData bpProductsReqData = new BpProductsReqData(prodtype, ownoperator, ownprovince);
        return bpProductsReqData;

    }

    public BpPaymentReqData buildBpPayReqData(Order order, Product product, Operator operator, String province) {
        String bpRequstType = String.valueOf(BpRequestType.convToBpRqType(order.getPayTerminal()).getValue());
        String bpForwarType = String.valueOf(BpForwardType.PAY_FORWARD.getValue());
        String bpBillId = order.getPhone();
        String bpAccountCode = order.getPhone();
        String bpBillType = "1";
        String bpProductId = product.getOuterId();
        String bpProductName = product.getProductName();
        String bpProType = String.valueOf(BpFeeType.convToBpFeeType(order.getFeeType()).getValue());
        String bpProAmount = String.valueOf(order.getTotalAmount());
        String bpOperator = BpOperator.convToBpOperator(operator).getCode();
        String bpProvince = BpProvince.getProCodeByBpName(ProvinceUtil.getProvinceByCode(province));
        String bpDenominationprice = String.valueOf(product.getDenominationprice());
        String bpPayappcode = BpPayChannel.convToBpChannel(order.getPayChanel()).getName();
        String bpReturnUrl = "https://pay.sogou.com";

        BpPaymentReqData bpPaymentReqData = new BpPaymentReqData()
                .setRequesttype(bpRequstType)
                .setForwardtype(bpForwarType)
                .setBillid(bpBillId)
                .setAcctcode(bpAccountCode)
                .setBilltype(bpBillType)
                .setProdid(bpProductId)
                .setProdname(bpProductName)
                .setProdtype(bpProType)
                .setProdamount(bpProAmount)
                .setOwnoperator(bpOperator)
                .setOwnprovince(bpProvince)
                .setProddenominationprice(bpDenominationprice)
                .setPayappcode(bpPayappcode)
                .setReturnurl(bpReturnUrl);

        return bpPaymentReqData;
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
}
