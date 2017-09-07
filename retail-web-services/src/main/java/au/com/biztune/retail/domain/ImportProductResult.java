package au.com.biztune.retail.domain;

/**
 * Created by arash on 7/09/2017.
 */
public class ImportProductResult {
    private Product importedProduct;
    private Supplier importedSupplier;
    private UnitOfMeasure unitOfMeasure;

    public Product getImportedProduct() {
        return importedProduct;
    }

    public void setImportedProduct(Product importedProduct) {
        this.importedProduct = importedProduct;
    }

    public Supplier getImportedSupplier() {
        return importedSupplier;
    }

    public void setImportedSupplier(Supplier importedSupplier) {
        this.importedSupplier = importedSupplier;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}
