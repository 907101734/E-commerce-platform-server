package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户充值记录保存对象
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2022 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRechargeSaveRequest对象", description = "用户充值记录保存对象")
public class UserRechargeSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "充值单id")
    private Integer userRechargeId;

    @NotBlank(message = "充值用户不能为空")
    @ApiModelProperty(value = "充值用户UID", required = true)
    private Integer uid;

    @ApiModelProperty(value = "充值金额", required = true)
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "999999.99")
    private BigDecimal price;

    @ApiModelProperty(value = "支付凭证截图")
    private String paymentVoucherImages;

    @ApiModelProperty(value = "客服备注信息")
    private String kfRemark;

}
