/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.HibernateUtil;
import dao.ProductoDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import modelo.Local;
import modelo.Producto;
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
public class PruebaMbV implements Serializable {

    //private String centerGeoMap = "-4.008366857632468, -79.21082496643066";
    private MapModel draggableModel;
    private String centerGeoMap;
    private Marker marker;

    public PruebaMbV() {
        draggableModel = new DefaultMapModel();
        centerGeoMap = "-4.008366857632468, -79.21082496643066";
        //Shared coordinates
        LatLng coord1 = new LatLng(-4.008366857632468, -79.21082496643066);
        //Draggable
        draggableModel.addOverlay(new Marker(coord1, "Konyaalti"));
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));
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
