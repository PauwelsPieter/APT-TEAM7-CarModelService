package fact.it.carmodelservice.model;

public class Carmodel {
    private int id;
    private int brandId;
    private String year;
    private String type;
    private String engine;
    private String name;

    public Carmodel(int id, int brandId, String year, String type, String engine, String name) {
        this.id = id;
        this.brandId = brandId;
        this.year = year;
        this.type = type;
        this.engine = engine;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
