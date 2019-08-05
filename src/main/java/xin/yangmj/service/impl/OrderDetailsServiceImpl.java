package xin.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.OrderDetails;
import xin.yangmj.entity.OrderItem;
import xin.yangmj.mapper.OrderDetailsMapper;
import xin.yangmj.mapper.OrderItemMapper;
import xin.yangmj.service.OrderDetailsService;

import java.util.List;
import java.util.Map;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Override
    public MyPageInfo<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails) {
        PageHelper.startPage(orderDetails.getPageNum(),orderDetails.getPageSize());
        List<OrderDetails> orderDetailsList = orderDetailsMapper.queryOrderDetailsAll(orderDetails);
        MyPageInfo<OrderDetails> orderDetailsPageInfo = new MyPageInfo<>(orderDetailsList);
        return orderDetailsPageInfo;
    }

    @Override
    @Transactional
    public String insertOrderDetails(OrderDetails orderDetails) {

        //入库前判断当前的人数是达到规定的总人数
        String result = "00";
        List<Map> partInAndAllNumMap = orderDetailsMapper.judgeStaffFull(orderDetails.getOrderId());
        Map mapNum = partInAndAllNumMap.get(0);
//        isFull 0满员 1 未满员
        String isFull = (String)mapNum.get("isFull");
        if("1".equals(isFull)){
            //参与人数 partInNum
            int partInNum = ((Number)mapNum.get("partInNum")).intValue();
//        Number numCount = (Number)hashMap.get(orderItemquery.getId());
            //项目总人数 totalNum
            int totalNum = ((Number)mapNum.get("totalNum")).intValue();
            int diValue = totalNum - partInNum ;
            if(diValue > 0){
                int resultOrder = orderDetailsMapper.insertOrderDetails(orderDetails);
                //活动项目最后一个人满的时候，需要将人员设置为满员
                if(1 ==  diValue && 1 == resultOrder){

                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(orderDetails.getOrderId());
                    List<OrderItem> orderItemList = orderItemMapper.queryOrderItemAll(orderItem);
                    if (!CollectionUtils.isEmpty(orderItemList)) {
                        OrderItem queryItem = orderItemList.get(0);
                        //                0满员 1 未满员
                        queryItem.setIsFull("0");
                        //                0 组队中 1 待参加 2 已结束
                        queryItem.setOrderStatus("1");
                        //                0可以开始游戏，1还在招募人员
                        queryItem.setGameStatus("0");
                        orderItemMapper.updateOrderItemByKey(queryItem);
                    }

                }
                return result = "00";
            }else {
                return result = "01";
            }
        }else {
            return "参数不完整";
        }

    }
}
