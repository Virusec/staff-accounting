package com.example.staffaccounting;

import com.example.staffaccounting.controller.EmployeeController;
import com.example.staffaccounting.exception.GlobalExceptionHandler;
import com.example.staffaccounting.exception.ResourceNotFountException;
import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.Employee;
import com.example.staffaccounting.model.dto.EmployeeRequest;
import com.example.staffaccounting.model.projection.EmployeeProjection;
import com.example.staffaccounting.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
public class EmployeeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void createEmployee_returnsCreated() throws Exception {
        Employee created = new Employee(
                1L,
                "John",
                "Doe",
                "Dev",
                new BigDecimal("5000.0"),
                new Department(1L, "IT", List.of()));
        when(employeeService.create(any(EmployeeRequest.class))).thenReturn(created);

        String json = "{" +
                "\"firstName\":\"John\"," +
                "\"lastName\":\"Doe\"," +
                "\"position\":\"Dev\"," +
                "\"salary\":5000.0," +
                "\"departmentId\":1" +
                "}";

        mockMvc.perform(post("/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.position").value("Dev"));
    }

    @Test
    void updateEmployee_returnsOk() throws Exception {
        Employee updated = new Employee(
                2L,
                "Jane",
                "Smith",
                "QA",
                new BigDecimal("4500.0"),
                new Department(2L, "HR", List.of()));
        when(employeeService.update(eq(2L), any(EmployeeRequest.class))).thenReturn(updated);

        String json = "{" +
                "\"firstName\":\"Jane\"," +
                "\"lastName\":\"Smith\"," +
                "\"position\":\"QA\"," +
                "\"salary\":4500.0," +
                "\"departmentId\":2" +
                "}";

        mockMvc.perform(put("/employee/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void deleteEmployee_returnsNoContent() throws Exception {
        doNothing().when(employeeService).delete(3L);

        mockMvc.perform(delete("/employee/delete/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findEmployeeById_returnsOk() throws Exception {
        Employee emp = new Employee(
                4L,
                "Alice",
                "Brown",
                "PM",
                new BigDecimal("6000.0"),
                new Department(3L, "Finance", List.of()));
        when(employeeService.findEmployeeByIdOrThrow(4L)).thenReturn(emp);

        mockMvc.perform(get("/employee/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.department.id").value(3));
    }

    @Test
    void findAllProjections_returnsOk() throws Exception {
        EmployeeProjection proj = new EmployeeProjection() {
            @Override public String getFullName() { return "John Doe"; }
            @Override public String getPosition() { return "Dev"; }
            @Override public String getDepartment() { return "IT"; }
        };
        when(employeeService.findAllProjections()).thenReturn(List.of(proj));

        mockMvc.perform(get("/employee/projection/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[0].position").value("Dev"));
    }

    @Test
    void findProjectionsByLastName_returnsOk() throws Exception {
        String lastName = "Smith";
        EmployeeProjection proj = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Jane Smith";
            }

            @Override
            public String getPosition() {
                return "QA";
            }

            @Override
            public String getDepartment() {
                return "HR";
            }
        };
        when(employeeService.findByLastName(lastName)).thenReturn(List.of(proj));

        mockMvc.perform(get("/employee/projection/lastName/Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Jane Smith"));
    }

    @Test
    void findEmployeeProjectionById_returnsOk() throws Exception {
        EmployeeProjection proj = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Bob Taylor";
            }

            @Override
            public String getPosition() {
                return "CTO";
            }

            @Override
            public String getDepartment() {
                return "Exec";
            }
        };
        when(employeeService.findEmployeeProjectionsById(5L)).thenReturn(proj);

        mockMvc.perform(get("/employee/projection/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Bob Taylor"));
    }

    @Test
    void findEmployeeById_notFound() throws Exception {
        when(employeeService.findEmployeeByIdOrThrow(6L))
                .thenThrow(new ResourceNotFountException("Employee", "id", 6L));

        mockMvc.perform(get("/employee/6"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Employee not found with id : '6'"));
    }

    @Test
    void findEmployeeProjectionById_notFound() throws Exception {
        when(employeeService.findEmployeeProjectionsById(7L))
                .thenThrow(new ResourceNotFountException("EmployeeProjection", "id", 7L));

        mockMvc.perform(get("/employee/projection/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("EmployeeProjection not found with id : '7'"));
    }
}
