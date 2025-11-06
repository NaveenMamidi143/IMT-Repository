package com.hcltech.InventoryMgtSystem.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.security.AuthFilter;
import com.hcltech.InventoryMgtSystem.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = ProductController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Response mockResponse = Response.builder()
            .status(200)
            .message("Success")
            .build();

    @Test
    void testSaveProduct() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "dummy".getBytes());

        Mockito.when(productService.saveProduct(any(), any())).thenReturn(mockResponse);

        mockMvc.perform(multipart("/api/products/add")
                        .file(imageFile)
                        .param("name", "Laptop")
                        .param("sku", "LAP123")
                        .param("price", "999.99")
                        .param("stockQuantity", "10")
                        .param("categoryId", "1")
                        .param("description", "High-end laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "dummy".getBytes());

        Mockito.when(productService.updateProduct(any(), any())).thenReturn(mockResponse);

        mockMvc.perform(multipart("/api/products/update")
                        .file(imageFile)
                        .param("productId", "1")
                        .param("name", "Updated Laptop")
                        .param("sku", "LAP123")
                        .param("price", "1099.99")
                        .param("stockQuantity", "15")
                        .param("categoryId", "1")
                        .param("description", "Updated description")
                        .with(request -> {
                            request.setMethod("PUT"); // multipart defaults to POST, override to PUT
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetProductById() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.when(productService.deleteProduct(1L)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/products/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testSearchProduct() throws Exception {
        Mockito.when(productService.searchProduct("Laptop")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/products/search")
                        .param("input", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}