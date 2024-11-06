package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.video.Advertisement;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.AdvertisementSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.AdvertisementVo;
import com.zbkj.common.vo.FileResultVo;
import com.zbkj.service.dao.AdvertisementDao;
import com.zbkj.service.service.AdvertisementService;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * AdvertisementServiceImpl
 * @author wtj
 * @date 2024/10/30
 */
@Service
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementDao, Advertisement> implements AdvertisementService {

    private static final Logger logger = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

    @Autowired
    private AdvertisementDao dao;

    @Autowired
    CrmebConfig crmebConfig;

    @Autowired
    UploadService uploadService;

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Override
    public PageInfo<AdvertisementVo> getList(AdvertisementSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Advertisement> adPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<Advertisement> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(request.getKeywords())) {
            lambdaQueryWrapper.like(Advertisement::getAdName, request.getKeywords());
        }
        lambdaQueryWrapper.orderByDesc(Advertisement::getCreateTime);
        List<Advertisement> advertisements = dao.selectList(lambdaQueryWrapper);
        List<AdvertisementVo> advertisementVos = new ArrayList<>();
        for (Advertisement advertisement : advertisements) {
            AdvertisementVo advertisementVo = new AdvertisementVo();
            BeanUtils.copyProperties(advertisement, advertisementVo);
            advertisementVos.add(advertisementVo);
        }
        return CommonPage.copyPageInfo(adPage, advertisementVos);
    }

    @Override
    public Boolean saveAdvertisement(MultipartFile multipartFile, String adName, String adDescription) {
        Advertisement advertisement = new Advertisement();
        try {
            String extStr = systemConfigService.getValueByKey(Constants.UPLOAD_FILE_EXT_STR_CONFIG_KEY);
            int size = Integer.parseInt(systemConfigService.getValueByKey(Constants.UPLOAD_FILE_MAX_SIZE_CONFIG_KEY));
            String type = Constants.UPLOAD_TYPE_FILE + "/";
            FileResultVo resultFile = uploadService.upload(multipartFile, Constants.UPLOAD_TYPE_MODEL_VIDEO, type, extStr, size);
            advertisement.setAdName(adName);
            advertisement.setAdDescription(adDescription);
            advertisement.setName(resultFile.getFileName());
            advertisement.setAttDir(resultFile.getUrl());
            advertisement.setAttSize(resultFile.getFileSize().toString());
            advertisement.setAttType(resultFile.getType());
            advertisement.setStatus(1);
            advertisement.setVideoType(resultFile.getUploadType());   //图片上传类型 1本地 2七牛云 3OSS 4COS, 默认本地，任务轮询数据库放入云服务
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmebException("附件上传异常:" + e.getMessage());
        }
        return this.save(advertisement);
    }

    @Override
    public Boolean updateAdvertisement(MultipartFile multipart, Integer id, String adName, String adDescription) {
        Advertisement advertisement = getById(id);
        if (ObjectUtil.isNull(advertisement)) {
            throw new CrmebException("广告不存在");
        }
        try {
            String extStr = systemConfigService.getValueByKey(Constants.UPLOAD_FILE_EXT_STR_CONFIG_KEY);
            int size = Integer.parseInt(systemConfigService.getValueByKey(Constants.UPLOAD_FILE_MAX_SIZE_CONFIG_KEY));
            String type = Constants.UPLOAD_TYPE_FILE + "/";
            FileResultVo resultFile = uploadService.upload(multipart, Constants.UPLOAD_TYPE_MODEL_VIDEO, type, extStr, size);
            advertisement.setAdName(adName);
            advertisement.setAdDescription(adDescription);
            advertisement.setName(resultFile.getFileName());
            advertisement.setAttDir(resultFile.getUrl());
            advertisement.setAttSize(resultFile.getFileSize().toString());
            advertisement.setAttType(resultFile.getType());
            advertisement.setVideoType(resultFile.getUploadType());   //图片上传类型 1本地 2七牛云 3OSS 4COS, 默认本地，任务轮询数据库放入云服务
        } catch (Exception e) {
            throw new CrmebException("附件上传异常:" + e.getMessage());
        }
        return this.updateById(advertisement);
    }

    @Override
    public Boolean deleteById(Integer id) {
        Advertisement advertisement = getById(id);
        if (ObjectUtil.isNull(advertisement)) {
            throw new CrmebException("广告已删除");
        }
        return removeById(id);
    }
}
