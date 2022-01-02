package fact.it.carmodelservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.carmodelservice.model.Carmodel;
import fact.it.carmodelservice.postgresql.spi.Dao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ModelControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private static final Dao<Carmodel, Integer> modelDAO = new PostgreSqlDao();

    Carmodel model1 = new Carmodel(1,"1","2008","Sport", "221 hp", "Honda Civic Type R" );
    Carmodel model2 = new Carmodel(2,"1","1990","Sport", "270 hp", "Honda NSX I Coupe" );
    Carmodel model3 = new Carmodel(3,"1","2005","4x4", "160 hp", "Honda CR-V" );

    @BeforeEach
    public void beforeAllTests() {
        modelDAO.deleteAll();
        modelDAO.save(model1);
        modelDAO.save(model2);
        modelDAO.save(model3);
    }

    @AfterEach
    public void afterAllTests() {
        modelDAO.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAllTest() throws Exception {
        mockMvc.perform(get("/models"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name",is("Honda Civic Type R")))
                .andExpect(jsonPath("$[0].engine",is("221 hp")))
                .andExpect(jsonPath("$[0].year",is("2008")))
                .andExpect(jsonPath("$[1].name",is("Honda NSX I Coupe")))
                .andExpect(jsonPath("$[1].engine",is("270 hp")))
                .andExpect(jsonPath("$[1].year",is("1990")))
                .andExpect(jsonPath("$[2].name",is("Honda CR-V")))
                .andExpect(jsonPath("$[2].engine",is("160 hp")))
                .andExpect(jsonPath("$[2].year",is("2005")));
    }

    @Test
    public void getByYearTest() throws Exception {
        mockMvc.perform(get("/models/year/{year}", "2005"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("Honda CR-V")))
                .andExpect(jsonPath("$[0].engine",is("160 hp")))
                .andExpect(jsonPath("$[0].year",is("2005")));
    }

    @Test
    public void getByTypeTest() throws Exception {
        mockMvc.perform(get("/models/type/{type}", "4x4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("Honda CR-V")))
                .andExpect(jsonPath("$[0].engine",is("160 hp")))
                .andExpect(jsonPath("$[0].type",is("4x4")));
    }

    @Test
    public void postModelTest() throws Exception {
        Carmodel newModel = new Carmodel(4,"1","1995","new type", "new engine", "new name" );

        mockMvc.perform(post("/models").content(mapper.writeValueAsString(newModel)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newModel.getName())))
                .andExpect(jsonPath("$.type", is(newModel.getType())))
                .andExpect(jsonPath("$.year", is(newModel.getYear())));
    }

    @Test
    public void putModelTest() throws Exception {
        Carmodel newModel = new Carmodel(1,"1","1995","updated type", "updated engine", "updated name" );

        mockMvc.perform(put("/models").content(mapper.writeValueAsString(newModel)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newModel.getName())))
                .andExpect(jsonPath("$.type", is(newModel.getType())))
                .andExpect(jsonPath("$.year", is(newModel.getYear())));
    }

    @Test
    public void putnotfoundTest() throws Exception {
        Carmodel tempModel = new Carmodel(123,"1","1995","fake type", "fake engine", "fake name" );

        mockMvc.perform(put("/brands").content(mapper.writeValueAsString(tempModel)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/models/{id}", "2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
