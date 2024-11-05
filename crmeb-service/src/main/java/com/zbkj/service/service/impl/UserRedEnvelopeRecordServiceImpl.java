package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.model.user.UserRedEnvelopeRecord;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRedEnvelopeRecordRequest;
import com.zbkj.common.vo.UserRedEnvelopeRecordVo;
import com.zbkj.service.dao.UserRedEnvelopeRecordDao;
import com.zbkj.service.service.UserRedEnvelopeRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * UserRedEnvelopeRecordServiceImpl
 * @author wtj
 * @date 2024/10/31
 */
@Service
public class UserRedEnvelopeRecordServiceImpl extends ServiceImpl<UserRedEnvelopeRecordDao, UserRedEnvelopeRecord> implements UserRedEnvelopeRecordService {

    @Autowired
    UserRedEnvelopeRecordDao dao;

    @Override
    public List<UserRedEnvelopeRecordVo> findPageLList(UserRedEnvelopeRecordRequest request, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<UserRedEnvelopeRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getStatus() != null) {
            queryWrapper.eq(UserRedEnvelopeRecord::getStatus, request.getStatus());
        }
        List<UserRedEnvelopeRecord> redEnvelopeRecords = dao.selectList(queryWrapper);
        List<UserRedEnvelopeRecordVo> responseList = new ArrayList<>();
        for (UserRedEnvelopeRecord redEnvelopeRecord : redEnvelopeRecords) {
            UserRedEnvelopeRecordVo userRedEnvelopeRecordVo = new UserRedEnvelopeRecordVo();
            BeanUtils.copyProperties(redEnvelopeRecord, userRedEnvelopeRecordVo);
            responseList.add(userRedEnvelopeRecordVo);
        }
        return responseList;
    }
}
