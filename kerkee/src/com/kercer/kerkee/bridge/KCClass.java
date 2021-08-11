package com.kercer.kerkee.bridge;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zihong
 *
 */
public class KCClass
{
    private String mJSClzName;
    private Class<?> mClz;
    //the key is method name
    private HashMap<String, List<KCMethod>> mMethods = new HashMap<String, List<KCMethod>>();


    public KCClass(String aJSClzName,  Class<?> aClass)
    {
        mJSClzName = aJSClzName;
        mClz = aClass;
    }

    public static KCClass newClass(String aJSClzName,  Class<?> aClass)
    {
        return new KCClass(aJSClzName, aClass);
    }

    public Class<?> getNavClass()
    {
        return mClz;
    }

    public String getJSClz()
    {
        return mJSClzName;
    }

    public void addMethod(String aMethodName, KCArgList aArgList)
    {
 //   	seachMethod(aMethodName, aArgList);
    }

    protected Method seachMethod(String aMethodName, KCArgList aArgList)
    {
    	Method method = null;
    	List<Method> nativeMethodsList = getNativeMethods(aMethodName);
    	int count = nativeMethodsList.size();
    	for(int i = 0; i < count; ++i)
    	{
            Method nativeMethod = nativeMethodsList.get(i);
            Class<?>[] parameterTypes = nativeMethod.getParameterTypes();
    	}

    	return method;
    }

    protected List<Method> getNativeMethods(String aName)
    {
    	List<Method> list = new ArrayList<Method>();
    	Method[] nativeMethods = mClz.getMethods();
    	for(int i = 0; i < nativeMethods.length; ++i)
    	{
    		Method method = nativeMethods[i];
    		if (aName.equals(method.getName()))
    		{
    			list.add(method);
    		}
    	}

    	return list;
    }

    public List<KCMethod> getMethods(String aName)
    {
       return mMethods.get(aName);
    }


    public KCMethod getMethod(String aMethodName, Class<?>... aParameterTypes) throws NoSuchMethodException
    {
        List<KCMethod> listMethods = getMethods(aMethodName);
        if (listMethods == null)
        {
            listMethods = new ArrayList<KCMethod>();
        }

        KCMethod jsMethod = null;
        for(int i = 0; i < listMethods.size(); ++i)
        {
            KCMethod tmpMethod = listMethods.get(i);
            if (tmpMethod == null) continue;
            if (tmpMethod.isSameArgList(aParameterTypes))
            {
                jsMethod = tmpMethod;
            }
        }

        if (jsMethod == null)
        {
            Method tmp = mClz.getMethod(aMethodName, aParameterTypes);
            jsMethod = new KCMethod(this, tmp);
            listMethods.add(jsMethod);
            mMethods.put(aMethodName, listMethods);
        }
        return jsMethod;
    }

    public KCMethod getMethod(String aMethodName, KCArgList aArgList) throws NoSuchMethodException
    {
        if (aArgList == null || aMethodName == null) return null;
        Class<?>[] argListTypes = aArgList.getTypes();
        KCMethod jsMethod = getMethod(aMethodName, argListTypes);
        return jsMethod;
    }

}
