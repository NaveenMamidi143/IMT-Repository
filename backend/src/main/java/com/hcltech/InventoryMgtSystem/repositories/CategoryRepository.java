package com.hcltech.InventoryMgtSystem.repositories;

import com.hcltech.InventoryMgtSystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
