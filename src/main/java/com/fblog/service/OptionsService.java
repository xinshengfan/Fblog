package com.fblog.service;

import com.fblog.core.dao.entity.Option;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.mapper.OptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionsService extends BaseService {

    @Autowired
    private OptionMapper optionsMapper;

    @Override
    protected BaseMapper getMapper() {
        return optionsMapper;
    }

    public String getOptionValue(String name) {
        return optionsMapper.getOptionValue(name);
    }

    /**
     * 以select .. for update,注意此方法须在事务中执行
     *
     * @param name
     * @return
     */
    public String getOptionValueForUpdate(String name) {
        return optionsMapper.getOptionValueForUpdate(name);
    }

    /**
     * 此处为MySQL的replace into, 注意这需要主键id一致
     *
     * @param name  .
     * @param value .
     */
    public void updateOptionValue(String name, String value) {
        Option option = new Option(name, value);
        option.setId(name);
        optionsMapper.update(option);
    }
}
