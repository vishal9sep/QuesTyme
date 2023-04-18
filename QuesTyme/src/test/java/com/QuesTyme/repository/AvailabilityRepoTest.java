//package com.QuesTyme.repository;
//
//import com.QuesTyme.entity.Availability;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.Assert.assertEquals;
////import static org.junit.Assert.assertThat;
//
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class AvailabilityRepoTest {
//
//    @Autowired
//    private AvailabilityRepo availabilityRepo;
//
//    @Test
//    public void testFindByUserId() {
//        // create test data
//        Availability availability1 = new Availability();
//        availability1.setUserId(1);
//        availability1.setStartTime(LocalTime.of(9, 0));
//        availability1.setEndTime(LocalTime.of(12, 0));
//        availabilityRepo.save(availability1);
//
//        Availability availability2 = new Availability();
//        availability2.setUserId(1);
//        availability2.setStartTime(LocalTime.of(13, 0));
//        availability2.setEndTime(LocalTime.of(16, 0));
//        availabilityRepo.save(availability2);
//
//        Availability availability3 = new Availability();
//        availability3.setUserId(2);
//        availability3.setStartTime(LocalTime.of(10, 0));
//        availability3.setEndTime(LocalTime.of(13, 0));
//        availabilityRepo.save(availability3);
//
//        // test finding availabilities by user ID
//        List<Availability> user1Availabilities = availabilityRepo.findByUserId(1);
//        assertThat(user1Availabilities).isNotNull();
//
//        List<Availability> user2Availabilities = availabilityRepo.findByUserId(2);
//        assertThat(user2Availabilities).isNotNull();
//
//        List<Availability> nonExistentUserAvailabilities = availabilityRepo.findByUserId(999);
//        assertThat(nonExistentUserAvailabilities).isNotNull();
//    }
//
//}
