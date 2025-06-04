package ma.twins.leave_service.services;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.twins.leave_service.entities.Leave;
import ma.twins.leave_service.enums.LeaveStatus;
import ma.twins.leave_service.model.Employee;
import ma.twins.leave_service.repositories.LeaveRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {
	private final LeaveRepository leaveRepository;
    private final EmployeeRestClient employeeRestClient;
    private final EmailService emailService;

    @Override
    public Page<Leave> getLeavesOrderedByStatus(Pageable pageable) {
    	Page<Leave> leaves = leaveRepository.findAllOrderByPendingFirst(LeaveStatus.PENDING, pageable);
        leaves.forEach(this::enrichLeaveWithEmployee);
        return leaves;

    }

//    @Override
//    public Page<Leave> findByStatus(LeaveStatus status, Pageable pageable) {
//    	 Page<Leave> leaves = leaveRepository.findByStatus(status, pageable);
//         leaves.forEach(this::enrichLeaveWithEmployee);
//         return leaves;
//    }

    @Override
    public Leave updateLeave(Long id, Leave updatedLeave) {
        Leave existingLeave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with id: " + id));

        existingLeave.setStartDate(updatedLeave.getStartDate());
        existingLeave.setEndDate(updatedLeave.getEndDate());
        existingLeave.setDaysRequested(updatedLeave.getDaysRequested());
        existingLeave.setType(updatedLeave.getType());
        existingLeave.setReason(updatedLeave.getReason());
        existingLeave.setDateRequested(updatedLeave.getDateRequested());
        existingLeave.setStatus(updatedLeave.getStatus());

        return leaveRepository.save(existingLeave);
    }
  
    @Override
    public Leave updateLeaveStatus(Long id, LeaveStatus status) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with id: " + id));
        leave.setStatus(status);

        // Charger l'employé
        Employee employee = employeeRestClient.customerById(leave.getEmployeeId());
        leave.setEmployee(employee);

        // Envoyer un email
        String subject = "Mise à jour de votre demande de congé";
        String content = switch (status) {
        case APPROVED -> String.format("""
                Bonjour %s,

                Nous avons le plaisir de vous informer que votre demande de congé a été approuvée. 
                Nous vous souhaitons un excellent repos.

                Bien cordialement,
                L'équipe RH
                """, employee.getFirstName());

            case DENIED -> String.format("""
                Bonjour %s,

                Après examen, nous vous informons que votre demande de congé a été malheureusement refusée.
                Pour plus de détails, nous vous invitons à contacter le service RH.

                Bien cordialement,
                L'équipe RH
                """, employee.getFirstName());

            default -> String.format("""
                Bonjour %s,

                Le statut de votre demande de congé a été mis à jour : %s.
                N'hésitez pas à consulter votre espace personnel pour plus d'informations.

                Bien cordialement,
                L'équipe RH
                """, employee.getFirstName(), status);
        };

        try {
            emailService.sendEmail(employee.getEmail(), subject, content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }

        return leaveRepository.save(leave);
    }
    
    @Override
    public Page<Leave> searchLeavesByEmployeeName(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            // Récupérer les employés correspondant au nom
            List<Employee> matchingEmployees = employeeRestClient.getEmployeeByName(name.toLowerCase(), null);
            Set<Long> matchingEmployeeIds = matchingEmployees.stream()
                    .map(Employee::getId)
                    .collect(Collectors.toSet());

            // Récupérer tous les congés (sans filtre)
            Page<Leave> leaves = leaveRepository.findAll(pageable);

            // Filtrer les congés en mémoire selon employeeId
            List<Leave> filtered = leaves.getContent().stream()
                    .filter(l -> matchingEmployeeIds.contains(l.getEmployeeId()))
                    .toList();

            return new PageImpl<>(filtered, pageable, filtered.size());
        } else {
            // Si pas de nom, retourner tous les congés paginés
            return leaveRepository.findAll(pageable);
        }
    }

    
    private void enrichLeaveWithEmployee(Leave leave) {
        try {
            Employee employee = employeeRestClient.customerById(leave.getEmployeeId());
            leave.setEmployee(employee);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'employé : " + e.getMessage());
        }
    }
 


   

}
