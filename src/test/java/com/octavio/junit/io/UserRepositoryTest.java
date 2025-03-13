package com.octavio.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

@DataJpaTest
public class UserRepositoryTest {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UsersRepository usersRepository;

    UserEntity userEntity = new UserEntity();

    UserEntity userEntity2 = new UserEntity();

    @BeforeEach
    void setup() {

        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Luujan");
        userEntity.setLastName("Perez");
        userEntity.setEmail("email1@email.com");
        userEntity.setEncryptedPassword("adasdfadfasdf");
        testEntityManager.persistAndFlush(userEntity);

        userEntity2.setUserId(UUID.randomUUID().toString());
        userEntity2.setFirstName("Luujan");
        userEntity2.setLastName("Perez");
        userEntity2.setEmail("email2@email.com");
        userEntity2.setEncryptedPassword("adasdfadfasdf");
        testEntityManager.persistAndFlush(userEntity2);
    }


    @Test
    void testFindBYEmail_whenGivenCorrectEmail_returnsUserEntity() {
        // Arg
        UserEntity userFindEmail =   usersRepository.findByEmail(userEntity.getEmail());

        // Assert
        Assertions.assertEquals(userEntity.getEmail(), userFindEmail.getEmail(),
                "The returns email address does not natch the expected value");
    }

    @Test
    void testFindUserId_whenGivenCorrectId_returnsUserEntity(){


        // Act
        UserEntity userFindById = usersRepository.findByUserId(userEntity.getUserId());

        // Assert
        Assertions.assertEquals(userEntity.getUserId(), userFindById.getUserId(),
                "The returns userId does not match the expected value");
    }

    @Test
    void testFindUserWithEmailEndsWitch_whenGivenEmailDomain_returnsUsersWithGivenDomain(){
        // Arrange
        UserEntity userEntity3 = new UserEntity();

        userEntity3.setUserId(UUID.randomUUID().toString());
        userEntity3.setFirstName("Pedro");
        userEntity3.setLastName("Escamoso");
        userEntity3.setEmail("email3@gemail.com");
        userEntity3.setEncryptedPassword("adasdfadfasdf");
        testEntityManager.persistAndFlush(userEntity3);

        String emailDomain = "email3@gemail.com";

        // Act
        List<UserEntity> users = usersRepository.findUsersWithEmailEndingWith(userEntity3.getEmail());

        // Assert

        Assertions.assertEquals(1, users.size(),
                "There should be only one user in the list"
        );

        Assertions.assertEquals(emailDomain, users.get(0).getEmail());
    }
}
