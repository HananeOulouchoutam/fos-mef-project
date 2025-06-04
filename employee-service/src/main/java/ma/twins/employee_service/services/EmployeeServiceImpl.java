package ma.twins.employee_service.services;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dtos.EmployeeRequest;
import dtos.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import ma.twins.employee_service.entities.Employee;
import ma.twins.employee_service.enums.Position;
import ma.twins.employee_service.repositories.EmployeeRepository;
import mappers.EmployeeMapper;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    private final EmployeeRepository empRepo;

    @Override
    public EmployeeResponse addEmployee(EmployeeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Employee request cannot be null");
        }

        Employee employee = Employee.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .cnss(request.getCnss())
            .phoneNumber(request.getPhoneNumber())
            .position(request.getPosition())
            .createdAt(LocalDate.now())
            .status(ma.twins.employee_service.enums.AccountStatus.ACTIVE) 
            .usedVacationDays(0)
            .vacationDaysPerYear(20)
            .build();

        log.info("Saving new employee: {}", employee);
        Employee saved = empRepo.save(employee);
        return EmployeeMapper.mapToResponse(saved);
    }

    @Override
    public Page<EmployeeResponse> getEmployees(Pageable pageable ){
        log.info("Fetching paginated employees: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return empRepo.findAll(pageable)
                .map(EmployeeMapper::mapToResponse);
    }

    @Override
    public List<EmployeeResponse> searchEmployees(Position position, String keyword) {
        log.info("Searching empaloyees with position={} or name containing={}", position, keyword);
        return empRepo
            .findByPositionOrFirstNameLikeOrLastNameLike(position, keyword, keyword)
            .stream()
            .map(EmployeeMapper::mapToResponse)
            .collect(Collectors.toList());
    }

	@Override
	public long countEmployees() {
		return empRepo.count();
	}

 
   
}