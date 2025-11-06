package com.hcltech.InventoryMgtSystem.models;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryFields() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setProducts(Collections.emptyList());

        assertEquals(1L, category.getId());
        assertEquals("Electronics", category.getName());
        assertNotNull(category.getProducts());
        assertTrue(category.getProducts().isEmpty());
    }

    @Test
    void testCategoryBuilder() {
        Category category = Category.builder()
                .id(2L)
                .name("Books")
                .products(Collections.emptyList())
                .build();

        assertEquals(2L, category.getId());
        assertEquals("Books", category.getName());
        assertNotNull(category.getProducts());
    }

    @Test
    void testToString() {
        Category category = new Category();
        category.setId(3L);
        category.setName("Clothing");

        String expected = "Category{id=3, name='Clothing'}";
        assertEquals(expected, category.toString());
    }
}