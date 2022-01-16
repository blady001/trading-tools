package com.dbrz.trading;

import com.dbrz.trading.alert.AlertHelper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(AlertHelper.class)
public abstract class TestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private List<JpaRepository<?, ?>> jpaRepositories;

    @BeforeEach
    void clearDatabase() {
        jpaRepositories.forEach(JpaRepository::deleteAllInBatch);
    }
}
