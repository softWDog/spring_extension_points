# Spring IOC的扩展点的简单应用
> 精通Spring主要看有没有掌握好Spring的那些扩展点，以及如何使用他们。
# 使用BeanPostProcessor自定义Bean  
如果您想在Spring容器完成实例化，配置和初始化bean之后实现一些自定义逻辑，则可以插入一个或多个自定义BeanPostProcessor实现。这些实现成为后置处理器。  

BeanPostProcessor接口包含两个回调方法。当实现此接口类通过容器注册为后处理器时，由Spring容器实例的Bean，Spring容器会在bean 的init方法执行前回调postProcessBeforeInitialization方法，然后会在bean初始化之后回调postProcessAfterInitialization方法。后置处理器可以对这些Bean做任何自定义操作。一些Spring Aop 的基础实现类就是通过实现BeanPostProcessor从而提供代理包装逻辑 。  

Spring容器能够自动检测任何实现了BeanPostProcessor接口的Bean.容器会自动将这些bean注册成后置处理器以便后续调用。

下面的示例演示如何在ApplicationContext中编写，注册和使用BeanPostProcessor实例（Spring AOP的实现方式就是如下）。

```java
public interface Greeting {
    void sayHello();
}
```


```
public class StudentImpl implements Greeting {
    private String name;

    @Override
    public void sayHello() {
        System.out.println("Hello World,"+this.name);
    }

    public void init(){
        this.name="student";
    }

    @Override
    public String toString() {
        return "HelloWorldImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

```
public class TeacherImpl implements Greeting {
    private String name;

    @Override
    public void sayHello() {
        System.out.println("Hello World,"+this.name);
    }

    public void init(){
        this.name="teacher";
    }

    @Override
    public String toString() {
        return "TeacherImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
```


```java
public class HelloWorldBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("BeanPostProcessor织入,Spring AOP 实现原理");
                return method.invoke(bean, args);
            }
        });
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- Root Context: defines shared resources visible to all other web components -->
	<bean id="student" class="com.gethin.extension.example1.StudentImpl" init-method="init" />

	<bean id="teacher" class="com.gethin.extension.example1.TeacherImpl" init-method="init" />

	<bean id="hellWorldBeanPostPostProcessor" class="com.gethin.extension.example1.HelloWorldBeanPostProcessor"/>
</beans>

```
执行入口：

```java
public class Main {
    public static void main(String args[]) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("example1.xml");
        Greeting student= (Greeting) applicationContext.getBean("student");
        student.sayHello();
        Greeting teacher= (Greeting) applicationContext.getBean("teacher");
        teacher.sayHello();
    }
}
```

上面程序执行的结果如下：

```
BeanPostProcessor织入,Spring AOP 实现原理
Hello World,student
BeanPostProcessor织入,Spring AOP 实现原理
Hello World,teacher
```

# 使用BeanFactoryPostProcessor自定义配置元数据    
BeanFactoryPostProcessor跟BeanPostProcessor有点相似，但是有一个很明显的区别，BeanFactoryPostProcessor 主要是作用于Bean的配置元数据。Spring IoC容器允许BeanFactoryPostProcessor读取配置元数据，并有可以在容器实例化除BeanFactoryPostProcessor实例之外的任何bean之前更改配置元数据。  

下面示例，如何通过BeanFactoryPostProcessor动态注册Bean进去。  
```Java
public class User {
private String id;
private String name;
//getter,setter省略
}
```

```
/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @Date: 2020/4/5 21:04
 * @description: BeanFactoryPostProcessor 实现自动注册User的bean上去
 */
public class HelloWorldBeanPostFactoryProcesser implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory= (DefaultListableBeanFactory) configurableListableBeanFactory;
        BeanDefinitionBuilder beanDefinitionBuilder=BeanDefinitionBuilder.genericBeanDefinition(User.class);
        beanDefinitionBuilder.addPropertyValue("id", new Integer(1));
        beanDefinitionBuilder.addPropertyValue("name", "gethin");
        defaultListableBeanFactory.registerBeanDefinition("user",beanDefinitionBuilder.getBeanDefinition());
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="helloWorldBeanPostFactoryProcesser" class="com.gethin.extension.example2.HelloWorldBeanPostFactoryProcesser"/>
</beans>
```  

程序入口，获取User并输出。

```java
/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @Date: 2020/3/15 22:13
 * @description:
 */
