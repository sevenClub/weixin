package xin.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xin.yangmj.common.CommonQuery;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.OrderItem;
import xin.yangmj.mapper.OrderDetailsMapper;
import xin.yangmj.mapper.OrderItemMapper;
import xin.yangmj.service.OrderItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Override
    public MyPageInfo<OrderItem> queryOrderItemAll(OrderItem orderItem) {
        PageHelper.startPage(orderItem.getPageNum(),orderItem.getPageSize());
        List<OrderItem> orderItemList = orderItemMapper.queryOrderItemAll(orderItem);
        //订单信息返回页面包含该订单目前的人数
        List<Map> listNumCount = orderDetailsMapper.selectConversationList();
        HashMap<Object, Object> hashMap = CommonQuery.setCurrentNum(listNumCount);
       /* HashMap<Object, Object> hashMap = new HashMap<>();
        for(int i = 0;i<listNumCount.size() ;i++){
            Map map = listNumCount.get(i);
            Object key = map.get("orderId");
            Object value = map.get("countNumFore");
            hashMap.put(key,value);
        }*/

        if(!CollectionUtils.isEmpty(orderItemList)){
            for (int i = 0; i < orderItemList.size(); i++) {
                OrderItem orderItemquery = orderItemList.get(i);
                //通过订单表的id获取这个id对应的总数量 计算总数的为long的需要转为int型
                Number numCount = (Number)hashMap.get(orderItemquery.getId());
                orderItemquery.setCurrNum(numCount.intValue());
            }
        }
        MyPageInfo<OrderItem> orderItemPageInfo = new MyPageInfo<>(orderItemList);
        return orderItemPageInfo;
    }

    @Override
    public int insertOrderItem(OrderItem record) {
        return orderItemMapper.insertOrderItem(record);
    }

    @Override
    public List<OrderItem> queryLeaderOrFollower(String isCaptain, String orderStatus,String wechatOpenid) {

        List<OrderItem> orderItemList = orderItemMapper.queryLeaderOrFollower(isCaptain, orderStatus,wechatOpenid);

        List<Map> listNumCount = orderDetailsMapper.selectConversationList();
        HashMap<Object, Object> hashMap = CommonQuery.setCurrentNum(listNumCount);

        if(!CollectionUtils.isEmpty(orderItemList)){
            for (int i = 0; i < orderItemList.size(); i++) {
                OrderItem orderItemquery = orderItemList.get(i);
                //通过订单表的id获取这个id对应的总数量 计算总数的为long的需要转为int型
                Number numCount = (Number)hashMap.get(orderItemquery.getId());
                orderItemquery.setCurrNum(numCount.intValue());
            }
        }
        return orderItemList;
    }

    @Override
    public int updateOrderItemByKey(OrderItem orderItem) {
        return orderItemMapper.updateOrderItemByKey(orderItem);
    }

    @Override
    public int cancleOrderByKey(Integer id) {


        return orderItemMapper.cancleOrderByKey(id);
    }

    @Override
    public int updateOrderItemByKeySelective(OrderItem orderItem) {
        return orderItemMapper.updateOrderItemByKeySelective(orderItem);
    }

    @Override
    public List<OrderItem> timerCloseOrder() {
        return orderItemMapper.timerCloseOrder();
    }

    @Override
    public int updateOrderItemBatch(HashMap hashMap) {
        return orderItemMapper.updateOrderItemBatch(hashMap);
    }
}
