package com.QuesTyme.repository;

import com.QuesTyme.entity.Slot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SlotRepoTest {

    @Autowired
    private SlotRepo slotRepo;

    @Test
    public void testFindByLocalDate() {
        LocalDate date = LocalDate.of(2023, 4, 15);
        List<Slot> slots = slotRepo.findByLocalDate(date);
        assertEquals(0, slots.size());
    }

    @Test
    public void testFindBydateStatusAdminId() {
        LocalDate date = LocalDate.of(2023, 4, 17);
        Integer adminId = 1;
        List<Slot> slots = slotRepo.FindBydateStatusAdminId(date, adminId);
        assertEquals(0, slots.size());
    }

    @Test
    public void testFindBydateStatusBAdminId() {
        Integer adminId = 1;
        List<Slot> slots = slotRepo.FindBydateStatusBAdminId(adminId);
        assertEquals(0, slots.size());
    }

    @Test
    public void testFindByAdminId() {
        Integer adminId = 1;
        List<Slot> slots = slotRepo.FindByAdminId(adminId);
        assertEquals(0, slots.size());
    }

    @Test
    public void testFindAllDistinctDates() {
        Integer adminId = 2;
        List<LocalDate> dates = slotRepo.findAllDistinctDates(adminId);
        assertEquals(0, dates.size());
    }

    @Test
    public void testFindBydateAndAdminId() {
        LocalDate date = LocalDate.of(2023, 4, 18);
        Integer adminId = 1;
        List<Slot> slots = slotRepo.FindBydateAndAdminId(date, adminId);
        assertEquals(0, slots.size());
    }

    @Test
    public void testFindAllDistincttypes() {
        List<String> types = slotRepo.findAllDistincttypes();
        assertEquals(0, types.size());
    }

    @Test
    public void testFindAllAdminByType() {
        String type = "meeting";
        List<Integer> adminIds = slotRepo.findAllAdminByType(type);
        assertEquals(0, adminIds.size());
    }

//    @Test
//    public void testFindNumberOfSlotsByAdminIdAndDate() {
//        Integer adminId = 1;
//        List<HashMap<LocalDate, Integer>> results = slotRepo.FindNumberOfSlotsByAdminIdAndDate(adminId);
//        assertEquals(0, results.size());
//    }
}
