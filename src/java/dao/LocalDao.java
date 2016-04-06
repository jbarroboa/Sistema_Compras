/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import modelo.Local;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ASUS COMPUTERS
 */
public class LocalDao {
    /**
     * Registrar
     */
    public boolean register(Session session, Object o) throws Exception {
        session.save(o);
        return true;
    }

    /**
     * Eliminar
     */
    public boolean delete(Session session, Object o) throws Exception {
        session.delete(o);
        return true;
    }

    /**
     * Actualizar
     */
    public boolean update(Session session, Object o) throws Exception {
        session.update(o);
        return true;
    }

    /**
     * Buscar Local por id
     */
    public Local buscarLocalId(Session session, Long idLocal) throws Exception {
        String hql = "FROM Local WHERE idLocal=:local";
        Query query = session.createQuery(hql);
        query.setParameter("local", idLocal);
        return (Local) query.uniqueResult();
    }
    /**
     * Buscar Local por id
     */
    public Local buscarLocalNombre(Session session, String nombre) throws Exception {
        String hql = "FROM Local WHERE nombre=:nombre";
        Query query = session.createQuery(hql);
        query.setParameter("nombre", nombre);
        return (Local) query.uniqueResult();
    }
    
    /**
     * Buscar por coincidencia de appellido, nombre o cedula
     */
    public List<Local> buscarLocal(Session session, String atributo) throws Exception {
        String hql = "FROM Local where nombre like :àtributo";
        Query query = session.createQuery(hql);
        query.setParameter("àtributo", "%" + atributo + "%");
        return query.list();
    }

    /**
     * Buscar Local por id
     */
    public Local buscarLocalUsuario(Session session, Long idUsuario) throws Exception {
        String hql = "FROM Local WHERE persona.usuario.idUsuario=:usuario";
        Query query = session.createQuery(hql);
        query.setParameter("usuario", idUsuario);
        return (Local) query.uniqueResult();
    }
    
    /**
     * Buscar por coincidencia de appellido, nombre o cedula
     */
    public List<Local> buscarLocalList(Session session) throws Exception {
        String hql = "from Local";
        Query query = session.createQuery(hql);
        return query.list();
    }
}
