package com.sogou.pay.fee.api;


import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.manager.OrderManager;
import com.sogou.pay.fee.model.ApiResult;
import com.sogou.pay.fee.model.PhoneInfo;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by nahongxu on 2016/6/12.
 */
@Api(name = "ORDER API", description = "order api")
@RestController
@RequestMapping(value = {"/api"})
public class OrderController {

    @Autowired
    private OrderManager orderManager;

    private int pages;
    private int count;

    @Autowired
    public OrderController(Environment env) {
        this.pages = Integer.parseInt(env.getProperty("list.pages", "2"));
        this.count = Integer.parseInt(env.getProperty("list.count", "3"));
    }


    @ApiMethod(description = "query phone info")
    @RequestMapping(value = "/phoneinfo", method = RequestMethod.GET)
    public ApiResult queryPhoneInfo(
            @ApiQueryParam(name = "phones", description = "phone list")
            @RequestParam List<String> phones) {

        return orderManager.queryPhoneInfo(phones);
    }

    @ApiMethod(description = "query phone products")
    @RequestMapping(value = "/phoneProducts", method = RequestMethod.GET)
    public ApiResult queryPhoneProducts(
            @ApiQueryParam(name = "feeType", description = "phone or flow")
            @RequestParam Product.FeeType feeType,
            @ApiQueryParam(name = "operator", description = "operator")
            @RequestParam PhoneInfo.Operator operator,
            @ApiQueryParam(name = "province", description = "province")
            @RequestParam String province) {

        return orderManager.queryPhoneProducts(feeType, operator, province);

    }

    @ApiMethod(description = "post an order")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ApiResult addOrder(
            @ApiQueryParam(name = "userId", description = "userId")
            @RequestParam Optional<String> userId,
            @ApiQueryParam(name = "phone", description = "phone")
            @RequestParam String phone,
            @ApiQueryParam(name = "specifiedNo", description = "specifiedNo")
            @RequestParam Optional<String> specifiedNo,
            @ApiQueryParam(name = "feetype", description = "feetype")
            @RequestParam Product.FeeType feeType,
            @ApiQueryParam(name = "productId", description = "productId")
            @RequestParam long productId,
            @ApiQueryParam(name = "quantity", description = "quantity")
            @RequestParam Optional<Integer> quantity,
            @ApiQueryParam(name = "payChannel", description = "payChannel")
            @RequestParam Order.Channel payChannel,
            @ApiQueryParam(name = "payTerminal", description = "payTerminal")
            @RequestParam Order.PayTerminal payTerminal,
            @ApiQueryParam(name = "province", description = "province code")
            @RequestParam String province,
            @ApiQueryParam(name = "operator", description = "operator code")
            @RequestParam PhoneInfo.Operator operator) {
        if (quantity.isPresent() && quantity.get() != 1) {
            return ApiResult.badRequest("quantity can be null or 1");
        }

        Order order = new Order();
        if (userId.isPresent()) {
            order.setUserId(userId.get());
        }
        if (specifiedNo.isPresent()) {
            order.setSpecifiedNo(specifiedNo.get());
        }


        order.setPhone(phone);
        order.setFeeType(feeType);
        order.setProductId(productId);
        order.setQuantity(quantity.orElse(1));
        order.setPayChanel(payChannel);
        order.setPayTerminal(payTerminal);
        return orderManager.createOrder(order, province, operator);

    }

    @ApiMethod(description = "query order detail by orderId")
    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public ApiResult getOrderDetail(
            @ApiPathParam(name = "orderId", description = "orderId")
            @PathVariable long orderId) {
        return orderManager.queryOrderDetail(orderId);

    }

