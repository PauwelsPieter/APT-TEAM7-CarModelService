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
        Carmodel model1 = new Carmodel(1,"1","2008","Sport", "221 hp", "Honda Civic Type R" );
        Carmodel model2 = new Carmodel(2,"1","1990","Sport", "270 hp", "Honda NSX I Coupe" );
        Carmodel model3 = new Carmodel(3,"1","2005","4x4", "160 hp", "Honda CR-V" );
        Carmodel model4 = new Carmodel(3,"2","2020","Electric", "417 hp", "Tesla Model X Long Range" );
        Carmodel model5 = new Carmodel(3,"2","2020","Electric", "346 hp", "Tesla Model 3 Long Range" );
        Carmodel model6 = new Carmodel(3,"3","2021","4x4", "90 hp", "Dacia Duster " );
        MODEL_DAO.save(model1).ifPresent(model1::setId);
        MODEL_DAO.save(model2).ifPresent(model2::setId);
        MODEL_DAO.save(model3).ifPresent(model3::setId);
        MODEL_DAO.save(model4).ifPresent(model4::setId);
        MODEL_DAO.save(model5).ifPresent(model5::setId);
        MODEL_DAO.save(model6).ifPresent(model6::setId);
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