package com.yangmj.controller;

import com.alibaba.fastjson.JSONObject;
import com.yangmj.common.MyPageInfo;
import com.yangmj.common.ResponseResult;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import com.yangmj.service.OrderDetailsService;
import com.yangmj.service.OrderItemService;
import com.yangmj.service.ProjectItemService;
import com.yangmj.util.CommonUtils;
import com.yangmj.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private ProjectItemService projectItemService;

    //详情页面的图片的信息
    @Value("${login.url}")
    private  String loginUrl;


    /**
     * 有条件查询订单
     * 查询所有的订单
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/findAllOrders")
    public ResponseResult queryOrderItemAll(@RequestBody OrderItem orderItem) {
        //运动类型
        String sportType = orderItem.getSportType();
        //人数范围
        String numType = orderItem.getNumType();
        //金额范围
        String costRMB = orderItem.getCostRMB();
        //时间范围
        String queryDate = orderItem.getQueryDate();
        //根据类型筛选
        if (!StringUtils.isEmpty(sportType)) {
            if ("all".equals(sportType)) {
                orderItem.setProjectId("");
            } else {
                orderItem.setProjectId(sportType);
            }
        }
         /*
            01 5人以下
            02 5-8人
            03 8-10人
            04 10人以上
            05 所有人
         */
        //根据人数筛选
        if (!StringUtils.isEmpty(numType)) {
            Integer num = Integer.valueOf(numType);
            switch (num) {
                case 1:
                    orderItem.setTotalNum(0);
                    orderItem.setTotalNumUp(5);
                    break;
                case 2:
                    orderItem.setTotalNum(5);
                    orderItem.setTotalNumUp(8);
                    break;
                case 3:
                    orderItem.setTotalNum(8);
                    orderItem.setTotalNumUp(10);
                    break;
                case 4:
                    orderItem.setTotalNum(10);
                    break;
                default:
                    break;
            }
        }
        /*
            根据订单总金额筛选
            01 免费
            02 人均50以内
            03 人均50-100
            04 人均100以上
            all 所有
         */
        if (!StringUtils.isEmpty(costRMB)) {
            if ("01".equals(costRMB)) {
//                OO: 免费 AA:AA制
                orderItem.setFeeTags("AA");
            } else if ("02".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(0));
                orderItem.setEndPrice(new BigDecimal(50));
            } else if ("03".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(50));
                orderItem.setEndPrice(new BigDecimal(100));
            } else if ("04".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(100));
            } else {
                //查询所有的
            }
        }
        //根据日期筛选,转换时间的格式
