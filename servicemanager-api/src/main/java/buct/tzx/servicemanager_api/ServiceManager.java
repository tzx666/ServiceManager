package buct.tzx.servicemanager_api;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 一个组件注册器demo，具体原理
 * apt阶段以接口为文件名生产java文件，内含唯一变量为接口的实现类
 * servicemanager内涵一hashmap
 * 根本解决问题：通过预加载和反射机制实现模块间的面向接口编程而不是具体实现编程，变相实现了组件间通信
 *
 * @author tzx
 * @email 916196773@qq.com
 */
public class ServiceManager {
    // 服务注册中心应该只有一个，故使用单例
    private volatile static ServiceManager serviceManager;

    private Map<Class<?>, Wrapper> _map = new HashMap<>();

    private ServiceManager() {

    }

    private volatile static Application realapplication;

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            synchronized (ServiceManager.class) {
                if (serviceManager == null) {
                    serviceManager = new ServiceManager();
                }
            }
        }
        return serviceManager;
    }

    public static void setApplication(Application application) {
        realapplication = application;
    }

    public <T> T getService(Class<?> kclass) {
        if (kclass == null) return null;
        if (!kclass.isInterface()) return null;
        Wrapper wrapper = _map.get(kclass);
        if (wrapper == null) {
            IFactory factory = createFractory(kclass.getCanonicalName());
            assert factory != null;
            wrapper = new Wrapper();
            wrapper.warpperImpl = factory.CreateFactory(realapplication);
        }
        if (wrapper.warpperImpl != null) {
            _map.put(kclass, wrapper);
            return (T) wrapper.warpperImpl;
        }
        return null;
    }

    private static class Wrapper {
        volatile Object warpperImpl;

        Wrapper(Object impl) {
            warpperImpl = impl;
        }

        Wrapper() {

        }
    }

    // 根据apt生成的类动态找到对应的实现类
    public IFactory<?> createFractory(String classname) {
        // 首先拿到类通过反射拿到对应的类，新建一个对象然后返回它
        try {
            Class<?> classz = Class.forName(classname + "SFC");
            Field br = classz.getDeclaredField("FACTORYNAME");
            br.setAccessible(true);
            String factoryname = (String) br.get((Object) null);
            Log.d("TAG123", "createFractory: " + factoryname);
            // TODO debug模式下可用手动注册来兜底
            if (factoryname == null) {
                return null;
            }
            // 根据获得的类名反射拿到对应的实现类
            Class<?> faclass = Class.forName(factoryname);
            IFactory<?> instance = (IFactory<?>) faclass.newInstance();
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根据apt生成的类动态找到对应的实现类,这部也就是说，只要保证拿到的工厂类是正确的，接下来的问题只是缓存优化而已
    public IFactory<?> createFractoryFromRealClassName(String classname) {
        // 首先拿到类通过反射拿到对应的类，新建一个对象然后返回它
        try {
            Class<?> faclass = Class.forName(classname);
            IFactory<?> instance = (IFactory<?>) faclass.newInstance();
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean regeister(Class<?>Service,Class<?>ServiceImpl) throws ClassNotFoundException {
        Wrapper service=_map.get(Service);
        if(service==null){
            Class<?> faclass = Class.forName(Objects.requireNonNull(ServiceImpl.getCanonicalName()));
            try{
                Object servoceImpl=faclass.newInstance();
                service = new Wrapper();
                service.warpperImpl = servoceImpl;
                _map.put(Service,service);
                return true;
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Log.d("TAG", "already regeister");
            return false;
        }
    }
}
