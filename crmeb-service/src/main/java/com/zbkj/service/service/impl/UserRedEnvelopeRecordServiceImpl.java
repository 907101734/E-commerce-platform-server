package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.enums.OneRedEnvelopeEnum;
import com.zbkj.common.enums.ThreeRedEnvelopeEnum;
import com.zbkj.common.enums.TwoRedEnvelopeEnum;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserBill;
import com.zbkj.common.model.user.UserRedEnvelope;
import com.zbkj.common.model.user.UserRedEnvelopeRecord;
import com.zbkj.common.model.video.Advertisement;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRedEnvelopeReceiveRequest;
import com.zbkj.common.request.UserRedEnvelopeRecordRequest;
import com.zbkj.common.response.UserRedEnvRecordCountResponse;
import com.zbkj.common.response.UserRedEnvRecordListResponse;
import com.zbkj.common.response.UserRedEnvRecordPriceResponse;
import com.zbkj.common.vo.DateLimitUtilVo;
import com.zbkj.common.vo.UserRedEnvelopeRecordVo;
import com.zbkj.service.dao.UserRedEnvelopeRecordDao;
import com.zbkj.service.service.AdvertisementService;
import com.zbkj.service.service.StoreOrderInfoService;
import com.zbkj.service.service.StoreOrderService;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.UserBillService;
import com.zbkj.service.service.UserRedEnvelopeRecordService;
import com.zbkj.service.service.UserRedEnvelopeService;
import com.zbkj.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * UserRedEnvelopeRecordServiceImpl
 * @author wtj
 * @date 2024/10/31
 */
@Service
public class UserRedEnvelopeRecordServiceImpl extends ServiceImpl<UserRedEnvelopeRecordDao, UserRedEnvelopeRecord> implements UserRedEnvelopeRecordService {

    private final Logger logger = LoggerFactory.getLogger(UserRedEnvelopeRecordServiceImpl.class);

