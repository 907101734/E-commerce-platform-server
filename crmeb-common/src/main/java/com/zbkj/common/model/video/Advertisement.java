package com.zbkj.common.model.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * video
 * @author wtj
 * @date 2024/10/30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_advertisement")
@ApiModel(value = "Advertisement对象", description = "广告管理表")
public class Advertisement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "广告管理ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "广告名称")
    private String adName;

    @ApiModelProperty(value = "广告描述")
    private String adDescription;

    @ApiModelProperty(value = "附件名称")
    private String name;

    @ApiModelProperty(value = "附件路径")
    private String attDir;

    @ApiModelProperty(value = "附件大小")
    private String attSize;

    @ApiModelProperty(value = "附件类型")
    private String attType;

    @ApiModelProperty(value = "浏览次数")
    private Long viewCount;

    @ApiModelProperty(value = "状态：0-未上架 1已上架")
    private Integer status;

    @ApiModelProperty(value = "上传类型 1本地 2七牛云 3OSS 4COS ")
    private Integer videoType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
