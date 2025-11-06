package com.hcltech.InventoryMgtSystem.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.dtos.TransactionRequest;
import com.hcltech.InventoryMgtSystem.enums.TransactionStatus;
import com.hcltech.InventoryMgtSystem.security.AuthFilter;
import com.hcltech.InventoryMgtSystem.services.TransactionService;
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

@WebMvcTest(controllers = TransactionController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    @Autowired
    private ObjectMapper objectMapper;
    private final Response mockResponse = Response.builder()
            .status(200)
            .message("Success")
            .build();

    @Test
    void testPurchaseInventory() throws Exception {
        TransactionRequest request = new TransactionRequest();
        // Set required fields in request if needed

        Mockito.when(transactionService.purchase(any(TransactionRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/transactions/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testMakeSale() throws Exception {
        TransactionRequest request = new TransactionRequest();

        Mockito.when(transactionService.sell(any(TransactionRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testReturnToSupplier() throws Exception {
        TransactionRequest request = new TransactionRequest();

        Mockito.when(transactionService.returnToSupplier(any(TransactionRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/transactions/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testGetAllTransactions() throws Exception {
        Mockito.when(transactionService.getAllTransactions(0, 1000, null)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/transactions/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testGetTransactionById() throws Exception {
        Mockito.when(transactionService.getAllTransactionById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testGetTransactionByMonthAndYear() throws Exception {
        Mockito.when(transactionService.getAllTransactionByMonthAndYear(10, 2025)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/transactions/by-month-year")
                        .param("month", "10")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testUpdateTransactionStatus() throws Exception {
        TransactionStatus status = TransactionStatus.COMPLETED;

        Mockito.when(transactionService.updateTransactionStatus(eq(1L), eq(status))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }
}