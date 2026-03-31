package org.exalt.task2;

import org.exalt.task2.service.AppConfigService;
import org.exalt.task2.service.AppointmentRequestContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeanScopeTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void prototypeBean_shouldReturnDifferentInstances() {
        AppointmentRequestContext bean1 = applicationContext.getBean(AppointmentRequestContext.class);
        AppointmentRequestContext bean2 = applicationContext.getBean(AppointmentRequestContext.class);

        assertNotSame(bean1, bean2, "Prototype beans should NOT be the same instance");
    }

    @Test
    void singletonBean_shouldReturnSameInstance() {
        AppConfigService bean1 = applicationContext.getBean(AppConfigService.class);
        AppConfigService bean2 = applicationContext.getBean(AppConfigService.class);

        assertSame(bean1, bean2, "Singleton beans SHOULD be the same instance");
    }
}