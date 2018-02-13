package khengat.sagar.scanqrc.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

public class History implements Parcelable {
    @DatabaseField(canBeNull = true)

    private String productName;
    @DatabaseField(canBeNull = true)

    private String productDescription;
    @DatabaseField(foreign = true)
    private Store store;

    @DatabaseField(canBeNull = true)

    private String productCartId;
    @DatabaseField(canBeNull = true)

    private String productUnit;


    @DatabaseField(canBeNull = true,generatedId = true)

    private Integer productId;


    @DatabaseField(canBeNull = true)

    private String productSize;
    @DatabaseField(canBeNull = true)

    private String productBrand;

    @DatabaseField(canBeNull = true)


    private double productTotalPrice;
    @DatabaseField(canBeNull = true)

    private int productQuantity;



    public double getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(double productToatalPrice) {
        this.productTotalPrice = productToatalPrice;
    }


    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductCartId() {
        return productCartId;
    }

    public void setProductCartId(String productCartId) {
        this.productCartId = productCartId;
    }
    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }




    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeInt(productQuantity);
        dest.writeString(productSize);
        dest.writeDouble(productTotalPrice);
        dest.writeString(productName);
        dest.writeString(productBrand);
        dest.writeString(productDescription);
        dest.writeString(productUnit);
        dest.writeString(productCartId);
        dest.writeValue(store);
    }
    public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
    public History(Parcel in) {
        productId = in.readInt();
        productName = in.readString();
        store = (Store) in.readValue(Product.class.getClassLoader());
        productBrand =in.readString();
        productCartId = in.readString();
        productDescription = in.readString();
        productQuantity = in.readInt();
        productSize = in.readString();
        productUnit = in.readString();
        productTotalPrice = in.readDouble();
    }

    public History() {
    }

    @Override
    public String toString() {
        return "History{" +
                "productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", store=" + store +
                ", productCartId='" + productCartId + '\'' +
                ", productUnit='" + productUnit + '\'' +
                ", productId=" + productId +
                ", productSize='" + productSize + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productTotalPrice=" + productTotalPrice +
                ", productQuantity=" + productQuantity +
                '}';
    }
}