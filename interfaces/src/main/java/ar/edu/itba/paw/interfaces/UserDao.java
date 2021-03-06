package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.CampusPage;
import ar.edu.itba.paw.models.CampusPageRequest;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(Integer fileNumber, String name, String surname, String username, String email, String password,
                boolean isAdmin);

    boolean update(Long userId, User user);

    boolean delete(Long userId);

    Optional<Role> getRole(Long userId, Long courseId);

    Optional<User> findById(Long userId);

    Optional<User> findByUsername(String username);

    List<User> list();

    CampusPage<User> filterByCourse(Long courseId, CampusPageRequest pageRequest);

    CampusPage<User> list(CampusPageRequest pageRequest);

    Optional<byte[]> getProfileImage(Long userId);

    CampusPage<User> getStudentsByCourse(Long courseId, CampusPageRequest pageRequest);

    boolean updateProfileImage(Long userId, byte[] image);

    Optional<User> findByFileNumber(Integer fileNumber);

    Optional<User> findByEmail(String email);

    Integer getMaxFileNumber();
}
