package buct.tzx.servicemanager_api;

import android.app.Application;

public interface IFactory<T> {
    // T 代表一个类，所有的组件protool应该继承此工厂进行调用
    T CreateFactory(Application application);
}
