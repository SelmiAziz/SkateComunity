package model;

public class DeliveryDestination {
    private Region region;
    private String province;
    private String city;
    private String streetAddress;

    public DeliveryDestination(Region region, String province, String city, String streetAddress){
        this.region = region;
        this.province = province;
        this.city = city;
        this.streetAddress = streetAddress;
    }

    public int estimatedDeliveryDays(){
        return region.getEstimatedDays();
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public Region getRegion() {
        return region;
    }
}
