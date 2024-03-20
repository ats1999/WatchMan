package com.watchman.etl.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class SingletonTemplate {
  private static volatile Object instance;

  protected SingletonTemplate() {
    // protected constructor to prevent instantiation from outside
  }

  public static <T> T getInstance(Class<T> clazz, Object... constructorArgs) {
    if (instance == null) {
      synchronized (clazz) {
        if (instance == null) {
          try {
            instance = createInstance(clazz, constructorArgs);
          } catch (InstantiationException
              | IllegalAccessException
              | InvocationTargetException
              | NoSuchMethodException e) {
            e.printStackTrace();
          }
        }
      }
    }

    return (T) instance;
  }

  private static <T> T createInstance(Class<T> clazz, Object[] constructorArgs)
      throws InstantiationException,
          IllegalAccessException,
          InvocationTargetException,
          NoSuchMethodException {
    if (constructorArgs == null || constructorArgs.length == 0) {
      return clazz.newInstance();
    } else {
      Class<?>[] parameterTypes =
          Arrays.stream(constructorArgs).map(Object::getClass).toArray(Class<?>[]::new);
      Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
      return constructor.newInstance(constructorArgs);
    }
  }
}
