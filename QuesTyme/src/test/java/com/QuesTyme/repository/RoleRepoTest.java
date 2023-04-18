package com.QuesTyme.repository;

import com.QuesTyme.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepo roleRepo;

    @Test
    public void testFindByName() {
        // create a new role and save it to the database
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        roleRepo.save(role);
//        entityManager.persist(role);
//        entityManager.flush();

        // call the repository method to find the role by name
        List<Role> foundRole =  roleRepo.findAll();

        // assert that the role was found and its name matches the expected value
        assertNotNull(foundRole);
        assertEquals(1, foundRole.size());
    }
}
