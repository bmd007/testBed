package ir.tiroon.echo.service

import ir.tiroon.echo.model.Role
import ir.tiroon.echo.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service("roleService")
class RoleServices {

    @Autowired
    RoleRepository roleRepository

    void save(Role role) { roleRepository.save(role) }

    Role get(Long id) { roleRepository.getOne(id) }

    Role getByName(String name) { roleRepository.findRoleByRoleName(name) }

    ArrayList<Role> list() { (ArrayList<Role>) roleRepository.findAll() }

    void delete(Role role) {
        roleRepository.delete(role)
        roleRepository.flush()
    }

}