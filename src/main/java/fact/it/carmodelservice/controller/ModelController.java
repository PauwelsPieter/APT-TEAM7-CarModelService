package fact.it.carmodelservice.controller;

import fact.it.carmodelservice.PostgreSqlDao;
import fact.it.carmodelservice.model.Carmodel;
import fact.it.carmodelservice.postgresql.spi.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;

@RestController
public class ModelController {
    @Autowired
    private static final Dao<Carmodel, Integer> MODEL_DAO = new PostgreSqlDao();

    @PostConstruct
    public void fillDB() {
        //create DB if needed
        MODEL_DAO.createDB();

        //clear all rows
        MODEL_DAO.deleteAll();

        //add some testmodels to the DB
        Carmodel firstModel =
                new Carmodel(1,1,"1965","small car", "Vroominator5000", "Beetle" );
        Carmodel secondModel =
                new Carmodel(2,1,"1968","4x4", "Vroominator3000", "Truckster" );
        Carmodel thirdModel =
                new Carmodel(3,1,"1975","limo", "Vroominator8000", "Limon" );
        MODEL_DAO.save(firstModel).ifPresent(firstModel::setId);
        MODEL_DAO.save(secondModel).ifPresent(secondModel::setId);
        MODEL_DAO.save(thirdModel).ifPresent(thirdModel::setId);
    }

    @GetMapping("/")
    public Collection<Carmodel> getAll() {
        return MODEL_DAO.getAll();
    }




    @PostMapping("/models")
    public Carmodel addModel(@RequestBody Carmodel newmodel) {
        MODEL_DAO.save(newmodel);
        return newmodel;
    }

    @PutMapping("/models")
    public Carmodel updateModel(@RequestBody Carmodel updatedModel) {
        Optional<Carmodel> retrievedModel = MODEL_DAO.get(updatedModel.getId());
        Carmodel currentModel = retrievedModel.get();

        currentModel.setName(updatedModel.getName());
        currentModel.setEngine(updatedModel.getEngine());
        currentModel.setType(updatedModel.getType());
        currentModel.setYear(updatedModel.getYear());
        currentModel.setBrandId(updatedModel.getBrandId());

        MODEL_DAO.update(currentModel);

        return currentModel;
    }

    @DeleteMapping("/models/{id}")
    public ResponseEntity deleteModel(@PathVariable String id) {
        Optional<Carmodel> modelToDelete = MODEL_DAO.get(Integer.parseInt(id));
        if (modelToDelete != null) {
            MODEL_DAO.delete(modelToDelete.get());
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}