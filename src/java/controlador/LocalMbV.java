/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.HibernateUtil;
import dao.LocalDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import modelo.EnumLocal;
import modelo.EnumUsuario;
import modelo.Local;
import modelo.Persona;
import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author ASUS COMPUTERS
 */
@ManagedBean
@ViewScoped
public class LocalMbV implements Serializable {

    private Session session;
    private Transaction transaction;
    private Local local;
    private List<Local> listLocals = new ArrayList();
    private Persona persona;
    private Usuario usuario;
    private Usuario usuarioCreacion;
    private EnumLocal tipo[];
    private String nombre;
    private String clave;
    private MapModel draggableModel;
    private Marker marker;
    private String centerGeoMap;

    public LocalMbV() {
        this.local = new Local();
        this.persona = new Persona();
        this.usuario = new Usuario();
        centerGeoMap = "-4.008366857632468, -79.21082496643066";
        HttpSession sessionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioCreacion = (Usuario) sessionUsuario.getAttribute("Usuario");
        draggableModel = new DefaultMapModel();

        //Shared coordinates 
        LatLng coord1 = new LatLng(-4.008366857632468, -79.21082496643066);
        //Draggable
        draggableModel.addOverlay(new Marker(coord1, "Konyaalti"));
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
    }

    /*Registra Locales**/
    public void registar() {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            this.usuario.setFechaCreacion(new Date());
            this.usuario.setTipo(EnumUsuario.PROPIETARIO);
            this.usuario.setUsuarioCreacion(usuarioCreacion);
            localDao.register(session, this.usuario);
            this.persona.setFechaCreacion(new Date());
            this.persona.setUsuarioCreacion(usuarioCreacion);
            this.persona.setUsuario(usuario);
            localDao.register(session, this.persona);
            this.local.setPersona(persona);
            this.local.setFechaCreacion(new Date());
            this.local.setUsuarioCreacion(usuarioCreacion);
            localDao.register(session, this.local);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue relizado correctamete"));
            this.local = new Local();
            this.persona = new Persona();
            this.usuario = new Usuario();
        } catch (Exception e) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + e.getMessage()));
            return;
        } finally {
            if (this.session != null) {
                this.session.close();

            }
        }
    }

    /*listado de todos los locales registrados**/
    public void buscarLocal() {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            listLocals = localDao.buscarLocal(session, nombre);
            this.transaction.commit();
        } catch (Exception e) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + e.getMessage()));
            return;
        } finally {
            if (this.session != null) {
                this.session.close();

            }
        }
    }

    /**
     * Cargar el Local
     */
    public void cargarLocal(Long codigoLocal) {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.local = localDao.buscarLocalId(session, codigoLocal);
            this.persona = local.getPersona();
            this.usuario = persona.getUsuario();
            centerGeoMap = local.getLatitud() + ", " + local.getLongitud();
            RequestContext.getCurrentInstance().update("frmEditarLocal");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarLocal').show()");
            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    /**
     * Cargar el Local
     */
    public void cargarMapEdit() {
        draggableModel = new DefaultMapModel();
        if (local.getLatitud()!=0.0) {
            centerGeoMap = local.getLatitud() + ", " + local.getLongitud();
            LatLng coord1 = new LatLng(local.getLatitud(), local.getLongitud());
            draggableModel.addOverlay(new Marker(coord1, local.getNombre()));
        } else {
            centerGeoMap = "-4.008366857632468, -79.21082496643066";
            LatLng coord1 = new LatLng(-4.008366857632468, -79.21082496643066);
            draggableModel.addOverlay(new Marker(coord1, local.getNombre()));
        }
        //Shared coordinates 
        //Draggable
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
        RequestContext.getCurrentInstance().update("frmMapa");
        RequestContext.getCurrentInstance().execute("PF('dlg').show()");
    }

    /**
     * Editar Producto
     */
    public void update() {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            if (!this.clave.equals("")) {
                this.usuario.setClave(clave);
            }
            localDao.update(session, this.usuario);
            localDao.update(session, this.persona);
            localDao.update(session, this.local);
            this.transaction.commit();
            this.local = new Local();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue relizado correctamete"));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        this.local.setLatitud(marker.getLatlng().getLat());
        this.local.setLongitud(marker.getLatlng().getLng());
    }

    public void cancelar() {
        this.local.setLatitud(0.0);
        this.local.setLongitud(0.0);
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuario usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public EnumLocal[] getTipo() {
        return EnumLocal.values();
    }

    public void setTipo(EnumLocal[] tipo) {
        this.tipo = tipo;
    }

    public List<Local> getListLocals() {
        return listLocals;
    }

    public void setListLocals(List<Local> listLocals) {
        this.listLocals = listLocals;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public void setDraggableModel(MapModel draggableModel) {
        this.draggableModel = draggableModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getCenterGeoMap() {
        return centerGeoMap;
    }

    public void setCenterGeoMap(String centerGeoMap) {
        this.centerGeoMap = centerGeoMap;
    }

}
