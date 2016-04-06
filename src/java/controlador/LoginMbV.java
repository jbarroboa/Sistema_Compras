/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.HibernateUtil;
import dao.LoginDao;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import modelo.EnumUsuario;
import modelo.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ASUS COMPUTERS
 */
@ManagedBean
@SessionScoped
public class LoginMbV {

    private Session session;
    private Transaction transaction;
    private Persona persona;
    private String cedula;
    private String clave;

    public LoginMbV() {
        HttpSession miSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        miSession.setMaxInactiveInterval(2000);
    }

    /*Abrir session**/
    public String login() {
        this.session = null;
        this.transaction = null;
        try {
            LoginDao loginDao = new LoginDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            this.persona = loginDao.buscarUsuario(this.session, this.cedula);
            this.transaction.commit();
            if (persona != null) {
                if ((persona.getUsuario().getClave().equals(clave))) {
                    //se ingresa a la seccion va a las vistas de usuarios                    
                    //esto es para el filtro
                    HttpSession httpsession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    httpsession.setAttribute("Usuario", this.persona.getUsuario()); //se le pone un atributo para q lo reconosca en el filtro                     
                    if (persona.getUsuario().getTipo().equals(EnumUsuario.ADMINISTRADOR)) {
                        return "/local/registrar.xhtml?faces-redirect=true";
                    } else {
                        if (persona.getUsuario().getTipo().equals(EnumUsuario.PROPIETARIO)) {
                            return "/producto/registrar.xhtml?faces-redirect=true";
                        } else {
                            return null;
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "No se puede determinar que las credenciales proporcionadas sean auténticas."));
                    this.clave = "";
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso:", "No se puede determinar que las credenciales proporcionadas sean auténticas."));
                this.clave = "";
                return null;
            }
        } catch (Exception e) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + e.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();

            }
        }
    }

    //serrar seccion
    public String cerrarSession() {
        this.cedula = null;
        this.clave = null;
        this.persona = null;
        HttpSession httpsession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpsession.invalidate();
        return "/faces/vistas/index?faces-redirect=true";//dirigir a la pagina principal
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

}
