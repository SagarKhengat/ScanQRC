package khengat.sagar.scanqrc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Sagar Khengat on 10/02/2018.
 */

public class Store implements Parcelable{
    @DatabaseField(canBeNull = true)
    private String storeName;
    @DatabaseField(canBeNull = true,id = true)
    private String storeId;
    @DatabaseField(foreign = true)
    private Area area;



    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(storeId);
                dest.writeString(storeName);
                dest.writeValue(area);
    }

    public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
    public Store(Parcel in) {
        storeId = in.readString();
        storeName = in.readString();
        area = (Area) in.readValue(Store.class.getClassLoader());
    }
    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", storeId='" + storeId + '\'' +
                ", area=" + area +
                '}';
    }

    public Store() {
    }
}
