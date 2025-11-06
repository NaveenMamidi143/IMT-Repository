package com.hcltech.InventoryMgtSystem.services;
import com.hcltech.InventoryMgtSystem.dtos.CategoryDTO;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.exceptions.NotFoundException;
import com.hcltech.InventoryMgtSystem.models.Category;
import com.hcltech.InventoryMgtSystem.repositories.CategoryRepository;
import com.hcltech.InventoryMgtSystem.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CategoryServiceTest {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        modelMapper = new ModelMapper();
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper);
    }

    @Test
    void testCreateCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Electronics");

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Response response = categoryService.createCategory(dto);

        assertEquals(200, response.getStatus());
        assertEquals("Category Saved Successfully", response.getMessage());
    }
    @Test
    void testGetAllCategories() {
        Category cat1 = new Category(1L, "Electronics", null);
        Category cat2 = new Category(2L, "Furniture", null);

        List<Category> categories = Arrays.asList(cat1, cat2);
        when(categoryRepository.findAll(any(Sort.class))).thenReturn(categories);

        Response response = categoryService.getAllCategories();

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getCategories());
        assertEquals(2, response.getCategories().size());
    }

    @Test
    void testGetCategoryById_Found() {
        Category category = new Category(1L, "Electronics", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Response response = categoryService.getCategoryById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getCategory());
        assertEquals("Electronics", response.getCategory().getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void testUpdateCategory_Success() {
        Category existing = new Category(1L, "Old Name", null);
        CategoryDTO updatedDTO = new CategoryDTO();
        updatedDTO.setName("New Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(existing);

        Response response = categoryService.updateCategory(1L, updatedDTO);

        assertEquals(200, response.getStatus());
        assertEquals("Category Was Successfully Updated", response.getMessage());
    }

    @Test
    void testUpdateCategory_NotFound() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("New Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.updateCategory(1L, dto));
    }

    @Test
    void testDeleteCategory_Success() {
        Category category = new Category(1L, "Electronics", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        Response response = categoryService.deleteCategory(1L);

        assertEquals(200, response.getStatus());
        assertEquals("Category Was Successfully Deleted", response.getMessage());
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}