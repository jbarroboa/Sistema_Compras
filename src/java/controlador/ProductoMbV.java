/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.HibernateUtil;
import dao.ProductoDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import modelo.EnumLocal;
import modelo.Local;
import modelo.Persona;
import modelo.Producto;
import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
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
public class ProductoMbV {

    private Session session;
    private Transaction transaction;
    private Local local;
    private Producto producto;
    private List<Producto> listProductos = new ArrayList();
    private Usuario usuarioCreacion;
    private String nombre;
    private MapModel draggableModel;
    private Marker marker;
    private String centerGeoMap;

    public ProductoMbV() {
        this.local = new Local();
        this.producto = new Producto();
        draggableModel = new DefaultMapModel();
        HttpSession sessionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioCreacion = (Usuario) sessionUsuario.getAttribute("Usuario");
    }

    /**
     * Registra Productos
     */
    public void registar() {
        this.session = null;
        this.transaction = null;
        try {
            ProductoDao productoDao = new ProductoDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            this.local = productoDao.buscarLocalUsuario(session, usuarioCreacion.getIdUsuario());
            this.producto.setFechaCreacion(new Date());
            this.producto.setUsuarioCreacion(usuarioCreacion);
            this.producto.setEstado(true);
            this.producto.setLocal(local);
            productoDao.register(session, this.producto);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue relizado correctamete"));
            this.local = new Local();
            this.producto = new Producto();
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

    /*listado de todos los Productos registrados**/
    public void buscarProducto() {
        this.session = null;
        this.transaction = null;
        try {
            ProductoDao productoDao = new ProductoDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            this.local = productoDao.buscarLocalUsuario(session, usuarioCreacion.getIdUsuario());
            listProductos = productoDao.buscarProductos(session, nombre, local.getIdLocal());
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
     * Cargar el producto
     */
    public void cargarProducto(Long codigoProducto) {
        this.session = null;
        this.transaction = null;
        try {
            ProductoDao productoDao = new ProductoDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.producto = productoDao.buscarProductoId(session, codigoProducto);
            RequestContext.getCurrentInstance().update("frmEditarProducto:editarProducto");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarProducto').show()");
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
     * Cargar el producto
     */
    public void cargarProductoCliente(Long codigoProducto) {
        this.session = null;
        this.transaction = null;
        try {
            ProductoDao productoDao = new ProductoDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.producto = productoDao.buscarProductoId(session, codigoProducto);
            RequestContext.getCurrentInstance().update("frmListadoProductos:panelDetalleProducto");
            RequestContext.getCurrentInstance().execute("PF('dialogoDetalleproducto').show()");
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
     * Editar Producto
     */
    public void update() {
        this.session = null;
        this.transaction = null;
        try {
            ProductoDao productoDao = new ProductoDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            productoDao.update(session, this.producto);
            this.transaction.commit();
            this.producto = new Producto();
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

    /**
     * Buscar productos por el nombre
     */
    public void buscarProductoNomre() {
        this.session = null;
        this.transaction = null;
        try {
            ProductoDao productoDao = new ProductoDao();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            listProductos = productoDao.buscarProductosNombre(session, nombre);
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
     * Registra Locales
     */
    public void cargarMapaProducto() {
        if (producto.getLocal().getLongitud()!=0.0) {
            centerGeoMap = producto.getLocal().getLatitud() + ", " + producto.getLocal().getLongitud();
            LatLng coord1 = new LatLng(producto.getLocal().getLatitud(), producto.getLocal().getLongitud());
            draggableModel.addOverlay(new Marker(coord1, producto.getLocal().getNombre()));
            for (Marker premarker : draggableModel.getMarkers()) {
                premarker.setDraggable(false);
            }
            RequestContext.getCurrentInstance().update("frmMapa");
            RequestContext.getCurrentInstance().execute("PF('dlg').show()");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "No a registrado la ubicación"));
            return;
        }
    }

    /**
     * Registra Locales
     */
    public boolean habilitarBotonMapa() {
        if (producto.getNombre() != null) {
            if (producto.getLocal().getLongitud() != 0.0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Producto> getListProductos() {
        return listProductos;
    }

    public void setListProductos(List<Producto> listProductos) {
        this.listProductos = listProductos;
    }

    public Usuario getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuario usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
