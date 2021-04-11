package mx.com.gm.sga.cliente.jpql;

import java.util.Iterator;
import java.util.List;
import javax.persistence.*;
import mx.com.gm.sga.domain.Persona;
import mx.com.gm.sga.domain.Usuario;
import org.apache.logging.log4j.*;

public class PruebaJPQL {
    
    static Logger log = LogManager.getRootLogger();
    
    public static void main(String[] args) {
        String jpql = null; //define el qwerty 
        Query q = null;
        List<Persona> personas = null;
        Persona persona = null; // guarda el resultado de la consulta en el tipo de objeto persona
        Iterator iter = null;
        Object[] tupla = null; //resive varios objetos de diferente tipo
        List<String> nombres = null;
        List<Usuario> usuarios = null;
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SgaPU");
        EntityManager em = emf.createEntityManager();
        
        //1. Consulta de todos los objetos de tipo Persona
        log.debug("\n1. Consulta de todas las Personas");
        jpql = "select p from Persona p"; //define el qwerty 
                                 //pasa el nuevo qwerty
        personas = em.createQuery(jpql).getResultList();
        //mostrarPersonas( personas ); //metodo para mostrar las pwersonas dentro de la clase actual
        
        //2. consulta de personaa con un id
        log.debug("\n2. consulta de persona con id = 1 ");
        jpql = "select p from Persona p where p.idPersona = 1"; // se re-define el qwerty con la misma variable
                 //la consulta retorna un objeto por ellos se debe castear   
       //asignamos al objeto persona creado          
        persona = (Persona) em.createQuery(jpql).getSingleResult();
        //log.debug(persona);
        
        //3. consullta por campo nombre
        jpql = "select p from Persona p where p.nombre = 'adrian'"; // se re-define el qwerty con la misma variable
        personas = em.createQuery(jpql).getResultList(); //utilizamos la sentencia de resultlist dado que la consulta puede devoover mas de 1 resultado
        //mostrarPersonas( personas );//se muestra iterando la lista por el metodo
    
        //4. consulta de datos individuales , se crea una rreglo tupla de objet con  3 columnas
        log.debug("\n4. consulta de datos individuales en TUPLA de 3 columnas");
        jpql = "select p.nombre as Nombre, p.apellido as Apellido, p.email as Email from Persona p";
        iter = em.createQuery(jpql).getResultList().iterator(); // al crear la tupla  se debe usar el iterator para q regrese los valore sde la columna
        while(iter.hasNext()){ //usamos hasnext para saber si exixte valor despues del elido y lo leea
            
            tupla = (Object[]) iter.next(); //iteramos para obtener los datos
            String nombre = (String) tupla[0];
            String apellido = (String) tupla[1];
            String email = (String) tupla[2];
           // log.debug("Nombre: " + nombre + "  Apellido: " + apellido + "  Email: " + email);
        }
        
        //5. areglo de tipo objet con 2 columnas de persona y el id
        log.debug("\n5. consulta de areglo de tipo objet con 2 columnas");
        jpql = "select p, p.idPersona from Persona p";
        iter = em.createQuery(jpql).getResultList().iterator();
         while(iter.hasNext()){ //usamos hasnext para saber si exixte valor despues del elido y lo leea
             tupla = (Object[]) iter.next();
             persona = (Persona) tupla[0];
             int idPersona = (int) tupla[1];
         //log.debug("objeto persona: " + persona);
        //log.debug("id persona: " + idPersona);
         }
         
         //6. objeto persona con id persona con los delmas valores nulos
         log.debug("\6. consulta de id con los demas campos en null por objeto ");
         jpql = "select new mx.com.gm.sga.domain.Persona (p.idPersona) from Persona p";
          personas = em.createQuery(jpql).getResultList();
         // mostrarPersonas( personas );
          
         //7. Regresa el valor minimo y maximo del idPersona ( scaler result) 
         log.debug("\7. regresa el valor minimo y maximo por funciones min maz y count ");
         jpql ="select min(p.idPersona) as MinId, max(p.idPersona) as MaxId,count(p.idPersona) as Contador from Persona p";
         iter = em.createQuery(jpql).getResultList().iterator();
         while(iter.hasNext()){ //usamos hasnext para saber si exixte valor despues del elido y lo leea
             tupla = (Object[]) iter.next();
             Integer idMin = (Integer) tupla[0];
             Integer idMax = (Integer) tupla[1];
             Long Count = (Long) tupla[2];
             //log.debug("idMin: " + idMin + " idMax: " + idMax + " Contador: " + Count );
            
         }    
         
         //8. Cuenta los nombres de las personas que son distintos
         log.debug("\n 8. Cuenta los nombres de las personas que son distintos");
         jpql ="select count(distinct p.nombre) from Persona p";
         Long contador = (Long) em.createQuery(jpql).getSingleResult();
         log.debug(" No. de perosnas con nombres distintos: " + contador);
         
         //9. Concatena y comvierte a mayusculas el nombre y apellido
         log.debug("\n 9. Concatena y comvierte a mayusculas el nombre y apellido");
         jpql ="select CONCAT(p.nombre, ' ', p.apellido)as Nombre from Persona p";
         //nombres es un arreglo String previamente definido
         nombres = em.createQuery(jpql).getResultList();
         for (String nombreCompleto: nombres){ //iteramos el arreglo de nombres
             //log.debug(nombreCompleto);
         }
         
         //10. obtenemos el objeto persona con id proporcionando un parametro
         log.debug("\10. obtenemos el objeto persona con id proporcionando un parametro");
         int idPersona = 1; // parametro que se enviara en la consulta
         jpql = "select p from Persona p where p.idPersona = :id"; //definimos el esquema del qwerty con :id que sera el dato a usar
         // es una variable de tipo QWERTY
         q = em.createQuery(jpql); //es un objeto typo qwerty sin datos
         q.setParameter("id",idPersona); // pasmaos por el emtodo setParameter los parametro apra ejecutar ql qwerty
         persona = (Persona) q.getSingleResult(); // obtenos el resultado y guiardamos en objeto de tipo persona
         //log.debug(persona);
         
         //11. personas que ocntengan una letra A en el nombre sea mayuscula o minuscula
         log.debug("\11. Consulta personas que ocntengan una letra A en el nombre sea mayuscula o minuscula");
                                              //upper comvierte a mayusculas  
         jpql = "select p from Persona p where upper(p.nombre) like upper(:parametro)";
         String parametroString = "%a%";//caracter que se utilizara en el like
          q = em.createQuery(jpql); //es un objeto typo qwerty sin datos
         q.setParameter("parametro",parametroString); // pasmaos por el emtodo setParameter los parametro apra ejecutar ql qwerty
         personas = q.getResultList(); // obtenos el resultado y guiardamos en objeto de tipo persona
         //mostrarPersonas(personas);
         
         //12. uso de between
         log.debug("\n 12. uso de between");                           //se pueden definir los valores por :param2 asignando con setParameter
         jpql ="select p from Persona p where p.idPersona between 1 and 2";
         personas = em.createQuery(jpql).getResultList();
         //mostrarPersonas(personas);
         
         
         //13. uso de ordenamiento
         log.debug("\n 13. uso de ordenamiento");                          
         jpql ="select p from Persona p where p.idPersona > 3 order by p.nombre desc, p.apellido desc";
         personas = em.createQuery(jpql).getResultList();
         //mostrarPersonas(personas);
         
         //14. uso de subqwerty ( no todas las db lo aceptan, mysql oracle postgres las aceptan)
         log.debug("\n 14. uso de sub qwerty");                       //p1 se usa dado que ya existe p  
         jpql ="select p from Persona where p.idPersona in(select min(p1.idpersona) from Persona p1)";
         personas = em.createQuery(jpql).getResultList();
         //mostrarPersonas(personas);
         
         //15. uso de join con lazy loading
         log.debug("\n 15. uso de join con lazy loading");
         jpql ="select u from Usuario join u.persona p";
         usuarios = em.createQuery(jpql).getResultList();
         //mostrarUsuarios(usuarios);
         
         //16. uso de let join aplicando el concepto de eager loading
         
         log.debug("\n 15. uso de let join aplicando el concepto de eager loading");
                                                  //fetch indica q es eager 
         jpql = "select u from Usuario u left join fetch u.persona";
         usuarios = em.createQuery(jpql).getResultList();
         mostrarUsuarios(usuarios);
         
    }

    private static void mostrarPersonas(List<Persona> personas) {
        for(Persona p: personas){
            log.debug(p);
        }
        }
    
    private static void mostrarUsuarios( List<Usuario> usuarios) {
        for(Usuario u: usuarios){
            log.debug(u);
        }
        }
        
    
}
