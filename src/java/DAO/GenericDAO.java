package DAO;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.JPAUtil;


/**
 *
 * @author Paulo
 */
public abstract class GenericDAO<T, ID extends Serializable> {

    public GenericDAO() {
    }

    public abstract Class<T> getEntityClass();

    public T persist(T entidade) {
        return JPAUtil.getEntityManager().merge(entidade);
    }

    public void delete(T entidade) throws Exception {
        JPAUtil.getEntityManager().remove(entidade);
    }

    public T selectFromId(ID id) throws Exception {
        return JPAUtil.getEntityManager().find(getEntityClass(), id);
    }

    public List<T> select() throws Exception {
        Query dynaQuery = JPAUtil.getEntityManager().createQuery("FROM " + getEntityClass().getName());
        return dynaQuery.getResultList();
    }

    public List<T> select(int first, int max, String sortField, String sortOrder, Map<String, String> filterMap) throws Exception {
        String filtros = montarStrFiltroDefault(filterMap);
        String strAtivo = " WHERE Ativo = 1 ";
        sortField = (sortField != null) ? (" ORDER BY " + sortField + " " + sortOrder) : "";
        //String strQuery = "FROM " + getEntityClass().getName() + filtros + sortField;
        String strQuery = "FROM " + getEntityClass().getName() + strAtivo + filtros + sortField;
        
        //Cria e executa a query:
        Query dynaQuery = JPAUtil.getEntityManager().createQuery(strQuery).setFirstResult(first).setMaxResults(max);
        return dynaQuery.getResultList();
    }

    public List<T> select(String query) throws Exception {
        Query dynaQuery = JPAUtil.getEntityManager().createQuery(query);
        return dynaQuery.getResultList();
    }

    public T selectSingle(String query) throws Exception {
        Query dynaQuery = JPAUtil.getEntityManager().createQuery(query);
        try {
            return (T) dynaQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    public int countRow(String query)throws Exception {
        Query dynaQuery = JPAUtil.getEntityManager().createQuery(query);
        Long count = (Long) dynaQuery.getSingleResult();
        return Integer.parseInt(count.toString());
    }
    

    public int getRow() throws Exception {
        Query dynaQuery = JPAUtil.getEntityManager().createQuery("SELECT COUNT(*) FROM " + getEntityClass().getName());
        Long count = (Long) dynaQuery.getSingleResult();
        return Integer.parseInt(count.toString());
    }
    
    public int getRow(Map<String, String> filters) throws Exception {
        String qryCount = "SELECT COUNT(*) FROM " + getEntityClass().getName();
        String filtros = montarStrFiltroDefault(filters);
        
        Query dynaQuery = JPAUtil.getEntityManager().createQuery(qryCount + filtros);
        Long count = (Long) dynaQuery.getSingleResult();
        return Integer.parseInt(count.toString());
    }
    
    protected String montarStrFiltroDefault(Map<String, String> filters) {
        String strFiltros = ""; 
        if (filters != null && !filters.isEmpty()) {
            strFiltros = " WHERE ";
            for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                String filterProperty = it.next();
                String filterValue = filters.get(filterProperty);
                strFiltros += filterProperty + " LIKE '" + filterValue + "%'";
                if (it.hasNext()) {
                    strFiltros += " AND ";
                }
            }
        }
        return strFiltros;
    }
    
    public void manualFlush() {
        JPAUtil.getEntityManager().flush();
    }
    
    public boolean isManaged(T entidade) {
        return JPAUtil.getEntityManager().contains(entidade);
    }
    
}
