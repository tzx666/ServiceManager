package buct.tzx.userspecfic;

import android.app.Application;

import buct.tzx.servicemanager_annotaion.ServiceFactory;
import buct.tzx.servicemanager_api.IFactory;
import buct.tzx.userprotool.UserService;

@ServiceFactory(protool = "buct.tzx.userprotool.UserService")
public class UserFactory implements IFactory<UserService> {
    @Override
    public UserService CreateFactory(Application application) {
        return new UserServiceImpl();
    }
}
