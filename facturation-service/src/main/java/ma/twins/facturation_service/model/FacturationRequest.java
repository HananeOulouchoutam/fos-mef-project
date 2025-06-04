package ma.twins.facturation_service.model;

import java.time.LocalDate;

import lombok.Data;


@Data
public class FacturationRequest {
	
	private Long clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhoneNumber;
    private Long projectId;
    private String projectTitle;
    private Long phaseId;
    private String phaseTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double amountHT;
    private Double tva; 

}
