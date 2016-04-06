/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import modelo.Local;
import modelo.Producto;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ASUS COMPUTERS
 */
public class ProductoDao {
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
     * Buscar Producto por id
     */
    public Producto buscarProductoId(Session session, Long idProducto) throws Exception {
        String hql = "FROM Producto WHERE idProducto=:producto";
        Query query = session.createQuery(hql);
        query.setParameter("producto", idProducto);
        return (Producto) query.uniqueResult();
    }
    
    /**
     * Buscar por coincidencia de appellido, nombre o cedula
     */
    public List<Producto> buscarProductos(Session session, String atributo, Long idLocal) throws Exception {
        String hql = "FROM Producto where local.idLocal=:local and (nombre like :àtributo)";
        Query query = session.createQuery(hql);
        query.setParameter("àtributo", "%" + atributo + "%");
        query.setParameter("local", idLocal);
        return query.list();
    }
   
    /**
     * Buscar el local por el usuario
     */
    public Local buscarLocalUsuario(Session session, Long idUsuario) throws Exception {
        String hql = "FROM Local where persona.usuario.idUsuario=:usuario";
        Query query = session.createQuery(hql);
        query.setParameter("usuario", idUsuario);
        return (Local)query.uniqueResult();
    }
    
    /**
     * Buscar el local por el usuario
     */
    public List<Producto> buscarProductosNombre(Session session, String nombre) throws Exception {
        String hql = "FROM Producto where (nombre like :atributo) and estado=true";
        Query query = session.createQuery(hql);
        query.setParameter("atributo", "%" + nombre + "%");
        return query.list();
    }
}
