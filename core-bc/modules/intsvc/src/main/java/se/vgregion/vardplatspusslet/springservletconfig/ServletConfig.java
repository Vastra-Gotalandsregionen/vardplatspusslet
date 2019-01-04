package se.vgregion.vardplatspusslet.springservletconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * @author Patrik Björk
 */
@Configuration
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServletConfig extends WebMvcConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> authentication;
    }

    public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();

        //Registering Hibernate5Module to support lazy objects
        mapper.registerModule(new Hibernate5Module());

        messageConverter.setObjectMapper(mapper);
        return messageConverter;

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        //Replace the JSON message converter to support lazy objects.

        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getSupportedMediaTypes().contains(MediaType.APPLICATION_JSON)) {
                iterator.remove();
            }
        }

        converters.add(jacksonMessageConverter());
    }
}
