package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDtoListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDto;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author wangjun
 * @since 2021-09-13
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate redisTemplate;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDto.class, new ExcelDictDtoListener(baseMapper)).sheet().doRead();
    }

    @Override
    public List<ExcelDictDto> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
        ArrayList<ExcelDictDto> excelDictDtoList = new ArrayList<>(dictList.size());

        dictList.forEach(dict -> {
            ExcelDictDto excelDictDto = new ExcelDictDto();
            BeanUtils.copyProperties(dict, excelDictDto);
            excelDictDtoList.add(excelDictDto);
        });
        return excelDictDtoList;

    }

    @Override
    public List<Dict> listByParentId(Long parentId) {

        //查询redis中是否存在数据列表
        List<Dict> dictList = null;
        try {
            dictList = (List<Dict>) redisTemplate.opsForValue().get("sre:core:dictList:"+ parentId);
            if (dictList != null) {
                //存在则直接返回数据列表
                log.info("从redis中取值");
                return dictList;
            }
        } catch (Exception e) {
            log.error("redis服务异常：" + ExceptionUtils.getStackTrace(e));
        }

        //不存在则查询数据库
        log.info("从数据库取值");

        dictList = baseMapper.selectList(new QueryWrapper<Dict>().eq("parent_id", parentId));
        //填充hasChildren字段
        dictList.forEach(dict -> {
            //判断当前节点是否有子节点,
            Boolean hasChlidren = this.hasChlidren(dict.getId());
            dict.setHasChildren(hasChlidren);
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("sre:core:dictList:" + parentId, dictList, 5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        } catch (Exception e) {
            log.error("redis服务异常：" + ExceptionUtils.getStackTrace(e));
        }

        return dictList;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {

        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);

        if (parentDict == null) {
            return "";
        }

        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .eq("parent_id", parentDict.getId())
                .eq("value", value);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);

        if (dict == null) {
            return "";
        }

        return dict.getName();

    }

    private Boolean hasChlidren(Long id) {
        Integer result = baseMapper.selectCount(new QueryWrapper<Dict>().eq("parent_id", id));

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
}
