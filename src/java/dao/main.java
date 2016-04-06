/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import modelo.EnumUsuario;
import modelo.Local;
import modelo.Persona;
import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ASUS COMPUTERS
 */
public class main {

    

    public void registrarUsuario() {
        Transaction transaccion = null;
        Session session = null;
        try {
            LocalDao localDao=new LocalDao();
            session = HibernateUtil.getSessionFactory().openSession();
            transaccion = session.beginTransaction();
            Usuario usuario = new Usuario();
            usuario.setCedula("0705497162");
            usuario.setClave("0705497162");
            usuario.setFechaCreacion(new Date());
            usuario.setTipo(EnumUsuario.ADMINISTRADOR);
            localDao.register(session, usuario);
            Persona persona=new Persona();
            persona.setApellidos("Arrobo Ajila");
            persona.setFechaCreacion(new Date());
            persona.setNombres("Jonathan Bladimir");
            persona.setTelefono("0959278231");
            persona.setUsuario(usuario);
            localDao.register(session, persona);
            transaccion.commit();
        } catch (Exception ex) {
            if (transaccion != null) {
                transaccion.rollback();
            }
            return;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    public void buscarPersona() {
        Transaction transaccion = null;
        Session session = null;
        try {
            LoginDao loginDao=new LoginDao();
            session = HibernateUtil.getSessionFactory().openSession();
            transaccion = session.beginTransaction();
            System.out.println(loginDao.buscarUsuario(session, "0705497162").getApellidos());
            transaccion.commit();
        } catch (Exception ex) {
            if (transaccion != null) {
                transaccion.rollback();
            }
            return;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    public void probar() {
        Transaction transaccion = null;
        Session session = null;
        try {
            LocalDao localDao = new LocalDao();
            session = HibernateUtil.getSessionFactory().openSession();
            transaccion = session.beginTransaction();
            
            for (Local lo:localDao.buscarLocalList(session)) {
                System.out.println(lo.getNombre());
            }
            transaccion.commit();
        } catch (Exception ex) {
            if (transaccion != null) {
                transaccion.rollback();
            }
            return;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
