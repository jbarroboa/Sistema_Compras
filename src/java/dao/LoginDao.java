/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import modelo.Persona;
import modelo.Usuario;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ASUS COMPUTERS
 */
public class LoginDao {
    /**
     * Buscar Usuario por id
     */
    public Persona buscarUsuario(Session session, String cedula) throws Exception {
        String hql = "FROM Persona WHERE usuario.cedula=:cedulas";
        Query query = session.createQuery(hql);
        query.setParameter("cedulas", cedula);
        return (Persona) query.uniqueResult();
    }
}
