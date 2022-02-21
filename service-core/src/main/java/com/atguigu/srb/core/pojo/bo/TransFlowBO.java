package com.atguigu.srb.core.pojo.bo;

import com.atguigu.srb.core.enums.TransTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author wangjun
 * @Date 2021/12/7 16:48
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransFlowBO {
    private String agentBillNo;
    private String bindCode;
    private BigDecimal amount;
    private TransTypeEnum transTypeEnum;
    private String memo;
}
