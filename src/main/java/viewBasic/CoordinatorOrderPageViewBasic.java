package viewBasic;

import controls.CustomOrderController;
import utils.CoordinatorOrderView;

public class CoordinatorOrderPageViewBasic implements CoordinatorOrderView {
    private CustomOrderController customOrderController;


    @Override
    public void newOrder() {

    }

    public void setController(CustomOrderController customOrderController){
        this.customOrderController = customOrderController;
    }

    public void displayOrders(){

    }
}