    @Autowired
    private UserRedEnvelopeRecordDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserRedEnvelopeService userRedEnvelopeService;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Override
    public List<UserRedEnvelopeRecordVo> findPageLList(UserRedEnvelopeRecordRequest request, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<UserRedEnvelopeRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getStatus() != null) {
            queryWrapper.eq(UserRedEnvelopeRecord::getStatus, request.getStatus());
        }
        //时间范围
        if (StrUtil.isNotBlank(request.getDateLimit())) {
            DateLimitUtilVo dateLimit = com.zbkj.common.utils.DateUtil.getDateLimit(request.getDateLimit());
            //判断时间
            int compareDateResult = com.zbkj.common.utils.DateUtil.compareDate(dateLimit.getEndTime(), dateLimit.getStartTime(), Constants.DATE_FORMAT);
            if (compareDateResult == -1) {
                throw new CrmebException("开始时间不能大于结束时间！");
            }
            queryWrapper.between(UserRedEnvelopeRecord::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }

        if (StrUtil.isNotBlank(request.getReceiveDateLimit())) {
            DateLimitUtilVo dateLimit = com.zbkj.common.utils.DateUtil.getDateLimit(request.getReceiveDateLimit());
            //判断时间
            int compareDateResult = com.zbkj.common.utils.DateUtil.compareDate(dateLimit.getEndTime(), dateLimit.getStartTime(), Constants.DATE_FORMAT);
            if (compareDateResult == -1) {
                throw new CrmebException("开始时间不能大于结束时间！");
            }
            queryWrapper.between(UserRedEnvelopeRecord::getReceiveTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }

        if (StrUtil.isNotBlank(request.getNikeName())) {
            List<Integer> idList = userService.findIdListLikeName(request.getNikeName());
            if (CollUtil.isNotEmpty(idList)) {
                queryWrapper.in(UserRedEnvelopeRecord::getUid, idList);
            } else {
                return CollUtil.newArrayList();
            }
        }
        List<UserRedEnvelopeRecord> redEnvelopeRecords = dao.selectList(queryWrapper);

        List<Integer> uidList = redEnvelopeRecords.stream()
            .map(e -> e.getUid())
            .distinct()
            .collect(Collectors.toList());
        HashMap<Integer, User> userMap = userService.getMapListInUid(uidList);

        List<UserRedEnvelopeRecordVo> responseList = new ArrayList<>();
        for (UserRedEnvelopeRecord redEnvelopeRecord : redEnvelopeRecords) {
            UserRedEnvelopeRecordVo userRedEnvelopeRecordVo = new UserRedEnvelopeRecordVo();
            BeanUtils.copyProperties(redEnvelopeRecord, userRedEnvelopeRecordVo);
            userRedEnvelopeRecordVo.setUserName(userMap.get(redEnvelopeRecord.getUid()).getNickname());
            responseList.add(userRedEnvelopeRecordVo);
        }
        return responseList;
    }

    @Override
    public UserRedEnvRecordCountResponse getCount() {
        User currentUser = userService.getInfo();
        UserRedEnvRecordCountResponse userRedEnvRecordCountResponse = new UserRedEnvRecordCountResponse();

        //获取待领取数量
        LambdaQueryWrapper<UserRedEnvelopeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRedEnvelopeRecord::getUid, currentUser.getUid());
        queryWrapper.eq(UserRedEnvelopeRecord::getStatus, 0);
        userRedEnvRecordCountResponse.setWaitCount(dao.selectCount(queryWrapper));

        QueryWrapper<UserRedEnvelopeRecord> hisQueryWrapper = new QueryWrapper<>();
        hisQueryWrapper.eq("uid", currentUser.getUid());
        hisQueryWrapper.eq("status", 1);
        hisQueryWrapper.select("sum(price) as totalSum");
        List<Map<String, Object>> maps = dao.selectMaps(hisQueryWrapper);
        if (maps.get(0) == null) {
            userRedEnvRecordCountResponse.setHisIncome("0.0");
        } else {
            Object totalSum = maps.get(0).get("totalSum");
            userRedEnvRecordCountResponse.setHisIncome(totalSum.toString());
        }

        DateTime todayStart = DateUtil.beginOfDay(DateUtil.date());
        DateTime tomorrowStart = DateUtil.offsetDay(todayStart, 1);
        DateTime yesterdayStart = DateUtil.offsetDay(todayStart, -1);
        //今日收益
        QueryWrapper<UserRedEnvelopeRecord> todayQueryWrapper = new QueryWrapper<>();
        todayQueryWrapper.eq("uid", currentUser.getUid());
        todayQueryWrapper.eq("status", 1);
        todayQueryWrapper.between("receive_time", todayStart, tomorrowStart);
        todayQueryWrapper.select("sum(price) as todayTotalSum");
        List<Map<String, Object>> todaymaps = dao.selectMaps(todayQueryWrapper);
        if (todaymaps.get(0) == null) {
            userRedEnvRecordCountResponse.setTodayIncome("0.0");
        } else {
            Object todayTotalSum = todaymaps.get(0).get("todayTotalSum");
            userRedEnvRecordCountResponse.setTodayIncome(todayTotalSum.toString());
        }
        //昨日收益
        QueryWrapper<UserRedEnvelopeRecord> yesterdayQueryWrapper = new QueryWrapper<>();
        yesterdayQueryWrapper.eq("uid", currentUser.getUid());
        yesterdayQueryWrapper.eq("status", 1);
        yesterdayQueryWrapper.between("receive_time", yesterdayStart, todayStart);
        yesterdayQueryWrapper.select("sum(price) as yesterdayTotalSum");
        List<Map<String, Object>> yesterdaymaps = dao.selectMaps(yesterdayQueryWrapper);
        if (yesterdaymaps.get(0) == null) {
            userRedEnvRecordCountResponse.setYesterdayIncome("0.0");
        } else {
            Object yesterdayTotalSum = yesterdaymaps.get(0).get("yesterdayTotalSum");
            userRedEnvRecordCountResponse.setYesterdayIncome(yesterdayTotalSum.toString());
        }
        return userRedEnvRecordCountResponse;
    }

    @Override
    public List<UserRedEnvRecordListResponse> getWaitList() {
        User currentUser = userService.getInfo();
        //待领取红包
        LambdaQueryWrapper<UserRedEnvelopeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRedEnvelopeRecord::getUid, currentUser.getUid());
        queryWrapper.eq(UserRedEnvelopeRecord::getStatus, 0);
        List<UserRedEnvelopeRecord> redEnvelopeRecords = dao.selectList(queryWrapper);
        //今日已经领取红包
        DateTime todayStart = DateUtil.beginOfDay(DateUtil.date());
        DateTime tomorrowStart = DateUtil.offsetDay(todayStart, 1);

