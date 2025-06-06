package beans;

public class OrderBean {
    private String orderCode;

    public OrderBean(){
        //empty
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getId() {
        return orderCode;
    }
}
