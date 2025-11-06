package com.hcltech.InventoryMgtSystem.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.dtos.SupplierDTO;
import com.hcltech.InventoryMgtSystem.security.AuthFilter;
import com.hcltech.InventoryMgtSystem.services.SupplierService;
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

@WebMvcTest(controllers = SupplierController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;
    @Autowired
    private ObjectMapper objectMapper;

    private final Response mockResponse = Response.builder()
            .status(200)
            .message("Success")
            .build();

    @Test
    void testAddSupplier() throws Exception {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName("ABC Supplies");
        supplierDTO.setContactInfo("abc@example.com");

        Mockito.when(supplierService.addSupplier(any(SupplierDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/suppliers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetAllSuppliers() throws Exception {
        Mockito.when(supplierService.getAllSupplier()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/suppliers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetSupplierById() throws Exception {
        Mockito.when(supplierService.getSupplierById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testUpdateSupplier() throws Exception {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName("Updated Supplier");
        supplierDTO.setContactInfo("Updated abc@example.com");
        Mockito.when(supplierService.updateSupplier(eq(1L), any(SupplierDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/suppliers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testDeleteSupplier() throws Exception {
        Mockito.when(supplierService.deleteSupplier(1L)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/suppliers/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}