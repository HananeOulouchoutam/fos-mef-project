package ma.twins.employee_service.entities;

import org.springframework.data.rest.core.config.Projection;

import ma.twins.employee_service.enums.Position;


@Projection(name = "leaveEmployee", types = Employee.class)
public interface EmployeeProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    Position getPosition();
    String getImageUrl();
    String getEmail();
}