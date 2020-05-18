package tmpapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tmpapp.model.Image;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

@SpringBootApplication
public class tmpapp {
    public static void main(String[] args) {
        SpringApplication.run(tmpapp.class,args);
    }
}
