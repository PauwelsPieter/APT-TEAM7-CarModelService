package fact.it.carmodelservice;

import fact.it.carmodelservice.model.carmodel;
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

    private static final Logger LOGGER =
            Logger.getLogger(CarModelServiceApplication.class.getName());
    private static final Dao<carmodel, Integer> CUSTOMER_DAO = new PostgreSqlDao();

    public static void main(String[] args) {
        // Test whether an exception is thrown when
        // the database is queried for a non-existent model.
        // But, if the model does exist, the details will be printed
        // on the console
        try {
            carmodel model = getModel(1);
        } catch (NonExistentEntityException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }

        // Test whether a model can be added to the database
        carmodel firstModel =
                new carmodel(1,1,"1965","small car", "Vroominator5000", "Beetle" );
        carmodel secondModel =
                new carmodel(2,1,"1968","4x4", "Vroominator3000", "Truckster" );
        carmodel thirdModel =
                new carmodel(3,1,"1975","limo", "Vroominator8000", "Limon" );
        addModel(firstModel).ifPresent(firstModel::setId);
        addModel(secondModel).ifPresent(secondModel::setId);
        addModel(thirdModel).ifPresent(thirdModel::setId);

        // Test whether the new model's details can be edited
        firstModel.setEngine("Vroominator3000");
        firstModel.setYear("1986");
        firstModel.setType("oldtimer");
        updateModel(firstModel);

        // Test whether all models can be read from database
        getAllModels().forEach(System.out::println);

        // Test whether a model can be deleted
        deleteModel(firstModel);
        deleteModel(secondModel);
        deleteModel(thirdModel);
    }

    // Static helper methods referenced above
    public static carmodel getModel(int id) throws NonExistentEntityException {
        Optional<carmodel> model = CUSTOMER_DAO.get(id);
        return model.orElseThrow(NonExistentCustomerException::new);
    }

    public static Collection<carmodel> getAllModels() {
        return CUSTOMER_DAO.getAll();
    }

    public static void updateModel(carmodel model) {
        CUSTOMER_DAO.update(model);
    }

    public static Optional<Integer> addModel(carmodel model) {
        return CUSTOMER_DAO.save(model);
    }

    public static void deleteModel(carmodel model) {
        CUSTOMER_DAO.delete(model);
    }
}
