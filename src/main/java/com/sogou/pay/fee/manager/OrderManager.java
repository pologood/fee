package com.sogou.pay.fee.manager;

import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.entity.ProvinceUtil;
import com.sogou.pay.fee.mapper.OrderMapper;
import com.sogou.pay.fee.mapper.ProductMapper;
import com.sogou.pay.fee.model.*;
import com.sogou.pay.fee.model.blueplus.*;
import com.sogou.pay.fee.service.BpAesUtil;
import com.sogou.pay.fee.service.BpRestService;
import com.sogou.pay.fee.service.BpSignUtil;
import commons.mybatis.Paging;
import commons.saas.RestNameService;
import commons.utils.JsonHelper;
import commons.utils.MapHelper;
import commons.utils.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class OrderManager {

    @Autowired
    private RestTemplate bpRestTemplate;

    @Autowired
    private RestNameService restNameService;
    @Autowired
    private BpRestService bpRestService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    private String tokenKey;

    @Autowired
    public OrderManager(Environment env) {

        tokenKey = env.getRequiredProperty("security.token");
    }

    public ApiResult queryPhoneInfo(List<String> phones) {

        BpPhoneInfoReqData bpPhoneInfoReq = new BpPhoneInfoReqData(phones);
        BpResponseBase<List> bpResponseBase = getBpApiResponse(bpPhoneInfoReq);

        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            return ApiResult.internalError(bpResponseBase.getErrdesc());
        }
        List bpPhoneInfos = bpResponseBase.getDetailinfo();

        List<PhoneInfo> phoneInfos = new ArrayList<PhoneInfo>();
        for (Object bpPhoneObject : bpPhoneInfos) {
            String mapStr = JsonHelper.writeValueAsString(bpPhoneObject);
            BpPhoneInfo bpPhoneInfo = JsonHelper.readValue(mapStr, BpPhoneInfo.class);
            phoneInfos.add(new PhoneInfo(bpPhoneInfo));
        }
        return new ApiResult<List>(phoneInfos);
    }


    public ApiResult queryPhoneProducts(FeeType feeType, Operator operator, String province) {
        BpProductsReqData bpProductsReqData = buildBpProductsReqData(feeType, operator, province);
        BpResponseBase<List> bpResponseBase = getBpApiResponse(bpProductsReqData);

        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            return ApiResult.internalError(bpResponseBase.getErrdesc());
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
                Tuple2<Product, Boolean> diff = compareProduct(productExist, productNew);
                if (diff.s) {
                    productMapper.update(diff.f);
                }
                productNew.setProductId(productExist.getProductId());
            }
            products.add(productNew);
        }
        return new ApiResult<List>(products);

    }

    public ApiResult createOrder(Order order, String province, Operator operator) {
        int quantity = order.getQuantity();
        long productId = order.getProductId();
        Product product = productMapper.findByProductId(productId);
        if (null == product) {
            return new ApiResult(Errno.NOT_FOUND, "product is not found");
        }
        Product.Status productStatus = product.getStatus();
        if (productStatus != Product.Status.ONSALE) {
            return ApiResult.serviceUnavailable("product is invalid");
        }

        long curPrice = product.getRealPrice();
        long totalAmount = curPrice * quantity;
        order.setTotalAmount(totalAmount);

        BpPaymentReqData bpPaymentReqData = buildBpPayReqData(order, product, operator, province);

        BpResponseBase<BpPaymentInfo> bpResponseBase = getBpApiResponse(bpPaymentReqData);
        if (bpResponseBase.getResult().equals(BpResponseBase.FAILED)) {
            return ApiResult.internalError(bpResponseBase.getErrdesc());
        }

        String mapStr = JsonHelper.writeValueAsString(bpResponseBase.getDetailinfo());
        BpPaymentInfo bpPaymentInfo = JsonHelper.readValue(mapStr, BpPaymentInfo.class);
        String bpPayReturnInfo = bpPaymentInfo.getReturninfo();
        String bpPayReturnType = bpPaymentInfo.getReturntype();
        String bpOrderId = bpPaymentInfo.getOrderid();

        order.setCurPrice(curPrice);
        order.setOuterId(bpOrderId);

        order.setStatus(Order.Status.TOPAY);
        orderMapper.add(order);

        PayReturnType returnType = BpReturnType.convFromBpRetType(Integer.parseInt(bpPayReturnType));
        PayReturnInfo payReturnInfo = new PayReturnInfo(order, bpPayReturnInfo, returnType);
        return new ApiResult<PayReturnInfo>(payReturnInfo);
    }

    public ApiResult queryOrderDetail(long orderId) {
        Order order = orderMapper.findByOrderId(orderId);
        if (null == order) {
            return new ApiResult(Errno.NOT_FOUND, "order is not found");
        }

        try {
            refreshOrder(order);
        } catch (BpException e) {
            return ApiResult.internalError(e.getMessage());
        }

        return new ApiResult<Order>(order);
    }

    public void refreshOrder(Order order) throws BpException {
        Order.Status statusOld = order.getStatus();
        long orderId = order.getOrderId();
        if (!needRefreshStatus(statusOld)) {
            return;
        }

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
        order.setStatus(orderStatus);

        orderMapper.updateStatus(orderId, orderStatus);
        return;

    }

    public ApiResult countOrders(QueryOrderType queryOrderType, Optional<String> userId, Optional<String> phone) {
        int count = 0;
        if (queryOrderType == QueryOrderType.BY_PHONE) {
            if (!phone.isPresent()) {
                return ApiResult.badRequest("phone is needed");
            }
            count = orderMapper.countByPhone(phone.get());
        } else {
            if (!userId.isPresent()) {
                return ApiResult.badRequest("userId is needed");
            }
            count = orderMapper.countByUser(userId.get());
        }
        return new ApiResult(Errno.OK, count);

    }

    public ApiResult pages(QueryOrderType queryOrderType, Optional<String> userId, Optional<String> phone,
                           long orderId, int pages, int count, boolean backword) {
        Paging paging = new Paging().setTable(OrderMapper.Sql.TABLE)
                .setRowId("orderId").setCount(pages, count);

        if (queryOrderType == QueryOrderType.BY_PHONE) {
            if (!phone.isPresent()) {
                return ApiResult.badRequest("phone is needed");
            }
            paging.setWhere("phone = #{phone}").setParams("phone", phone.get());
        } else {
            if (!userId.isPresent()) {
                return ApiResult.badRequest("userId is needed");
            }
            paging.setWhere("userId = #{userId}").setParams("userId", userId.get());
        }

        List<Long> orderIds;
        if (orderId == -1) {
            orderIds = orderMapper.first(paging);
        } else {
            paging.setParams("orderId", orderId);
            orderIds = backword ? orderMapper.backward(paging) : orderMapper.forward(paging);
        }

        return new ApiResult<List>(paging.pages(orderIds, count));

    }


    public ApiResult list(QueryOrderType queryOrderType, Optional<String> userId, Optional<String> phone, long orderId, int count) {
        Paging paging = new Paging().setTable(OrderMapper.Sql.TABLE)
                .setRowId("orderId").setCount(1, count);

        if (queryOrderType == QueryOrderType.BY_PHONE) {
            if (!phone.isPresent()) {
                return ApiResult.badRequest("phone is needed");
            }
            paging.setWhere("phone = #{phone}").setParams("phone", phone.get());
        } else {
            if (!userId.isPresent()) {
                return ApiResult.badRequest("userId is needed");
            }
            paging.setWhere("userId = #{userId}").setParams("userId", userId.get());
        }


        List<Order> orders;
        paging.setParams("orderId", orderId);
        orders = orderMapper.find(paging);
        for (Order order : orders) {
            refreshOrder(order);
        }
        return new ApiResult<List>(orders);
    }


    public boolean needRefreshStatus(Order.Status status) {
        boolean needRefresh = false;
        if (!Order.STATUS_DONE.contains(status)) {
            needRefresh = true;
        }
        return needRefresh;
    }

    public Tuple2<Product, Boolean> compareProduct(Product oldPro, Product newPro) {
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


    public BpResponseBase getBpApiResponse(BluePlusBaseData bluePlusBaseData) {
        String partid = bpRestService.getPartid();
        String dataToEncrypt = JsonHelper.writeValueAsString(bluePlusBaseData);
        String curTime = String.valueOf(System.currentTimeMillis());
        String data = "";
        String sign = "";
        try {
            data = BpAesUtil.encrypt(dataToEncrypt, bpRestService.getSecret());
            sign = BpSignUtil.getSignAndMD5(partid, data, curTime);
        } catch (Exception e) {
            throw new BpException(e);
        }


        BluePlusRequestBody requestBody = new BluePlusRequestBody(bpRestService.getPartid(), data, curTime, sign);


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


}
