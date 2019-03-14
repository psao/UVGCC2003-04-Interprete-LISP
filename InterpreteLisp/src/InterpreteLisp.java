
import java.util.List;




/**
 * Programa pincipal desde el cual se ejecuta el interprete de LISP
 * @author pablo
 * @version 03/02/2019
 */
public class InterpreteLisp {
    
    final static String DELIMITADOR = " \t\n\r\f";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        int opcion = 0;
        
        try{
            while(true){

                System.out.println("\n\t\tMenú");
                System.out.println("1) Ejecutar comando LISP");
                System.out.println("2) Salir");

                System.out.print("Ingrese la opción: ");
                //Realizamos la lectura de la terminal con la ayuda de la clase Keyboard
                opcion = Keyboard.readInt();

                switch(opcion){
                    case 1:
                        //Realizamos la solicitud del path del archivo a leer
                        String path = "";
                        System.out.print("Ingrese el Path del archivo: ");
                        
                        //Leemos el path con la ayuda de la clase Keyboard
                        path = Keyboard.readString();
                        
                        //Creamos un objeto del manejador de datos
                        DataManager archivo = new DataManager();
                        
                        //Seteamos el path del archivo en el objeto
                        archivo.setPathFile(path);
                        
                        //Verificamos que exista el archivo
                        if(archivo.getExists()){
                            //Mediante el objeto instanciado, obtenemos los tokens y parseo de la instruccion en lisp
                            runLisp(archivo.getInstruccion(archivo.getTokens(DELIMITADOR)));
                        }
                        else{
                            System.out.println(String.format("\n\t\tEl archivo de la ruta %s no fue encontrado", path));
                        }
                        break;
                    case 2:
                        //Salimos del programa
                        System.exit(0);
                }
            
            }
        }
        catch(Exception e){
            System.out.println(String.format("\n\n\t\tOcurrio el problema: %s",e.toString()));
        }
        
    }
    
    /**
     * Método para ejecutar la instrucción de LISP
     * @param value objeto con las instrucciones de LISP
     */
    public static void runLisp(Object value) throws Exception{
        //Casteamos el objeto a tipo List y lo asignamos auna variable List
        List instruccion = (List)value;
        
        //Despliegue temporal del parseo de las instrucciones
        System.out.println(instruccion);
    }
    
}
