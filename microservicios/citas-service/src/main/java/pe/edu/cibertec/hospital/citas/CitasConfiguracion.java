package pe.edu.cibertec.hospital.citas;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class CitasConfiguracion {
    @Bean @LoadBalanced WebClient.Builder balanceador() { return WebClient.builder(); }
    @Bean TopicExchange hospitalExchange() { return new TopicExchange("hospital.eventos", true, false); }
    @Bean MessageConverter conversor() { return new Jackson2JsonMessageConverter(); }
}
