package com.wjicloud.simpson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjicloud.simpson.domain.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
