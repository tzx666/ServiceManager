package buct.tzx.adspecfic;

import buct.tzx.adprotool.AdService;
import buct.tzx.servicemanager_api.ServiceManager;
import buct.tzx.userprotool.UserService;
public class AdServiceImpl implements AdService {
    @Override
    public String hello() {
        UserService service = ServiceManager.getInstance().getService(UserService.class);
        return service.hello();
    }
}
