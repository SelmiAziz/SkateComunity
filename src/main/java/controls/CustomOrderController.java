package controls;

import beans.CustomSkateboardBean;
import beans.SkateboardBean;
import dao.SkateboardDao;
import dao.patternAbstractFactory.DaoFactory;

import model.*;
import model.decorator.*;

import java.util.ArrayList;
import java.util.List;

public class CustomOrderController {
    private final SkateboardDao skateboardDao = DaoFactory.getInstance().createSkateboardDao();

    public List<SkateboardBean> getSkateboardSamples(){
        List<SkateboardBean> skateboardBeanList = new ArrayList<>();
        List<Skateboard> skateboardList = skateboardDao.selectAvailableSkateboards();
        for(Skateboard skateboard: skateboardList){
            SkateboardBean skateboardBean = new SkateboardBean(skateboard.name(), skateboard.description(), skateboard.size(), skateboard.price());
            skateboardBeanList.add(skateboardBean);
        }
        return skateboardBeanList;
    }


    public SkateboardBean generateModel(CustomSkateboardBean customSkateboardBean){
        System.out.println("Il nome "+customSkateboardBean.getName());
        Skateboard skateboard = skateboardDao.selectSkateboardByName(customSkateboardBean.getName());

        //I think that doesn't work in this way
        GripTextureDecorator grip = new GripTextureDecorator(skateboard,customSkateboardBean.getGripTexture());
        NoseConcaveDecorator nose = new NoseConcaveDecorator(grip,customSkateboardBean.getConcaveNose());
        TailConcaveDecorator tail = new TailConcaveDecorator(nose, customSkateboardBean.getConcaveTail());
        WarrantyDecorator warranty = new WarrantyDecorator(tail, customSkateboardBean.getWarrantyMonths());
        ExtraPilesDecorator piles = new ExtraPilesDecorator(warranty, customSkateboardBean.getExtraPiles());


        SkateboardBean skateboardBean = new SkateboardBean();
        skateboardBean.setName( skateboard.name());
        skateboardBean.setDescription(skateboard.description());
        skateboardBean.setSize(skateboard.size());
        skateboardBean.setPrice(piles.price());
        skateboardBean.setDescription(piles.description());

        return skateboardBean;
    }


}