//        if (!StringUtils.isEmpty(queryDate)) {
////            yyyy/mm/dd to yyyy-mm-dd
//            String yyyyToYYYY = DateUtil.yyyyToYYYY(queryDate);
//            orderItem.setActureStartTm(yyyyToYYYY);
//        }
        MyPageInfo<OrderItem> projectItemPageInfo = orderItemService.queryOrderItemAll(orderItem);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, projectItemPageInfo);
        return resp;
    }

    /**
     * 查看我发起运动项目和我参加的运动项目
     * 运动的最新状态
     */
    @PostMapping("/queryLeaderOrFollower")
    public ResponseResult queryLeaderOrFollower(@RequestBody JSONObject jsonObject) {
        ResponseResult resp = null;
        if (jsonObject.size() == 0) {
            log.info("参数缺失，请稍后再试");
            return resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        //isCaptain 0我发起的 1 我参加的
        //orderStatus 0 组队中 1 待参加 2 已结束
        String isCaptain = jsonObject.get("isCaptain").toString();
        String orderStatus = jsonObject.get("orderStatus").toString();
        String wechatOpenid = jsonObject.get("wechatOpenid").toString();

        try {
            List<OrderItem> orderItems = orderItemService.queryLeaderOrFollower(isCaptain, orderStatus, wechatOpenid);
            resp = ResponseResult.makeSuccResponse(null, orderItems);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败queryLeaderOrFollower，原因"+e.toString());
            resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        return resp;
    }


    /**
     * 创建订单的信息
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/createOrderItem")
    @Transactional
    public ResponseResult createProjectItem(@RequestBody OrderItem orderItem) {
        ResponseResult resp = null;
        //验证手机号是否输入或输入是否合法
        String phone = orderItem.getContactDir();
        String phoneResult = CommonUtils.checkPhone(phone);
        if ("01".equals(phoneResult)) {
            return ResponseResult.makeFailResponse(SystemDefault.MOBILE_MISSING, "");
        }
        if ("02".equals(phoneResult)){
                return ResponseResult.makeFailResponse(SystemDefault.MOBILE_ERROR, "");
        }

        OrderDetails orderDetails = new OrderDetails();

        try {
            //发起订单的人初始为1
            orderItem.setCurrNum(1);
            //0开始游戏，1还在招募人员，主要是针对手动关闭订单开始游戏
            orderItem.setGameStatus("1");
            Integer totalNum = orderItem.getTotalNum();
            //订单只有一个人的情况
            if (1 == totalNum) {
                orderItem.setOrderStatus("1");
                orderItem.setIsFull("0");
            } else {
                //0 组队中 1 待参加 2： 正常结束 3：时间到组队失败 4：发起者人为取消订单
                orderItem.setOrderStatus("0");
                //0满员 1 未满员
                orderItem.setIsFull("1");
            }
            //TODO 创建订单的时候需要判断当前时间点是否冲突,待讨论定义
            //创建订单如果是AA的，设置每个人的花费
            String feeTags = orderItem.getFeeTags();
            if ("AA".equals(feeTags)) {
                System.out.println("totalNum***"+totalNum);
                BigDecimal projectCost = orderItem.getProjectCost();
                System.out.println("projectCost***"+projectCost);
                BigDecimal perCost = projectCost.divide(new BigDecimal(totalNum));
                orderItem.setEndPrice(perCost);
            } else {
                orderItem.setEndPrice(new BigDecimal(0));
            }
            int item = orderItemService.insertOrderItem(orderItem);
            //订单创建的时候，需要将发起人的信息插入到order_details订单明细表
            orderDetails.setOrderId(orderItem.getId());
            orderDetails.setWechatOpenid(orderItem.getStartWechatOpenid());
            // 0 在线  1 退出拼单
            orderDetails.setIsActive("0");
            orderDetails.setProjectFee(orderItem.getProjectCost());
            //0是 1否 发起人
            orderDetails.setIsCaptain("0");
            //手机号
            orderDetails.setContactDir(phone);
            String details = orderDetailsService.insertOrderDetails(orderDetails);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        return resp;
    }


    /**
     * 订单人数未达标，人为关闭订单，即使人员没有满也可以开始游戏
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/tmpStartGame")
    public ResponseResult tmpStartGame(@RequestBody OrderItem orderItem) {
//        安全需要自己从表或者然后更新
        MyPageInfo<OrderItem> queryOrderItemAll = orderItemService.queryOrderItemAll(orderItem);
        OrderItem orderItemBean = queryOrderItemAll.getList().get(0);
//        0满员 1 未满员
        orderItemBean.setIsFull("0");
//        0 组队中 1 待参加 2： 正常结束 3：时间到组队失败 4：发起者人为取消订单）
        orderItemBean.setOrderStatus("1");
//        按钮显示是否可以玩，不管人数满员否，0开始游戏，1还在招募人员
        orderItemBean.setGameStatus("0");
        orderItem.setUpdateTime(DateUtil.formatDateTime());
        ResponseResult resp = null;
        try {
            int itemResult = orderItemService.updateOrderItemByKey(orderItemBean);
            resp = ResponseResult.makeSuccResponse(null, itemResult);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse(SystemDefault.NETWORK_ERROR, "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    /**
     * 发起人取消订单
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/cancleOrder")
    public ResponseResult cancleOrder(@RequestBody OrderItem orderItem) {

        //        0 组队中 1 待参加 2： 正常结束 3：时间到组队失败 4：发起者人为取消订单）
        orderItem.setOrderStatus("4");
        orderItem.setUpdateTime(DateUtil.formatDateTime());

        ResponseResult resp = null;
        try {
            int item = orderItemService.updateOrderItemByKeySelective(orderItem);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse("网络错误", "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    /**
     * 定时任务启动，关闭时间到期还没有满员的订单
     *
     * @return
     */
    //@PostMapping("/timerCloseOrder") //页面测试
    //定时任务时间的
    @Scheduled(cron = "* 0/15 * * * ?")
    public void timerCloseOrderNoFull() {
        List<OrderItem> orderItemList = orderItemService.timerCloseOrder();
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderItemList)) {
            hashMap.put("ids", orderItemList);
            //3 为人数未满订单关闭
            hashMap.put("status", "3");
            //批量更新数据的信息
            int i = orderItemService.updateOrderItemBatch(hashMap);
            if (i > 0) {
                log.info("批量更新关闭时间到期还没有满员的订单:{}", i);
            }
        }
    }

    /**
     * 订单时间到了最后的时间，该订单进行关闭
     */
//    @PostMapping("/timerCloseOrderNormalEnd")
    @Scheduled(cron = "* 0/15 * * * ?")
    public void timerCloseOrderNormalEnd() {
        List<OrderItem> orderItemList = orderItemService.timerCloseOrderNormalEnd();
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderItemList)) {
            hashMap.put("ids", orderItemList);
            //2为订单正常关闭
            hashMap.put("status", "2");
            //批量更新数据的信息
            int i = orderItemService.updateOrderItemBatch(hashMap);
            if (i > 0) {
                log.info("批量更新关闭时间到期正常结束的订单数量:{}", i);
            }
        }

    }

}
