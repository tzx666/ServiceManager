package buct.tzx.adspecfic;

import android.app.Application;

import buct.tzx.adprotool.AdService;
import buct.tzx.servicemanager_annotaion.ServiceFactory;
import buct.tzx.servicemanager_api.IFactory;

//@ServiceFactory(protool = "buct.tzx.adprotool.AdService")
public class AdFactory  implements IFactory<AdService> {
    @Override
    public AdServiceImpl CreateFactory(Application application) {
        return new AdServiceImpl();
    }
}
