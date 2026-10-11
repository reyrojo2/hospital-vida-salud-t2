package pe.edu.cibertec.hospital.auditoria;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RegistroAuditoriaConfiguracion {
    @Bean TopicExchange exchange(){return new TopicExchange("hospital.eventos",true,false);}
    @Bean Queue cola() {return QueueBuilder.durable("auditoria.citas").build();}
    @Bean Binding binding(Queue cola,TopicExchange exchange){return BindingBuilder.bind(cola).to(exchange).with("cita.*");}
    @Bean MessageConverter conversor(){return new Jackson2JsonMessageConverter();}
}
