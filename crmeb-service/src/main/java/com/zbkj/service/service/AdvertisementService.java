package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.video.Advertisement;
import com.zbkj.common.request.AdvertisementSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.AdvertisementVo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * AdvertisementService
 * @author wtj
 * @date 2024/10/30
 */
public interface AdvertisementService extends IService<Advertisement> {

    /**
     * 广告列表
     * @param pageParamRequest 分页类参数
     * @return PageInfo<Article>
     */
    PageInfo<AdvertisementVo> getList(AdvertisementSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 保存广告信息
     * @param multipart
     * @param adName
     * @param adDescription
     * @throws IOException
     */
    Boolean saveAdvertisement(MultipartFile multipart, String adName, String adDescription);


    /**
     * 更新广告
     * @param multipart
     * @param id
     * @param adName
     * @param adDescription
     * @return
     */
    Boolean updateAdvertisement(MultipartFile multipart,Integer id, String adName, String adDescription);

    /**
     * 删除广告
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);
}
