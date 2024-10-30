package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.system.SystemAttachment;
import com.zbkj.common.model.video.Advertisement;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.AdvertisementSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.UploadUtil;
import com.zbkj.common.vo.AdvertisementVo;
import com.zbkj.common.vo.CloudVo;
import com.zbkj.common.vo.FileResultVo;
import com.zbkj.common.vo.UploadCommonVo;
import com.zbkj.service.dao.AdvertisementDao;
import com.zbkj.service.dao.ArticleDao;
import com.zbkj.service.service.AdvertisementService;
import com.zbkj.service.service.ArticleService;
import com.zbkj.service.service.OssService;
import com.zbkj.service.service.SystemConfigService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private SystemConfigService systemConfigService;

    @Autowired
    private OssService ossService;

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
    public void saveAdvertisement(MultipartFile multipartFile, String adName, String adDescription) throws IOException {
        
    }
}
