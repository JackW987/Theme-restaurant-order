package com.wjicloud.simpson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjicloud.simpson.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
