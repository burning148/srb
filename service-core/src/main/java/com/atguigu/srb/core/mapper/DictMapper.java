package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.dto.ExcelDictDto;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author wangjun
 * @since 2021-09-13
 */
public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(List<ExcelDictDto> list);
}
