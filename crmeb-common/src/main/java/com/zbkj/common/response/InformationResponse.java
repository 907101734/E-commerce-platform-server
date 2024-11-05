package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * InformatiionResponse
 * @author wtj
 * @date 2024/11/04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "InformationResponse对象", description = "企业信息响应对象")
public class InformationResponse {

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "企业图片")
    private String image;

    @ApiModelProperty(value = "企业描述")
    private String description;

}
