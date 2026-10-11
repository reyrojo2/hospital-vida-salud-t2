package pe.edu.cibertec.hospital.pagos;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class PagoConfiguracion {
   @Bean @LoadBalanced WebClient.Builder balanceadorPagos(){return WebClient.builder();}
}
