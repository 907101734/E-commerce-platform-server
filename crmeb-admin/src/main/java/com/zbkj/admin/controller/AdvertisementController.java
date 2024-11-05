package com.zbkj.admin.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.AdvertisementSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.vo.AdvertisementVo;
import com.zbkj.service.service.AdvertisementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * AdvertisementController
 *
 * @author wtj
 * @date 2024/10/30
 */
@Slf4j
@RestController
@RequestMapping("api/admin/ad")
@Api(tags = "广告管理")
public class AdvertisementController {

    @Autowired
    AdvertisementService advertisementService;

    /**
     * 分页显示广告管理表
     *
     * @param request          ArticleSearchRequest 搜索条件
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('admin:ad:list')")
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiImplicitParam(name = "keywords", value = "搜索关键字")
    public CommonResult<CommonPage<AdvertisementVo>> getList(@Validated AdvertisementSearchRequest request,
                                                             @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(advertisementService.getList(request, pageParamRequest)));
    }

    /**
     * 新增广告管理表
     *
     * @param multipart
     * @param adName
     * @param adDescription
     * @return
     */
    @PreAuthorize("hasAuthority('admin:ad:save')")
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adName", value = "广告名称"),
            @ApiImplicitParam(name = "adDescription", value = "广告描述")
    })
    public CommonResult<String> save(MultipartFile multipart,
                                     @RequestParam(value = "adName") String adName,
                                     @RequestParam(value = "adDescription") String adDescription) {
        if (this.advertisementService.saveAdvertisement(multipart, adName, adDescription)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 新增广告管理表
     *
     * @param multipart
     * @param adName
     * @param adDescription
     * @return
     */
    @PreAuthorize("hasAuthority('admin:ad:update')")
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "广告id"),
            @ApiImplicitParam(name = "adName", value = "广告名称"),
            @ApiImplicitParam(name = "adDescription", value = "广告描述")
    })
    public CommonResult<String> update(MultipartFile multipart,
                                       @RequestParam(value = "id") Integer id,
                                       @RequestParam(value = "adName") String adName,
                                       @RequestParam(value = "adDescription") String adDescription) {
        if (this.advertisementService.updateAdvertisement(multipart,id, adName, adDescription)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 删除广告管理表
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:ad:delete')")
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="文章ID")
    public CommonResult<String> delete(@RequestParam(value = "id") Integer id) {
        if (this.advertisementService.deleteById(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

}
