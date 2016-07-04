package com.sogou.pay.fee.service;

import com.sogou.pay.fee.entity.Order;
import com.sogou.pay.fee.entity.Product;
import com.sogou.pay.fee.model.FeeType;
import com.sogou.pay.fee.model.Operator;
import com.sogou.pay.fee.model.PayReturnInfo;
import com.sogou.pay.fee.model.PhoneInfo;
import commons.utils.Tuple2;

import java.util.List;

/**
 * Created by nahongxu on 2016/7/4.
 */
public interface FeeService {

    public List<PhoneInfo> queryPhoneInfos(List<String> phones);

    public List<Product> queryPhoneProducts(FeeType feeType, Operator operator, String province);

    public PayReturnInfo createOrder(Order order, Product product,String province, Operator operator);

    public Order.Status queryOrderStatus(Order order);

    public static Tuple2<Product, Boolean> compareProduct(Product oldPro, Product newPro) {
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
