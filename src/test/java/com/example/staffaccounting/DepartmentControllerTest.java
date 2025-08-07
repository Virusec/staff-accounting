package com.example.staffaccounting;

import com.example.staffaccounting.controller.DepartmentController;
import com.example.staffaccounting.exception.GlobalExceptionHandler;
import com.example.staffaccounting.exception.ResourceNotFountException;
import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.dto.DepartmentRequest;
import com.example.staffaccounting.model.projection.DepartmentProjection;
import com.example.staffaccounting.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Anatoliy Shikin
 */
public class DepartmentControllerTest {
    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createDepartment_returnsCreated() throws Exception {
        Department created = new Department(1L, "IT", List.of());
        when(departmentService.createDepartment(any(DepartmentRequest.class))).thenReturn(created);

        String json = "{\"name\":\"IT\"}";

        mockMvc.perform(post("/departments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void updateDepartment_returnsOk() throws Exception {
        Department updated = new Department(2L, "HR", List.of());
        when(departmentService.updateDepartment(eq(2L), any(DepartmentRequest.class))).thenReturn(updated);

        String json = "{\"name\":\"HR\"}";

        mockMvc.perform(put("/departments/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void deleteDepartment_returnsNoContent() throws Exception {
        doNothing().when(departmentService).deleteDepartment(3L);

        mockMvc.perform(delete("/departments/delete/3"))
                .andExpect(status().isNoContent());

        verify(departmentService).deleteDepartment(3L);
    }

    @Test
    void findDepartmentById_returnsOk() throws Exception {
        Department dept = new Department(4L, "Finance", List.of());
        when(departmentService.findDepartmentByIdOrThrow(4L)).thenReturn(dept);

        mockMvc.perform(get("/departments/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Finance"));
    }

    @Test
    void findAllDepartmentProjections_returnsOk() throws Exception {
        DepartmentProjection proj = new DepartmentProjection() {
            @Override
            public String getDepartmentName() {
                return "Sales";
            }
        };
        when(departmentService.findAllDepartmentProjections()).thenReturn(List.of(proj));

        mockMvc.perform(get("/departments/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departmentName").value("Sales"));
    }

    @Test
    void findDepartmentById_notFound() throws Exception {
        when(departmentService.findDepartmentByIdOrThrow(5L))
                .thenThrow(new ResourceNotFountException("Department", "id", 5L));

        mockMvc.perform(get("/departments/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Department not found with id : '5'"));
    }
}
