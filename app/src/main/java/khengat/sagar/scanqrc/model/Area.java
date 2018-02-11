package khengat.sagar.scanqrc.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

public class Area implements Parcelable {
    @DatabaseField(canBeNull = true)
    private String areaName;

    @DatabaseField(canBeNull = true,generatedId = true)
    private Integer areaId;


    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(areaId);
        dest.writeString(areaName);

    }
    public static final Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel source) {
            return new Area(source);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

    public Area(Parcel in) {
        areaId = in.readInt();
        areaName = in.readString();
    }
    @Override
    public String toString() {
        return "Area{" +
                "areaName='" + areaName + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }

    public Area() {
    }
}