public class Main {
    public static void main(String args[]) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("example2.xml");
        User user= (User) applicationContext.getBean("user");
        System.out.println(user.toString());
    }
}
```

程序输出结果，如下。  

```
User{id='1', name='gethin'}
```
# 使用FactoryBean自定义实例化逻辑  
您可以为本身就是工厂的对象实现FactoryBean接口。  

FactoryBean接口是可插入Spring IoC容器的实例化逻辑的一点。
如果您有复杂的初始化代码，可以用Java更好地表达，则可以创建自己的FactoryBean，在该类中编写复杂的初始化，然后将自定义FactoryBean插入容器。  

FactoryBean接口提供了三种方法：  

- Object getObject（）：返回此工厂创建的对象的实例。
实例可以共享，具体取决于该工厂是否返回单例或原型。  
- boolean isSingleton（）：如果此FactoryBean返回单例，则返回true，否则返回false。   
- getObjectType（）：返回由getObject（）方法返回的对象类型；如果类型未知，则返回null。  

Mybatis与Spring整合，Mybatis的mapper对象也是通过FactoryBean来实例化的。下面通过简单的例子，实现这一原理。  

定义自己的Mapper注解。  

```java  
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {
}
```   

创建模拟的Mapper接口类,PersonMapper和UserMapper都是接口，没有具体实现。  

```Java
@Mapper
public interface PersonMapper {
    void getPerson();
}
```

创建MapperFatoryBean实现FatoryBean，重写3个方法实现MapperBean的特殊实例方式(通过JDK代理实例)。  

```
@Component
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    @Override
    public T getObject() throws Exception {
        return (T)Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, (proxy, method, args) -> {
            System.out.println( mapperInterface.getSimpleName() +"的代理类对象");
            return null;
        });
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
}
```  

```java
/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @Date: 2020/4/6 1:06
 * @description: 扫描包并进行Bean的注册
 */
public class MapperScanner extends ClassPathBeanDefinitionScanner {
    public MapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
        addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
    }
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> mapperBeanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder mapperBeanDefinitionHolder : mapperBeanDefinitionHolders) {
            GenericBeanDefinition mapperBeanDefinition= (GenericBeanDefinition) mapperBeanDefinitionHolder.getBeanDefinition();
            MutablePropertyValues propertyValues = mapperBeanDefinition.getPropertyValues();
            propertyValues.add("mapperInterface", mapperBeanDefinition.getBeanClassName());
            mapperBeanDefinition.setBeanClass(MapperFactoryBean.class);
        }
        return mapperBeanDefinitionHolders;
    }
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isInterface() && metadata.isIndependent());
    }
}
```   
MapperBean通过实现BeanFactoryPostProcessor，将FactoryBean注册到容器中去。  

```java
/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @Date: 2020/4/6 1:00
 * @description:
 */
public class MapperBean implements BeanFactoryPostProcessor {
    private String package2Scan;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        MapperScanner mapperScanner=new MapperScanner((BeanDefinitionRegistry) configurableListableBeanFactory);
        mapperScanner.doScan(package2Scan);
    }
    public void setPackage2Scan(String package2Scan) {
        this.package2Scan = package2Scan;
    }
}
```
程序执行入口，mapper接口方法执行。  

```java
/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @description:
 */
public class Main {
    public static void main(String args[]) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("example3.xml");
        UserMapper userMapper= applicationContext.getBean(UserMapper.class);
        userMapper.getUser(1);
        PersonMapper personMapper=applicationContext.getBean(PersonMapper.class);
        personMapper.getPerson();
    }
}
```  

程序输出，mapper接口执行成功。  

```
UserMapper的代理类对象
PersonMapper的代理类对象
```  

# 总结   

Spring IOC提供的拓展接口有以下3个，其他很多扩展也都是在此基础上进行提供。   
- BeanPostProcessor    
- BeanFactoryPostProcessor
- FactoryBean


