    @ApiMethod(description = "count orders by userid or phone")
    @RequestMapping(value = "/orders/count", method = RequestMethod.GET)
    public ApiResult countOrders(
            @ApiQueryParam(name = "queryType", description = "query type,phone or user")
            @RequestParam Optional<Order.QueryOrderType> queryType,
            @ApiQueryParam(name = "userId", description = "userId")
            @RequestParam Optional<String> userId,
            @ApiQueryParam(name = "phone", description = "phone")
            @RequestParam Optional<String> phone
    ) {
        if(queryType.isPresent()&& (queryType.get()==Order.QueryOrderType.BY_USERID)){
            return ApiResult.notAccept("not support query by user now");
        }
        return orderManager.countOrders(queryType.orElse(Order.QueryOrderType.BY_PHONE), userId, phone);
    }

    @ApiMethod(description = "query first order pages")
    @RequestMapping(value = "/orders/pages", method = RequestMethod.GET)
    public ApiResult firstPage(
            @ApiQueryParam(name = "queryType", description = "query type,phone or user")
            @RequestParam Optional<Order.QueryOrderType> queryType,
            @ApiQueryParam(name = "userId", description = "userId")
            @RequestParam Optional<String> userId,
            @ApiQueryParam(name = "phone", description = "phone")
            @RequestParam Optional<String> phone
    ) {
        if(queryType.isPresent()&& (queryType.get()==Order.QueryOrderType.BY_USERID)){
            return ApiResult.notAccept("not support query by user now");
        }
        return orderManager.pages(queryType.orElse(Order.QueryOrderType.BY_PHONE), userId, phone, -1, pages, count, true);
    }

    @ApiMethod(description = "query order pages forward by orderId")
    @RequestMapping(value = "/orders/pages/forward", method = RequestMethod.GET)
    public ApiResult forwardPage(
            @ApiQueryParam(name = "queryType", description = "query type,phone or user")
            @RequestParam Optional<Order.QueryOrderType> queryType,
            @ApiQueryParam(name = "userId", description = "userId")
            @RequestParam Optional<String> userId,
            @ApiQueryParam(name = "phone", description = "phone")
            @RequestParam Optional<String> phone,
            @ApiQueryParam(name = "orderId", description = "orderId")
            @RequestParam long orderId
    ) {
        if(queryType.isPresent()&& (queryType.get()==Order.QueryOrderType.BY_USERID)){
            return ApiResult.notAccept("not support query by user now");
        }
        return orderManager.pages(queryType.orElse(Order.QueryOrderType.BY_PHONE), userId, phone, orderId, pages, count, false);
    }

    @ApiMethod(description = "query order pages backward by orderId")
    @RequestMapping(value = "/orders/pages/backward", method = RequestMethod.GET)
    public ApiResult backwardPage(
            @ApiQueryParam(name = "queryType", description = "query type,phone or user")
            @RequestParam Optional<Order.QueryOrderType> queryType,
            @ApiQueryParam(name = "userId", description = "userId")
            @RequestParam Optional<String> userId,
            @ApiQueryParam(name = "phone", description = "phone")
            @RequestParam Optional<String> phone,
            @ApiQueryParam(name = "orderId", description = "orderId")
            @RequestParam long orderId
    ) {
        if(queryType.isPresent()&& (queryType.get()==Order.QueryOrderType.BY_USERID)){
            return ApiResult.notAccept("not support query by user now");
        }
        return orderManager.pages(queryType.orElse(Order.QueryOrderType.BY_PHONE), userId, phone, orderId, pages, count, true);
    }

    @ApiMethod(description = "list orders backword by orderId")
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    public ApiResult listOrders(
            @ApiQueryParam(name = "queryType", description = "query type,phone or user")
            @RequestParam Optional<Order.QueryOrderType> queryType,
            @ApiQueryParam(name = "userId", description = "userId")
            @RequestParam Optional<String> userId,
            @ApiQueryParam(name = "phone", description = "phone")
            @RequestParam Optional<String> phone,
            @ApiQueryParam(name = "orderId", description = "orderId")
            @RequestParam Optional<Long> orderId
    ) {
        if(queryType.isPresent()&& (queryType.get()==Order.QueryOrderType.BY_USERID)){
            return ApiResult.notAccept("not support query by user now");
        }
        return orderManager.list(queryType.orElse(Order.QueryOrderType.BY_PHONE), userId, phone, orderId, count);
    }

}
