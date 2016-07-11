package com.sogou.pay.fee.service;

import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.model.PayReturnInfo;
import com.sogou.pay.fee.model.PhoneInfo;

import java.util.List;

/**
 * Created by nahongxu on 2016/7/4.
 */
public interface FeeService {
    String PAY_CALLBACK_URL = "https://pay.sogou.com/";

    List<PhoneInfo> queryPhoneInfos(List<String> phones);

    List<Product> queryPhoneProducts(Product.FeeType feeType, PhoneInfo.Operator operator, String province);

    PayReturnInfo createOrder(Order order, Product product, String province, PhoneInfo.Operator operator);

    Order.Status queryOrderStatus(Order order);


}
