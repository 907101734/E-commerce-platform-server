package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.user.UserRedEnvelopeRecord;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRedEnvelopeRecordRequest;
import com.zbkj.common.vo.UserRedEnvelopeRecordVo;

import java.util.List;

/**
 * UserRedEnvelopeRecordService
 * @author wtj
 * @date 2024/10/31
 */
public interface UserRedEnvelopeRecordService extends IService<UserRedEnvelopeRecord> {

    /**
     * 查询红包收益情况
     * @param request
     * @param pageParamRequest
     * @return
     */
    List<UserRedEnvelopeRecordVo> findPageLList(UserRedEnvelopeRecordRequest request, PageParamRequest pageParamRequest);
}
