package fact.it.carmodelservice.model;

public class model {
    private int brandId;
    private String year;
    private String type;
    private String engine;
    private String name;

    public model(int brandId, String year, String type, String engine, String name) {
        this.brandId = brandId;
        this.year = year;
        this.type = type;
        this.engine = engine;
        this.name = name;
    }
}