        LambdaQueryWrapper<UserRedEnvelopeRecord> hisQueryWrapper = new LambdaQueryWrapper<>();
        hisQueryWrapper.eq(UserRedEnvelopeRecord::getUid, currentUser.getUid());
        hisQueryWrapper.eq(UserRedEnvelopeRecord::getStatus, 1);
        hisQueryWrapper.between(UserRedEnvelopeRecord::getReceiveTime, todayStart, tomorrowStart);
        List<UserRedEnvelopeRecord> redEnvelopeRecords1 = dao.selectList(hisQueryWrapper);
        redEnvelopeRecords.addAll(redEnvelopeRecords1);

        List<UserRedEnvRecordListResponse> userRedEnvRecordListResponses = new ArrayList<>();
        for (UserRedEnvelopeRecord redEnvelopeRecord : redEnvelopeRecords) {
            UserRedEnvRecordListResponse userRedEnvRecordListResponse = new UserRedEnvRecordListResponse();
            BeanUtils.copyProperties(redEnvelopeRecord, userRedEnvRecordListResponse);
            if (userRedEnvRecordListResponse.getStatus().equals(0)) {
                userRedEnvRecordListResponse.setPrice(null);
            }
            userRedEnvRecordListResponse.setLinkAdAddr(systemAttachmentService.clearPrefix(redEnvelopeRecord.getLinkAdAddr()));
            userRedEnvRecordListResponses.add(userRedEnvRecordListResponse);
        }
        return userRedEnvRecordListResponses;
    }

    @Override
    public CommonPage<UserRedEnvelopeRecordVo> getHisList(PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        User currentUser = userService.getInfo();
        LambdaQueryWrapper<UserRedEnvelopeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRedEnvelopeRecord::getUid, currentUser.getUid());
        queryWrapper.ne(UserRedEnvelopeRecord::getStatus, 0);
        queryWrapper.orderByDesc(UserRedEnvelopeRecord::getReceiveTime);
        List<UserRedEnvelopeRecord> redEnvelopeRecords = dao.selectList(queryWrapper);
        List<UserRedEnvelopeRecordVo> responseList = new ArrayList<>();
        for (UserRedEnvelopeRecord redEnvelopeRecord : redEnvelopeRecords) {
            UserRedEnvelopeRecordVo userRedEnvelopeRecordVo = new UserRedEnvelopeRecordVo();
            BeanUtils.copyProperties(redEnvelopeRecord, userRedEnvelopeRecordVo);
            responseList.add(userRedEnvelopeRecordVo);
        }
        return CommonPage.restPage(responseList);
    }

    @Override
    public UserRedEnvRecordPriceResponse receive(UserRedEnvelopeReceiveRequest request) {
        UserRedEnvelopeRecord userRedEnvelopeRecord = this.getById(request.getId());
        if (userRedEnvelopeRecord == null) {
            throw new CrmebException("红包不存在");
        }
        if (!userRedEnvelopeRecord.getStatus().equals(0)) {
            throw new CrmebException("红包已领取，无法重复领取");
        }
        BigDecimal redPrice = userRedEnvelopeRecord.getPrice();
        //设置为已领取
        userRedEnvelopeRecord.setStatus(1);
        userRedEnvelopeRecord.setReceiveTime(new Date());
        User currentUser = userService.getInfo();

        // userBill现金增加记录
        UserBill userBill = new UserBill();
        userBill.setUid(currentUser.getUid());
        userBill.setLinkId(userRedEnvelopeRecord.getId().toString());
        userBill.setLinkType(Constants.USER_BILL_LINK_TYPE_RED);
        userBill.setPm(1);
        userBill.setTitle("红包收益转余额");
        userBill.setCategory(Constants.USER_BILL_CATEGORY_MONEY);
        userBill.setType(Constants.USER_BILL_TYPE_RED_IN);
        userBill.setNumber(redPrice);
        userBill.setBalance(currentUser.getNowMoney().add(redPrice));
        userBill.setMark(StrUtil.format("红包收益转余额,增加{}", redPrice));
        userBill.setStatus(1);
        userBill.setCreateTime(DateUtil.date());

        Boolean execute = transactionTemplate.execute(e -> {
            //修改状态
            this.updateById(userRedEnvelopeRecord);
            // 加余额
            userService.operationNowMoney(currentUser.getUid(), redPrice, currentUser.getNowMoney(), "add");
            userBillService.save(userBill);
            return Boolean.TRUE;
        });
        if (Boolean.FALSE.equals(execute)) {
            throw new CrmebException("红包领取异常");
        }
        UserRedEnvRecordPriceResponse userRedEnvRecordPriceResponse = new UserRedEnvRecordPriceResponse();
        userRedEnvRecordPriceResponse.setId(userRedEnvelopeRecord.getId());
        userRedEnvRecordPriceResponse.setPrice(userRedEnvelopeRecord.getPrice());
        return userRedEnvRecordPriceResponse;
    }

    @Override
    public void autoCreateRedEnv() {

        //获取礼包的订单
        LambdaQueryWrapper<UserRedEnvelope> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRedEnvelope::getStatus, 1);
        List<UserRedEnvelope> userRedEnvelopes = userRedEnvelopeService.list(lambdaQueryWrapper);
        //查看用户属于单个红包还是混合红包
        Map<Integer, Set<Integer>> userRedEnvelopeTypeMap = new HashMap<>();

        //查询用户用户属性
        for (UserRedEnvelope userRedEnvelope : userRedEnvelopes) {
            if (userRedEnvelopeTypeMap.get(userRedEnvelope.getUid()) == null) {
                Set<Integer> types = new HashSet<>();
                types.add(userRedEnvelope.getGiftProperty());
                userRedEnvelopeTypeMap.put(userRedEnvelope.getUid(), types);
            } else {
                Set<Integer> types = userRedEnvelopeTypeMap.get(userRedEnvelope.getUid());
                types.add(userRedEnvelope.getGiftProperty());
            }
        }

        if (userRedEnvelopeTypeMap.isEmpty()) {
            logger.error("用户红包为空");
            throw new CrmebException("用户红包为空,无法生成");
        }
        //更新用户的红包属性
        //用户红包属性
        Map<Integer, Integer> userRedenvelopeLevelMap = new HashMap<>();
        List<User> userList = userService.listByIds(userRedEnvelopeTypeMap.keySet());
        for (User user : userList) {
            if (userRedEnvelopeTypeMap.get(user.getUid()) != null) {
                Set<Integer> types = userRedEnvelopeTypeMap.get(user.getUid());
                user.setRedEnvelopeLevel(types.size());
                user.setRedEnvelopeTypes(StringUtils.join(types, ","));
                userRedenvelopeLevelMap.put(user.getUid(), types.size());
            }
        }

        //广告
        LambdaQueryWrapper<Advertisement> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(Advertisement::getStatus, 1);
        List<Advertisement> advertisements = advertisementService.list(objectLambdaQueryWrapper);

        List<UserRedEnvelopeRecord> userRedEnvelopeRecords = new ArrayList<>();
        //当存在一类红包 按照15%
        //当存在二类红包 按照17%
        //当存在三类红包 按照19%
        for (UserRedEnvelope userRedEnvelope : userRedEnvelopes) {
            //查询红包已经领取多少金额
            //当金额超过了总额 就无法再领取
            // 动态调整金额范围
            Integer userRedenvelopeLevel = userRedenvelopeLevelMap.get(userRedEnvelope.getUid());

            // 随机生成红包金额
            BigDecimal redEnvelope = generateRedEnvelopePrice(userRedenvelopeLevel, userRedEnvelope.getGiftProperty());

            //查看余额是否足够
            if (redEnvelope.compareTo(userRedEnvelope.getSurplusAmount()) > 0) {
                redEnvelope = userRedEnvelope.getSurplusAmount();
                userRedEnvelope.setStatus(2);
            }
            // 更新剩余金额
            userRedEnvelope.setSurplusAmount(userRedEnvelope.getSurplusAmount().subtract(redEnvelope));
            //todo:查看当前是否已经生成红包
            //如果红包金额已经不足 就修改红包状态
            UserRedEnvelopeRecord userRedEnvelopeRecord = new UserRedEnvelopeRecord();
            userRedEnvelopeRecord.setUid(userRedEnvelope.getUid());
            userRedEnvelopeRecord.setRedEnvelopeId(userRedEnvelope.getId());
            int nextInt = ThreadLocalRandom.current().nextInt(advertisements.size());
            userRedEnvelopeRecord.setLinkAdId(advertisements.get(nextInt).getId());
            userRedEnvelopeRecord.setLinkAdAddr(advertisements.get(nextInt).getAttDir());
            userRedEnvelopeRecord.setPrice(redEnvelope);
            userRedEnvelopeRecord.setGiftProperty(userRedEnvelope.getGiftProperty());
            userRedEnvelopeRecord.setStatus(0);
            userRedEnvelopeRecords.add(userRedEnvelopeRecord);
        }

        Boolean execute = transactionTemplate.execute(e -> {
            //更新用户信息
            userService.updateBatchById(userList);
            //更新红包状态
            userRedEnvelopeService.updateBatchById(userRedEnvelopes);
            //新增红包记录
            this.saveBatch(userRedEnvelopeRecords);

            return Boolean.TRUE;
        });
        if (!execute) {
            logger.error("红包生成异常");
        }
    }

    /**
     * 生成红包金额
     * @param userRedenvelopeLevel 用户红包等级
     * @param giftProperty         礼包属性
     * @return 金额
     */
    public BigDecimal generateRedEnvelopePrice(Integer userRedenvelopeLevel, Integer giftProperty) {
        Double maxNumber;
        Double minNumber;
        if (userRedenvelopeLevel == null || userRedenvelopeLevel == 1) {
            OneRedEnvelopeEnum oneRedEnvelopeEnum = OneRedEnvelopeEnum.get(giftProperty);
            maxNumber = oneRedEnvelopeEnum.getMaxNumber();
            minNumber = oneRedEnvelopeEnum.getMinNumber();
        } else if (userRedenvelopeLevel == 2) {
            TwoRedEnvelopeEnum twoRedEnvelopeEnum = TwoRedEnvelopeEnum.get(giftProperty);
            maxNumber = twoRedEnvelopeEnum.getMaxNumber();
            minNumber = twoRedEnvelopeEnum.getMinNumber();
        } else if (userRedenvelopeLevel == 3) {
            ThreeRedEnvelopeEnum threeRedEnvelopeEnum = ThreeRedEnvelopeEnum.get(giftProperty);
            maxNumber = threeRedEnvelopeEnum.getMaxNumber();
            minNumber = threeRedEnvelopeEnum.getMinNumber();
        } else {
            return BigDecimal.ZERO;
        }
        return generateSecureRandomAndRoundUp(minNumber, maxNumber, 2);
    }

    /**
     * 生成指定范围内的安全随机数，保留指定小数位数并向上取整
     * @param min           最小值（包含）
     * @param max           最大值（包含），注意：由于向上取整，实际生成的数可能略微超出此值
     * @param decimalPlaces 保留的小数位数
     * @return 格式化并向上取整后的随机数字符串
     */
    private BigDecimal generateSecureRandomAndRoundUp(double min, double max, int decimalPlaces) {
        // 创建一个SecureRandom对象
        SecureRandom secureRandom = new SecureRandom();

        // 生成一个0到1之间的随机浮点数（包括0但不包括1）
        double randomValue = secureRandom.nextDouble();

        // 缩放并平移随机数以匹配所需范围，注意这里使用nextDouble()*(max-min+0.0001)来确保max能被取到
        // 但由于我们要向上取整，所以+0.0001在这里不是必需的，但为了安全起见还是保留了
        // 实际上，由于我们保留两位小数并向上取整，所以max值在四舍五入时自然会被包含进来
        double scaledValue = min + (randomValue * (max - min));

        // 使用BigDecimal进行精确的数学运算和向上取整
        BigDecimal bd = new BigDecimal(Double.toString(scaledValue));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP); // 向上取整到指定小数位数
        return bd;
    }
}
