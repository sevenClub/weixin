package xin.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.OrderDetails;
import xin.yangmj.mapper.OrderDetailsMapper;
import xin.yangmj.service.OrderDetailsService;

import java.util.List;
@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Override
    public MyPageInfo<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails) {
        PageHelper.startPage(orderDetails.getPageNum(),orderDetails.getPageSize());
        List<OrderDetails> orderDetailsList = orderDetailsMapper.queryOrderDetailsAll(orderDetails);
        MyPageInfo<OrderDetails> orderDetailsPageInfo = new MyPageInfo<>(orderDetailsList);
        return orderDetailsPageInfo;
    }

    @Override
    public int insertOrderDetails(OrderDetails orderDetails) {
        return orderDetailsMapper.insertOrderDetails(orderDetails);
    }
}
