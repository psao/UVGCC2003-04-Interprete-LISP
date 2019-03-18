
import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




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
                            //Obteniendo lista de los tokens generados
                            List instruccion = archivo.getTokens(DELIMITADOR);
                            
                            //Mostrandole al usuario la expresión a evaluar
                            System.out.println(String.format( "\n\n\t\tExpresión a Evaluar: \n\n%s\n",archivo.getDataFile()));
                            
                            System.out.println(archivo.getListInstruccion());
                            
                            //Mediante el objeto instanciado, obtenemos los tokens y parseo de la instruccion en lisp
                            runLisp(archivo.getInstruccion(instruccion));
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
        try{
            //Casteamos el objeto a tipo List y lo asignamos auna variable List
            List instruccion = (List)value;
            
            //System.out.println(instruccion);
            
            List<Defun> deFun = new ArrayList<>();
            //Evaluar sintaxis

            //Al cumplir con la evaluación de la sintaxis mostramos que instrucción se evaluara
//            System.out.println(String.format( "\n\n\t\tExpresión a Evaluar: %s",(String) instruccion.stream()
//                                .map(n -> String.valueOf(n))
//                                .collect(Collectors.joining(" ", "(", ")")))
//                                .replace(",", " ")
//                                .replace("[", "(")
//                                .replace("]", ")"));

            //Verificamos si contiene la instrucción ATOM
            if(instruccion.contains("atom")){
                
                //Si el tamaño es de 2, la sintaxis de LISP para atom es correcta
                if(instruccion.size() == 2 ){
                    //System.out.println("Ejecuta atom");
                    if( (new functionEvaluation()).isAtom(instruccion.get(1))){
                        System.out.print("\n\t\tResultado: True\n\n");
                    }
                    else{
                        System.out.print("\n\t\tResultado: NIL\n\n");
                    }
                }
                //de lo contrario salimos de la ejecucion de LISP
                else{
                    System.out.println("La función de atom tiene erroes de sintaxis");
                }
            }
            else if(instruccion.contains("defun")){//Crea una funcion
                Defun newFunc = new Defun(instruccion.get(1).toString(), instruccion.get(2), instruccion.get(3));
                deFun.add(newFunc);//Agrega la funcion al array de funciones

            } else if (instruccion.contains("list")){//Devuelve una lista con los valores ingresados
                List<Object> list = new functionEvaluation().toList(instruccion.subList(1, instruccion.size()));
                System.out.println(list);
            } else if (instruccion.contains("equal")){//Compara si dos valores son iguales
                if( (new functionEvaluation()).isEqual(instruccion.get(1), instruccion.get(2))){
                    System.out.print("\n\t\tResultado: True\n\n");
                }
                else{
                    System.out.print("\n\t\tResultado: NIL\n\n");
                }
            } else if (instruccion.contains(">")){//Verifica si un valor A es mayor que B
                if( (new functionEvaluation()).isGreaterThan(instruccion.get(1), instruccion.get(2))){
                    System.out.print("\n\t\tResultado: True\n\n");
                }
                else{
                    System.out.print("\n\t\tResultado: NIL\n\n");
                }
            } else if (instruccion.contains("<")){//Verifica si un valor A es menor que B
                if( (new functionEvaluation()).isLessThan(instruccion.get(1), instruccion.get(2))){
                    System.out.print("\n\t\tResultado: True\n\n");
                }
                else{
                    System.out.print("\n\t\tResultado: NIL\n\n");
                }
            }
            else if (instruccion.contains("+") || instruccion.contains("-") || instruccion.contains("*") || instruccion.contains("/")){
                ArithmeticCalculator calculator = new ArithmeticCalculator();
                System.out.println("\n\t\tResultado: " + calculator.calculate(instruccion));
                //Despliegue temporal del parseo de las instrucciones
            } else {//Si no es ninguno de los casos anteriores revisa dentro de un array que contiene todas las definiciones de funciones
                for (Defun fun: deFun) {
                    if (instruccion.contains(fun.getFunName())){
                        fun.executeInstructions(instruccion.subList(1, instruccion.size()));
                    }
                }
            }
        
        }
        catch(Exception e){
            System.out.println("\n\n\tOcurrio un problema al evaluar la expreción. \n\tError: " + e.toString());
        }
    }
}
