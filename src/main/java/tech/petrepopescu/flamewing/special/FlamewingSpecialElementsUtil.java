package tech.petrepopescu.flamewing.special;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FlamewingSpecialElementsUtil {
    private final ApplicationContext applicationContext;

    public FlamewingSpecialElementsUtil(@Autowired ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getCsrfTokenName(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getParameterName();
        }

        return "";
    }

    public String getCsrfHeaderName(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getHeaderName();
        }

        return "";
    }

    public String getCsrfTokenValue(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getToken();
        }

        return "";
    }

    public CsrfToken getCsrfToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    public <T> T getAutowiredObject(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
