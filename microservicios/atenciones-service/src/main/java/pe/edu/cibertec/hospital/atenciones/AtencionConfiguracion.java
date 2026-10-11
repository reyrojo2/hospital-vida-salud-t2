package pe.edu.cibertec.hospital.atenciones;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class AtencionConfiguracion {
   @Bean @LoadBalanced WebClient.Builder balanceadorAtenciones() {return WebClient.builder();}
}
