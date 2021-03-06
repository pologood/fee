package com.sogou.pay.fee.manager;

import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.mapper.OrderMapper;
import com.sogou.pay.fee.mapper.ProductMapper;
import com.sogou.pay.fee.model.ApiResult;
import com.sogou.pay.fee.model.Errno;
import com.sogou.pay.fee.model.PayReturnInfo;
import com.sogou.pay.fee.model.PhoneInfo;
import com.sogou.pay.fee.service.blueplus.BpException;
import com.sogou.pay.fee.service.blueplus.BpService;
import commons.mybatis.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class OrderManager {

    @Autowired
    private BpService bpService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    private String tokenKey;

    public static final String PHONE_FORMAT = "^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$";


    @Autowired
    public OrderManager(Environment env) {

        tokenKey = env.getRequiredProperty("security.token");
    }

    public ApiResult queryPhoneInfo(List<String> phones) {
        if(!isPhoneNumber(phones)){
            return ApiResult.badRequest("wrong phone number");
        }
        List<PhoneInfo> phoneInfos = bpService.queryPhoneInfos(phones);
        return new ApiResult<List>(phoneInfos);
    }


    public ApiResult queryPhoneProducts(Product.FeeType feeType, PhoneInfo.Operator operator, String province) {
        List<Product> products = bpService.queryPhoneProducts(feeType, operator, province);
        return new ApiResult<List>(products);

    }

    public ApiResult createOrder(Order order, String province, PhoneInfo.Operator operator) {
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
        order.setCurPrice(curPrice);

        try {
            PayReturnInfo payReturnInfo = bpService.createOrder(order, product, province, operator);
            return new ApiResult<PayReturnInfo>(payReturnInfo);
        }catch (BpException bpException){
            return ApiResult.internalError(bpException.getMessage());
        }
    }

    public ApiResult queryOrderDetail(long orderId) {
        Order order = orderMapper.findByOrderId(orderId);
        if (null == order) {
            return new ApiResult(Errno.NOT_FOUND, "order is not found");
        }
        refreshOrder(order);
        return new ApiResult<Order>(order);
    }

    public void refreshOrder(Order order) {
        Order.Status statusOld = order.getStatus();
        long orderId = order.getOrderId();
        if (!needRefreshStatus(statusOld)) {
            return;
        }

        Order.Status statusNew = bpService.queryOrderStatus(order);
        order.setStatus(statusNew);

        orderMapper.updateStatus(orderId, statusNew);
        return;

    }

    public ApiResult countOrders(Order.QueryOrderType queryOrderType, Optional<String> userId, Optional<String> phone) {
        int count;
        if (queryOrderType == Order.QueryOrderType.BY_PHONE) {
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

    public ApiResult pages(Order.QueryOrderType queryOrderType, Optional<String> userId, Optional<String> phone,
                           long orderId, int pages, int count, boolean backword) {
        Paging paging = new Paging().setTable(OrderMapper.Sql.TABLE)
                .setRowId("orderId").setCount(pages, count);

        if (queryOrderType == Order.QueryOrderType.BY_PHONE) {
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


    public ApiResult list(Order.QueryOrderType queryOrderType, Optional<String> userId, Optional<String> phone,
                          Optional<Long> orderId, int count) {
        Paging paging = new Paging().setTable(OrderMapper.Sql.TABLE)
                .setRowId("orderId").setCount(1, count);
        if(orderId.isPresent()){
            paging.setParams("orderId",orderId.get());
        }

        if (queryOrderType == Order.QueryOrderType.BY_PHONE) {
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

    public boolean isPhoneNumber(List<String> phones){
        boolean isPhone=true;
        for(String phone:phones){
            if(!phone.matches(PHONE_FORMAT)){
                isPhone=false;
                break;
            }
        }
        return isPhone;
    }


}
