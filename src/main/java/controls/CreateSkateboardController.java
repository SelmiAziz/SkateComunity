package controls;

import beans.SkateboardBean;
import dao.SkateboardDao;
import dao.patternAbstractFactory.DaoFactory;
import model.SkateboardBase;
import model.decorator.Skateboard;

import java.util.ArrayList;
import java.util.List;

public class CreateSkateboardController {
    private final  SkateboardDao skateboardDao = DaoFactory.getInstance().createSkateboardDao();


    public void createSkateboard(SkateboardBean skateboardBean){
        Skateboard skateboard = new SkateboardBase(skateboardBean.getName(), skateboardBean.getDescription(), skateboardBean.getSize(), skateboardBean.getPrice());
        skateboardDao.addSkateboard(skateboard);
    }


    public List<SkateboardBean> getStoredSkateboards(){
        List<SkateboardBean> skateboardBeanList = new ArrayList<>();
        List<Skateboard> skateboardList = skateboardDao.selectAvailableSkateboards();
        for(Skateboard skateboard: skateboardList){
            SkateboardBean skateboardBean = new SkateboardBean(skateboard.name(), skateboard.description(), skateboard.size(), skateboard.price());
            skateboardBeanList.add(skateboardBean);
        }
        return skateboardBeanList;
    }
}
