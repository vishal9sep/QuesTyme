package com.QuesTyme.repository;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.QuesTyme.entity.User;
import com.QuesTyme.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import javax.management.Query;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Sql(scripts = "classpath:data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = userRepository.findByEmail("testuser1@test.com");
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(101);
        assertThat(user.getEmail()).isEqualTo("testuser1@test.com");
    }

    @Test
    public void testFindByIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(101);
        ids.add(102);
        List<User> users = userRepository.findByIds(ids);
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId()).isEqualTo(101);
        assertThat(users.get(1).getId()).isEqualTo(102);
    }
}
