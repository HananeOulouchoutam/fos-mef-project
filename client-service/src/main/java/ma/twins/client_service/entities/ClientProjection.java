package ma.twins.client_service.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "clientProjection" , types=Client.class)
public interface ClientProjection {
	Long getId();
	String getFirstName();
	String getLastName();
	String getImageUrl();

}
