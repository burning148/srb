package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2021/9/13 22:56
 * @Description
 */
@Slf4j
@NoArgsConstructor
public class ExcelDictDtoListener extends AnalysisEventListener<ExcelDictDto> {

    private DictMapper dictMapper;

    //数据列表
    private List<ExcelDictDto> list = new ArrayList<>();

    //每隔5条记录存储一次数据
    private static  final int BATCH_COUNT = 5;

    public ExcelDictDtoListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }




    @Override
    public void invoke(ExcelDictDto data, AnalysisContext analysisContext) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //最后数据记录不足BATCH_COUNT，一次性存储完剩余的
        saveData();
        log.info("excel导入成功");
    }

    private void saveData() {

        dictMapper.insertBatch(list);

    }

}
