package com.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import com.yangmj.common.CommonQuery;
import com.yangmj.common.MyPageInfo;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderItem;
import com.yangmj.mapper.OrderDetailsMapper;
import com.yangmj.mapper.OrderItemMapper;
import com.yangmj.mapper.ProjectItemMapper;
import com.yangmj.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
                    orderItemquery.setPerCost(SystemDefault.AMOUNT_SYMBOL+orderItemquery.getEndPrice());
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
                //循环当前的订单id
                Integer itemqueryId = orderItemquery.getId();
                int joindNum = orderDetailsMapper.verifyRepeatedPartIn(itemqueryId, startWechatOpenid);
                if (joindNum > 0) {
                    orderItemquery.setJoined(true);
                } else {
                    orderItemquery.setJoined(false);
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
    public MyPageInfo<OrderItem> queryLeaderOrFollower(HashMap hashMapparam) {
        Integer pageNum = (Integer)hashMapparam.get("pageNum");
        Integer pageSize = (Integer)hashMapparam.get("pageSize");
        PageHelper.startPage(pageNum,pageSize);

        List<OrderItem> orderItemList = orderItemMapper.queryLeaderOrFollower(hashMapparam);

        List<Map> listNumCount = orderDetailsMapper.selectConversationList();
        HashMap<Object, Object> hashMap = CommonQuery.setCurrentNum(listNumCount);
        if(!CollectionUtils.isEmpty(orderItemList)){
            for (int i = 0; i < orderItemList.size(); i++) {
                OrderItem orderItemquery = orderItemList.get(i);
                //通过订单表的id获取这个id对应的总数量 计算总数的为long的需要转为int型
                if (hashMap.size()>0) {
                    Number numCount = (Number)hashMap.get(orderItemquery.getId());
                    if (!StringUtils.isEmpty(numCount)) {
                        orderItemquery.setCurrNum(numCount.intValue());
                    }

                }
                //页面返回字段
                String feeTags = orderItemquery.getFeeTags();
                if ("AA".equals(feeTags)) {
                    orderItemquery.setPerCost(SystemDefault.PAY_AA);
                } else {
                    orderItemquery.setPerCost(SystemDefault.PAY_OO);
                }
            }
        }
        MyPageInfo<OrderItem> orderItemPageInfo = new MyPageInfo<>(orderItemList);
        return orderItemPageInfo;
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

    @Override
    public OrderItem queryOrderItemByKey(OrderItem record) {
        return orderItemMapper.queryOrderItemByKey(record);
    }
}
