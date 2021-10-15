package fact.it.carmodelservice.postgresql.spi;

public class NonExistentCustomerException extends NonExistentEntityException {

    private static final long serialVersionUID = 8633588908169766368L;

    public NonExistentCustomerException() {
        super("Model does not exist");
    }
}
