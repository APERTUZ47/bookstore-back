package co.edu.uniandes.dse.bookstore.services;

import co.edu.uniandes.dse.bookstore.entities.Usuario;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioEntity createUsuario(UsuarioEntity usuario) throws IllegalOperationException {
        
        List<UsuarioEntity> existingUsuarios = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (!existingUsuarios.isEmpty()) {
            throw new IllegalOperationException("Ya existe un usuario con ese correo electrónico");
        }
        
        
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre del usuario no puede estar vacío");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioEntity updateUsuario(Long id, UsuarioEntity usuario) throws IllegalOperationException {
        UsuarioEntity existingUsuario = usuarioRepository.findById(id).orElseThrow(() -> 
            new IllegalOperationException("Usuario no encontrado"));

        
        List<UsuarioEntity> existingUsuarios = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (!existingUsuarios.isEmpty() && !existingUsuarios.get(0).getId().equals(id)) {
            throw new IllegalOperationException("Ya existe un usuario con ese correo electrónico");
        }

       
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalOperationException("El nombre del usuario no puede estar vacío");
        }

        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setCorreo(usuario.getCorreo());
        
        return usuarioRepository.save(existingUsuario);
    }

    public List<UsuarioEntity> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public UsuarioEntity getUsuario(Long id) throws IllegalOperationException {
        return usuarioRepository.findById(id).orElseThrow(() -> 
            new IllegalOperationException("Usuario no encontrado"));
    }

    @Transactional
    public void deleteUsuario(Long id) throws IllegalOperationException {
        UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(() -> 
            new IllegalOperationException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }
}
