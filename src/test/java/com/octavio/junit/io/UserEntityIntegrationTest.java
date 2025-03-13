package com.octavio.junit.io;


import org.junit.jupiter.api.*;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    UserEntity userEntity = new UserEntity();

    @BeforeEach
    void setup() {

        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Luujan");
        userEntity.setLastName("Perez");
        userEntity.setEmail("email@email.com");
        userEntity.setEncryptedPassword("adasdfadfasdf");
    }

    @Test
    @Order(1)
    void testUserEntity_whenValidUserDetailsProvider_shouldReturnsStorageUserData() {
        // Act

        UserEntity storeUserEntity = testEntityManager.persistAndFlush(userEntity);


        // Assert
        Assertions.assertTrue(storeUserEntity.getId() > 0);

        Assertions.assertEquals(userEntity.getUserId(), storeUserEntity.getUserId());
        Assertions.assertEquals(userEntity.getFirstName(), storeUserEntity.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), storeUserEntity.getLastName());
        Assertions.assertEquals(userEntity.getEmail(), storeUserEntity.getEmail());
        Assertions.assertEquals(userEntity.getEncryptedPassword(), storeUserEntity.getEncryptedPassword());

    }


    @Test
    @Order(2)
    void testUserEntity_whenFirstNameIsTooLong_shouldThrowException() {

        // Arrange
        userEntity.setFirstName("012345678901234567890123456789012345678901234567890123456789");

        // Assert & Act
        Assertions.assertThrows(PersistenceException.class, () -> {
            testEntityManager.persistAndFlush(userEntity);
        }, "Was expecting a PersistenceException to be thrown");
    }


    @Test
    @Order(3)
    void testUserEntity_whenExistingUserEntity_shouldReturnsThrowException() {

        UserEntity userEntityUpdate =new UserEntity();
        userEntityUpdate.setUserId(UUID.randomUUID().toString());
        userEntityUpdate.setFirstName("Luujan");
        userEntityUpdate.setLastName("Perez");
        userEntityUpdate.setEmail("email@email.com");
        userEntityUpdate.setEncryptedPassword("adasdfadfasdf");
        userEntityUpdate.setUserId("1");


        testEntityManager.persistAndFlush(userEntityUpdate);

        userEntity.setUserId("1");

        Assertions.assertThrows(PersistenceException.class, () -> {

            testEntityManager.persistAndFlush(userEntity);
        }, "Was expecting a PersistenceException to be thrown");
    }
}
