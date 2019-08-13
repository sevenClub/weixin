package com.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import com.yangmj.common.CommonQuery;
import com.yangmj.common.MyPageInfo;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import com.yangmj.mapper.OrderDetailsMapper;
import com.yangmj.mapper.OrderItemMapper;
import com.yangmj.mapper.ProjectItemMapper;
import com.yangmj.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Autowired
    private ProjectItemMapper projectItemMapper;

    @Value("${login.pageUrl}")
    private  String loginPageUrl;
    @Override
    public MyPageInfo<OrderItem> queryOrderItemAll(OrderItem orderItem) {
        PageHelper.startPage(orderItem.getPageNum(),orderItem.getPageSize());
        List<OrderItem> orderItemList = orderItemMapper.queryOrderItemAll(orderItem);
        //订单信息返回页面包含该订单目前的人数
        List<Map> listNumCount = orderDetailsMapper.selectConversationList();
        List<OrderDetails> joinedOpenidMaps = orderDetailsMapper.queryJoinedOpenid();
        HashMap<Object, Object> hashMap = CommonQuery.setCurrentNum(listNumCount);
        if(!CollectionUtils.isEmpty(orderItemList)){
            //订单信息不是空的时候，获取该订单的url,订单的地址
//            ProjectItem projectItem = new ProjectItem();
//            List<Map> mapUrls = projectItemMapper.queryAllProjectUrl();
            for (int i = 0; i < orderItemList.size(); i++) {
                OrderItem orderItemquery = orderItemList.get(i);
                //通过订单表的id获取这个id对应的总数量 计算总数的为long的需要转为int型
                System.out.println("查询的订单idorderItemquery.getId()"+orderItemquery.getId());
                Number numCount = (Number)hashMap.get(orderItemquery.getId());
                if (null != numCount) {
                    orderItemquery.setCurrNum(numCount.intValue());
                }
                //订单的aa和免费
                String feeTags = orderItemquery.getFeeTags();
                if ("AA".equals(feeTags)) {
                    orderItemquery.setFeeTags(SystemDefault.PAY_AA);
                    orderItemquery.setPerCost(orderItemquery.getEndPrice()+"/人");
                } else {
                    orderItemquery.setFeeTags(SystemDefault.PAY_OO);
                    orderItemquery.setPerCost(SystemDefault.PAY_OO);
                }
                //设置图片的地址
                orderItemquery.setSportImgUrl(orderItemquery.getFirstPageUrl());
                //判断当前登录人是否参加订单了，details的id
//                String wechatOpenid = orderItemquery.getWechatOpenid();
                //当前登录人的传递过来的id
                String startWechatOpenid = orderItem.getStartWechatOpenid();
                for (int j = 0; j <joinedOpenidMaps.size() ; j++) {
                    OrderDetails orderDetails = joinedOpenidMaps.get(j);
                    String detailOpenid = orderDetails.getWechatOpenid();
                    if (startWechatOpenid.equals(detailOpenid)) {
                        orderItemquery.setJoined(true);
                    } else {
                        orderItemquery.setJoined(false);
                    }
                }
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

        //查看已经结束的的，包含2： 正常结束 3：时间到组队失败 4：发起者人为取消订单
        if ("2".equals(orderStatus)) {
            orderStatus = "'2','3','4'";
        }
//        int j = 1 / 0;
        List<OrderItem> orderItemList = orderItemMapper.queryLeaderOrFollower(isCaptain, orderStatus,wechatOpenid);

        List<Map> listNumCount = orderDetailsMapper.selectConversationList();
        HashMap<Object, Object> hashMap = CommonQuery.setCurrentNum(listNumCount);
        if(!CollectionUtils.isEmpty(orderItemList)){
            for (int i = 0; i < orderItemList.size(); i++) {
                OrderItem orderItemquery = orderItemList.get(i);
                //通过订单表的id获取这个id对应的总数量 计算总数的为long的需要转为int型
                Number numCount = (Number)hashMap.get(orderItemquery.getId());
                orderItemquery.setCurrNum(numCount.intValue());
                //页面返回字段
                String feeTags = orderItemquery.getFeeTags();
                if ("AA".equals(feeTags)) {
                    orderItemquery.setPerCost(orderItemquery.getEndPrice() + "/人");
                } else {
                    orderItemquery.setPerCost(SystemDefault.PAY_OO);
                }
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

    @Override
    public List<OrderItem> timerCloseOrderNormalEnd() {
        return orderItemMapper.timerCloseOrderNormalEnd();
    }
}
