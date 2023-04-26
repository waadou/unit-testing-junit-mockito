package com.waadsoft.testing.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class BaseTestCase {
    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {//Has to be called to initialize annotated objects
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}
