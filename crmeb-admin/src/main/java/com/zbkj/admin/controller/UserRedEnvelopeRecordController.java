package com.zbkj.admin.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRedEnvelopeRecordRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.vo.UserRedEnvelopeRecordVo;
import com.zbkj.service.service.UserRedEnvelopeRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserRedEnvelopeRecordController
 * @author wtj
 * @date 2024/10/31
 */
@Slf4j
@RestController
@RequestMapping("api/admin/user/redEnvelope")
@Api(tags = "营销 -- 红包")
public class UserRedEnvelopeRecordController {

    @Autowired
    UserRedEnvelopeRecordService userRedEnvelopeRecordService;

    /**
     * 分页显示红包管理表
     * @param request          ArticleSearchRequest 搜索条件
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('admin:red:list')")
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserRedEnvelopeRecordVo>> getList(@Validated UserRedEnvelopeRecordRequest request, @Validated PageParamRequest pageParamRequest) {
        CommonPage<UserRedEnvelopeRecordVo> page = CommonPage.restPage(userRedEnvelopeRecordService.findPageLList(request, pageParamRequest));
        return CommonResult.success(page);
    }

    // @PreAuthorize("hasAuthority('admin:red:list')")
    @ApiOperation(value = "手动执行")
    @RequestMapping(value = "/handle", method = RequestMethod.GET)
    public CommonResult handle() {
        userRedEnvelopeRecordService.autoCreateRedEnv();
        return CommonResult.success();
    }
}
