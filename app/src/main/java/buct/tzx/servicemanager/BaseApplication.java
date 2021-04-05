package buct.tzx.servicemanager;

import android.app.Application;

import buct.tzx.servicemanager.Impl.AppService;
import buct.tzx.servicemanager.Interface.AppServiceImpl;
import buct.tzx.servicemanager_api.ServiceManager;

public class BaseApplication extends Application {
    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceManager.setApplication(this);
    }
}
