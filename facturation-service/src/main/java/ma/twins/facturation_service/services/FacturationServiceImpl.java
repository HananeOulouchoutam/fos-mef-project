package ma.twins.facturation_service.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.transaction.Transactional;
import ma.twins.facturation_service.entities.Facturation;
import ma.twins.facturation_service.exceptions.ProjectNotFoundException;
import ma.twins.facturation_service.model.FacturationRequest;
import ma.twins.facturation_service.model.Project;
import ma.twins.facturation_service.repositories.FacturationRepository;

@Transactional
@Service
public class FacturationServiceImpl implements FacturationService {

	
	    @Autowired
	    private FacturationRepository facturationRepository;
	    
	    @Autowired
	    private ProjectRestClient projectRestClient;
	    
	   
	    
	    @Override
	    public Facturation createFacturation(FacturationRequest request) {
	    	 if (request.getPhaseId() == null) {
	    	        Project projetResponse = projectRestClient.getProjetById(request.getProjectId());
	    	        if (projetResponse == null) {
	    	            throw new ProjectNotFoundException("Le projet avec l'ID " + request.getProjectId() + " n'existe pas.");
	    	        }
	    	    }

	    	    Facturation facturation = new Facturation();
	    	    facturation.setProjectId(request.getProjectId());
	    	    facturation.setPhaseId(request.getPhaseId());
	    	    facturation.setAmount(request.getAmountHT());
	    	    facturation.setTva(request.getTva());
	    	    facturation.setAmountTTC(request.getAmountHT() + request.getTva());

	    	    Facturation savedFacturation = facturationRepository.save(facturation);

	    	    // Générer le PDF avec les infos du client et projet
	    	    generateFacturationPdf(savedFacturation, request);

	    	    return savedFacturation;
	    }

		@Override
		public List<Facturation> getAllFacturations() {
			return facturationRepository.findAll();
		}

		@Override
		public Facturation getFacturationById(Long id) {
			return facturationRepository.findById(id).orElse(null);
		}

		@Override
		public void deleteFacturation(Long id) {
			facturationRepository.deleteById(id);
			
		}
		
		
		private void generateFacturationPdf(Facturation facturation, FacturationRequest request) {
		    String dest = "src/main/resources/facturations/facture_"+request.getProjectId()+"_"+request.getPhaseId()+".pdf";
		    try {
		        PdfWriter writer = new PdfWriter(dest);
		        PdfDocument pdf = new PdfDocument(writer);
		        Document doc = new Document(pdf);

		        com.itextpdf.kernel.colors.Color primary = new com.itextpdf.kernel.colors.DeviceRgb(34, 45, 50);
		        com.itextpdf.kernel.colors.Color accent = new com.itextpdf.kernel.colors.DeviceRgb(184, 32, 102); 
		        com.itextpdf.kernel.colors.Color lightGray = new com.itextpdf.kernel.colors.DeviceRgb(240, 240, 240);

		        Table header = new Table(new float[]{2, 3}).useAllAvailableWidth();
		        String logoPath = "src/main/resources/static/logo.png";
		        Image logo = new Image(ImageDataFactory.create(logoPath)).scaleToFit(80, 80);
		        header.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));

		        Paragraph info = new Paragraph()
		                .add(new Text("Twins Action Agency\n").setFontSize(12).setBold().setFontColor(primary))
		                .add("76 AV HASSN 2 VN, Fes 30000\n")
		                .add("+(212) 701-150140 | contact@twinsgroup.com");
		        header.addCell(new Cell().add(info).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
		        doc.add(header);

		        Paragraph title = new Paragraph("FACTURE")
		                .setFontSize(18)
		                .setBold()
		                .setTextAlignment(TextAlignment.CENTER)
		                .setFontColor(accent)
		                .setMarginTop(10)
		                .setMarginBottom(10);
		        doc.add(title);
		        Paragraph projectTitle = new Paragraph("Projet : " + request.getProjectTitle())
		                .setFontSize(14)
		                .setBold()
		                .setTextAlignment(TextAlignment.LEFT)
		                .setFontColor(primary)
		                .setMarginBottom(10);
		        doc.add(projectTitle);

		        Table meta = new Table(new float[]{1, 1}).useAllAvailableWidth();
		        meta.addCell(new Cell().add(new Paragraph("Facture N°: FACT-" + facturation.getId()))
		                .setBorder(Border.NO_BORDER).setFontColor(primary));
		        meta.addCell(new Cell().add(new Paragraph("Date : " + LocalDate.now()))
		                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setFontColor(primary));
		        doc.add(meta);

		        Paragraph client = new Paragraph()
		                .add(new Text("\nClient:\n").setBold())
		                .add(request.getClientName() + "\n")
		                .add(request.getClientEmail() + " | " + request.getClientPhoneNumber())
		                .setMarginBottom(10);
		        doc.add(client);

		        float[] colWidths = {40, 150, 60, 60, 70, 50, 70};
		        Table table = new Table(colWidths).useAllAvailableWidth();
		        String[] headers = {"#", "Description", "Début", "Fin", "HT", "TVA", "TTC"};

		        for (String h : headers) {
		            table.addHeaderCell(new Cell().add(new Paragraph(h).setBold().setFontColor(primary))
		                    .setBackgroundColor(lightGray).setTextAlignment(TextAlignment.CENTER));
		        }

		        table.addCell(new Cell().add(new Paragraph("1")).setTextAlignment(TextAlignment.CENTER));
		        table.addCell(new Cell().add(new Paragraph(request.getPhaseTitle())));
		        table.addCell(new Cell().add(new Paragraph(request.getStartDate().toString())).setTextAlignment(TextAlignment.CENTER));
		        table.addCell(new Cell().add(new Paragraph(request.getEndDate().toString())).setTextAlignment(TextAlignment.CENTER));
		        table.addCell(new Cell().add(new Paragraph(request.getAmountHT() + " €")).setTextAlignment(TextAlignment.RIGHT));
		        table.addCell(new Cell().add(new Paragraph(request.getTva() + " €")).setTextAlignment(TextAlignment.RIGHT));
		        table.addCell(new Cell().add(new Paragraph(facturation.getAmountTTC() + " €")).setTextAlignment(TextAlignment.RIGHT));

		        doc.add(table);

		        Table totals = new Table(new float[]{1, 1}).setHorizontalAlignment(HorizontalAlignment.RIGHT).setWidth(250);
		        totals.setMarginTop(10);

		        totals.addCell(new Cell().add(new Paragraph("Total HT :")).setBorder(Border.NO_BORDER));
		        totals.addCell(new Cell().add(new Paragraph(request.getAmountHT() + " €")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

		        totals.addCell(new Cell().add(new Paragraph("TVA :")).setBorder(Border.NO_BORDER));
		        totals.addCell(new Cell().add(new Paragraph(request.getTva() + " €")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

		        totals.addCell(new Cell().add(new Paragraph("Total TTC :").setBold().setFontColor(accent)).setBorder(Border.NO_BORDER));
		        totals.addCell(new Cell().add(new Paragraph(facturation.getAmountTTC() + " €").setBold().setFontColor(accent))
		                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

		        doc.add(totals);

		        Paragraph footer = new Paragraph("\nPaiement sous 30 jours. Merci pour votre confiance.")
		                .setFontSize(10)
		                .setTextAlignment(TextAlignment.CENTER)
		                .setFontColor(primary)
		                .setMarginTop(30);
		        doc.add(footer);

		        doc.close();
		        System.out.println("PDF généré : " + dest);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}



}
