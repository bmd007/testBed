package ir.tiroon.echo.repository

import ir.tiroon.echo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmail(String email)
    User findUserByPhoneNumber(String phoneNumber)
}
