package tech.petrepopescu.flamewing.views;

import tech.petrepopescu.flamewing.utils.StringUtils;
import tech.petrepopescu.flamewing.format.HtmlFormat;
import tech.petrepopescu.flamewing.views.predefined.FlamewingErrorView;
import tech.petrepopescu.flamewing.views.predefined.ViewNotFoundView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class View {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(View.class);
    private static final Map<String, Class<?>> VIEW_CLASSES = new HashMap<>();
    private static final Map<String, Method> RENDER_METHODS = new HashMap<>();

    private static final HtmlFormat VIEW_NOT_FOUND = new ViewNotFoundView();

    private View() {
    }

    public static HtmlFormat of(String name, Object... args) {
        Method renderMethod = discoverMethod(name, args.length);
        if (renderMethod == null) {
            log.error("The view {} could not be found", name);
            return VIEW_NOT_FOUND;
        }
        try {
            return (HtmlFormat) renderMethod.invoke(null, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("Error calling view " + name, e);
            return new FlamewingErrorView(e.getMessage());
        }
    }

    public static HtmlFormat ofNullableWithStringArgs(String name, List<String> args) {
        Method renderMethod = discoverMethod(name, args.size());
        if (renderMethod == null) {
            return null;
        }

        try {
            Object[] correctTypeArgs = new Object[args.size()];
            Parameter[] methodParameters = renderMethod.getParameters();
            for (int paramCount = 0; paramCount < methodParameters.length; paramCount++) {
                Parameter parameter = methodParameters[paramCount];
                String argumentString = args.get(paramCount);
                Class<?> paramClass = parameter.getType();
                Object correctTypeParam = convertToPrimitive(paramClass, argumentString);
                correctTypeArgs[paramCount] = correctTypeParam;
            }
            return (HtmlFormat) renderMethod.invoke(null, correctTypeArgs);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("Error calling view " + name, e);
            return new FlamewingErrorView(e.getMessage());
        }
    }

    private static Object convertToPrimitive(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == short.class || type == Short.class) {
            return Short.parseShort(value);
        } else if (type == byte.class || type == Byte.class) {
            return Byte.parseByte(value);
        } else if (type == char.class || type == Character.class) {
            if (value.length() == 1) {
                return value.charAt(0);
            } else {
                throw new IllegalArgumentException("Cannot convert String to char");
            }
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("Unsupported primitive type: " + type);
    }

    private static Method discoverMethod(String viewName, int numArgs) {
        Method renderMethod = RENDER_METHODS.get(viewName);
        if (renderMethod == null) {
            Method method = null;
            Class<?> viewClass = VIEW_CLASSES.get(viewName);
            if (viewClass == null) {
                try {
                    viewClass = Class.forName("views.html." + viewName);
                    VIEW_CLASSES.put(viewName, viewClass);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
            for (Method m : viewClass.getDeclaredMethods()) {
                if (m.getName().equals("render") && m.getParameterCount() == numArgs) {
                    method = m;
                    break;
                }
            }
            RENDER_METHODS.put(viewName, renderMethod);
            renderMethod = method;
        }

        return renderMethod;
    }

    public static void add(Class<?> resultedClass, String viewName) {
        VIEW_CLASSES.put(StringUtils.replace(viewName, "views.html.", ""), resultedClass);
    }

    public static void reset() {
        VIEW_CLASSES.clear();
        RENDER_METHODS.clear();
    }
}
