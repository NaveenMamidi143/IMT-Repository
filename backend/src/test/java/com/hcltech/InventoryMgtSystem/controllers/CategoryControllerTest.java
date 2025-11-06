package com.hcltech.InventoryMgtSystem.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.InventoryMgtSystem.dtos.CategoryDTO;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hcltech.InventoryMgtSystem.security.AuthFilter;

@WebMvcTest(controllers = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Response mockResponse = Response.builder()
            .status(200)
            .message("Success")
            .build();
    @Test
    void testCreateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Electronics");

        Mockito.when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }
    @Test
    void testGetAllCategories() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Mockito.when(categoryService.getCategoryById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Category");

        Mockito.when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/categories/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Mockito.when(categoryService.deleteCategory(1L)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/categories/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}