package fact.it.carmodelservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.carmodelservice.model.Carmodel;
import fact.it.carmodelservice.postgresql.spi.Dao;
import fact.it.carmodelservice.postgresql.spi.NonExistentCustomerException;
import fact.it.carmodelservice.postgresql.spi.NonExistentEntityException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ModelControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private static final Dao<Carmodel, Integer> modelDAO = new PostgreSqlDao();

    private static final Logger LOGGER =
            Logger.getLogger(CarModelServiceApplication.class.getName());
    private static final Dao<Carmodel, Integer> MODEL_DAO = new PostgreSqlDao();

    @BeforeEach
    public void beforeEachTest() {
        modelDAO.deleteAll();
    }

    @AfterEach
    public void afterEachTest() {

        modelDAO.deleteAll();
    }

    @Test
    public void nonexistTest(){
        // Test whether an exception is thrown when
        // the database is queried for a non-existent model.
        // But, if the model does exist, the details will be printed
        // on the console
        try {
            Carmodel model = getModel(1);
        } catch (NonExistentEntityException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
    }

    @Test
    public void testCRUD(){
        // Test whether a model can be added to the database
        Carmodel firstModel =
                new Carmodel(1,"1","1965","small car", "Vroominator5000", "Beetle" );
        Carmodel secondModel =
                new Carmodel(2,"1","1968","4x4", "Vroominator3000", "Truckster" );
        Carmodel thirdModel =
                new Carmodel(3,"1","1975","limo", "Vroominator8000", "Limon" );
        addModel(firstModel);
        addModel(secondModel);
        addModel(thirdModel);

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
    public static Carmodel getModel(int id) throws NonExistentEntityException {
        Optional<Carmodel> model = MODEL_DAO.get(id);
        return model.orElseThrow(NonExistentCustomerException::new);
    }

    public static Collection<Carmodel> getAllModels() {
        return MODEL_DAO.getAll();
    }

    public static void updateModel(Carmodel model) {
        MODEL_DAO.update(model);
    }

    public static Optional<Integer> addModel(Carmodel model) {
        return MODEL_DAO.save(model);
    }

    public static void deleteModel(Carmodel model) {
        MODEL_DAO.delete(model);
    }

}
