package co.edu.uniandes.dse.bookstore.services;

import co.edu.uniandes.dse.bookstore.entities.UsuarioEntity;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager;

    private UsuarioEntity usuarioEntity;
    
    @BeforeEach
    void setUp() {
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNombre("Juan Pérez");
        usuarioEntity.setCorreo("juan.perez@example.com");

        entityManager.persist(usuarioEntity);
        entityManager.flush();
    }

    @Test
    void testCreateUsuario() throws IllegalOperationException {
        UsuarioEntity newUsuario = new UsuarioEntity();
        newUsuario.setNombre("Maria Gomez");
        newUsuario.setCorreo("maria.gomez@example.com");

        UsuarioEntity result = usuarioService.createUsuario(newUsuario);
        assertNotNull(result);

        UsuarioEntity entity = entityManager.find(UsuarioEntity.class, result.getId());
        assertEquals(newUsuario.getNombre(), entity.getNombre());
        assertEquals(newUsuario.getCorreo(), entity.getCorreo());
    }

    @Test
    void testCreateUsuarioWithExistingEmail() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newUsuario = new UsuarioEntity();
            newUsuario.setNombre("Carlos Sánchez");
            newUsuario.setCorreo("juan.perez@example.com"); // Correo ya existente

            usuarioService.createUsuario(newUsuario);
        });
    }

    @Test
    void testCreateUsuarioWithEmptyName() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newUsuario = new UsuarioEntity();
            newUsuario.setNombre(""); // Nombre vacío
            newUsuario.setCorreo("mario.rossi@example.com");

            usuarioService.createUsuario(newUsuario);
        });
    }

    @Test
    void testUpdateUsuario() throws IllegalOperationException {
        usuarioEntity.setNombre("Updated Name");
        usuarioService.updateUsuario(usuarioEntity.getId(), usuarioEntity);

        UsuarioEntity updatedEntity = entityManager.find(UsuarioEntity.class, usuarioEntity.getId());
        assertEquals("Updated Name", updatedEntity.getNombre());
    }

    @Test
    void testDeleteUsuario() throws IllegalOperationException {
        usuarioService.deleteUsuario(usuarioEntity.getId());
        UsuarioEntity deletedEntity = entityManager.find(UsuarioEntity.class, usuarioEntity.getId());
        assertNull(deletedEntity);
    }

    @Test
    void testGetUsuario() throws IllegalOperationException {
        UsuarioEntity foundUsuario = usuarioService.getUsuario(usuarioEntity.getId());
        assertNotNull(foundUsuario);
        assertEquals(usuarioEntity.getNombre(), foundUsuario.getNombre());
    }
}

