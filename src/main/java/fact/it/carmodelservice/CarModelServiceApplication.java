package fact.it.carmodelservice;

import fact.it.carmodelservice.model.Carmodel;
import fact.it.carmodelservice.postgresql.spi.Dao;
import fact.it.carmodelservice.postgresql.spi.NonExistentCustomerException;
import fact.it.carmodelservice.postgresql.spi.NonExistentEntityException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class CarModelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarModelServiceApplication.class, args);
    }

}
