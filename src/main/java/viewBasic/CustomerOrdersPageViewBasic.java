package viewBasic;

import controls.CustomOrderController;
import utils.CustomerOrderView;

public class CustomerOrdersPageViewBasic implements CustomerOrderView {
    private CustomOrderController customOrderController;

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }


    @Override
    public void orderUpdate() {

    }
}
