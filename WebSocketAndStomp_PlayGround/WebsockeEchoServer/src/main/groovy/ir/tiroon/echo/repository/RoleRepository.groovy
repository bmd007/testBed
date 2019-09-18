package ir.tiroon.echo.repository

import ir.tiroon.echo.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(String roleName)
}
