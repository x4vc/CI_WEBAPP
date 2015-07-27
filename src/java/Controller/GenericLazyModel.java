/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.GenericDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


/**
 *
 * @author tulioasc
 * @param <T>
 */
public abstract class GenericLazyModel<T> extends LazyDataModel<T> implements Serializable {
    
    private GenericDAO dao;

    public abstract Class<T> getEntityClass();
    
    public String getDefaultSortField() { return null; }

    public GenericLazyModel(GenericDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if (sortField == null) {
            sortField = getDefaultSortField();
        }
        List<T> data = new ArrayList<>();
        try {
           String order = (sortOrder == SortOrder.ASCENDING) ? "ASC" : "DESC";
            
           data = dao.select(first, pageSize, sortField, order, filters);
           this.setRowCount(dao.getRow());
        } catch (Exception ex) {
            Logger.getLogger(GenericLazyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        /*
         * The following is in ancestor (LazyDataModel): this.rowIndex =
         * rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
         */
        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        } else {
            super.setRowIndex(rowIndex % getPageSize());
        }
    }
}
