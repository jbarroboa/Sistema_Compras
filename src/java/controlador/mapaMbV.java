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
import javax.xml.bind.ParseConversionEvent;
import modelo.EnumLocal;
import modelo.EnumUsuario;
import modelo.Local;
import modelo.Persona;
import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
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
public class mapaMbV implements Serializable {

    private Session session;
    private Transaction transaction;
    private Local local;
    private Usuario usuarioCreacion;
    private List<Local> listLocals = new ArrayList();
    private MapModel draggableModel;
    private Marker marker;
    private String centerGeoMap;

    public mapaMbV() {
        this.local = new Local();
        HttpSession sessionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioCreacion = (Usuario) sessionUsuario.getAttribute("Usuario");
        draggableModel = new DefaultMapModel();

    }

    /**
     * Registra Locales
     */
    public String cargarMapaPropietario() {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            local = localDao.buscarLocalUsuario(session, usuarioCreacion.getIdUsuario());
            this.transaction.commit();
            //Shared coordinates 
            //centerGeoMap = "-4.008366857632468, -79.21082496643066";
            centerGeoMap = local.getLatitud() + "," + local.getLongitud();
            LatLng coord1 = new LatLng(local.getLatitud(), local.getLongitud());
            //Draggable
            draggableModel.addOverlay(new Marker(coord1, local.getNombre()));
            for (Marker premarker : draggableModel.getMarkers()) {
                premarker.setDraggable(false);
            }
            return centerGeoMap;
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

    /**
     * Registra Locales
     */
    public String cargarMapaLista() {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            draggableModel = new DefaultMapModel();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            listLocals = localDao.buscarLocalList(session);
            this.transaction.commit();
            //Shared coordinates 
            centerGeoMap = "-4.008366857632468, -79.21082496643066";

            for (Local locals : listLocals) {
                if (locals.getLatitud()!=0.0) {
                    LatLng coord1 = new LatLng(locals.getLatitud(), locals.getLongitud());
                    draggableModel.addOverlay(new Marker(coord1, locals.getNombre()));
                }
            }

            //Draggable
            for (Marker premarker : draggableModel.getMarkers()) {
                premarker.setDraggable(false);
            }
            return centerGeoMap;
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

    public void onMarkerSelect(OverlaySelectEvent event) {
        this.session = null;
        this.transaction = null;
        try {
            LocalDao localDao = new LocalDao();
            draggableModel = new DefaultMapModel();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            marker = (Marker) event.getOverlay();
            local = localDao.buscarLocalNombre(session, marker.getTitle());
            RequestContext.getCurrentInstance().update("frmVerLocal:panelDetalleLocal");
            RequestContext.getCurrentInstance().execute("PF('dialogoVerLocal').show()");
            this.transaction.commit();
        } catch (Exception e) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + e.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();

            }
        }
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Usuario getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuario usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public List<Local> getListLocals() {
        return listLocals;
    }

    public void setListLocals(List<Local> listLocals) {
        this.listLocals = listLocals;
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
