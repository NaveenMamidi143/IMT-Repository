package com.hcltech.InventoryMgtSystem.services;

import com.hcltech.InventoryMgtSystem.dtos.CategoryDTO;
import com.hcltech.InventoryMgtSystem.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}
