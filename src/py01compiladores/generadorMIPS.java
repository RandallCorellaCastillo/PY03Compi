/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package py01compiladores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
/**
 *
 * @author truez
 */
public class generadorMIPS {
    
    private String ruta;
    private List<String> lineas;
    private String result;

    public generadorMIPS(String ruta) {
        this.ruta = ruta;
        this.lineas = new ArrayList<>();
        this.result = ".data";
        leerYDividirArchivo();
    }

    private void leerYDividirArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public List<String> getLineas() {
        return lineas;
    }
    
    public void dataGeneratorVariables(){
         for (String linea : lineas) {
            String[] partes_ = linea.split("_");
            //COndiciones para guardar los datos en el segmento de data.
            boolean cond = (partes_[0].equals("global") || partes_[0].equals("local"));
            if(cond){
                String nombreVariable = partes_[2].split(" ")[1];
                String tipoDato = partes_[2].split(" ")[0];
                String mipsCode = "";
                if (tipoDato.equals("String")) { mipsCode = nombreVariable + ": .asciiz \"\""; } 
                else if (tipoDato.equals("int")) {mipsCode = nombreVariable + ": .word 0";} 
                else if (tipoDato.equals("bool")) {mipsCode = nombreVariable + ": .word 0";}
                else if (tipoDato.equals("float")) {mipsCode = nombreVariable + ": .float 0.0";}
                else {mipsCode = nombreVariable + ": .word 0";}
                result += "\n" + mipsCode;
            }
        }
        result += "\nflag : .word 0" + "\ncont : .word 0" + "\ncant : .word 0" + "\njump : .word 0" + "\ninit : .word 0";
    }
    
    public void textGenerator(){
         result += "\n.text" + "\n.globl main" + "\nmain:";
         int cont = 1;
         for (String linea : lineas) {
            String[] partes = linea.split(" ");
            String[] partes_ = linea.split("_");
            String[] partesEquals = linea.split("=");
            if(!partes[0].equals("")) { //En caso de no ser una linea vacia.
                //System.out.println("Linea numero: " + cont++ + " text: " + partes[0]);
                String etiqueta = partesEquals[0];
                if(partes_[0].equals("begin") || partes_[0].equals("end")){ result+= "\n\n" + partes[0] + ":";} //En caso de ser una declaracion de funcion.
                
                if (linea.contains("=")) {
                    result += "\naddi $sp, $sp, -4";
                    if(etiqueta.matches("^\\s*[t][0-9]+\\s*$")) { //en caso de ser una t.

                        String value = partesEquals[1].trim();
                        if(value.matches("^'[^']*'$")) { //En caso de ser una declaracion de string.
                            System.out.println("String Asignacion: " + linea);
                        } else {
                            //Logica principal.
                            String almacenarEntero = "";
                            String input = partesEquals[1].trim(); 
                            String patternNumber = "\\s*-?\\d+(\\.\\d+)?\\s*"; //Regex un numero entero.
                            boolean isNumber = input.matches(patternNumber);
                            if (isNumber) {
                                almacenarEntero += "\nli $v0, " + input;
                                almacenarEntero += "\nsw $v0, 0($sp)";
                                result += almacenarEntero;
                            }
                            String patternOper = "\\b\\w+\\d+\\s*([-+*/])\\s*\\w+\\d+\\b"; // Regex para el operando.
                            boolean isOper = input.matches(patternOper);
                            String almacenarOperacion = "";
                            if(isOper) {
                                System.out.println("oper: " + linea);
                                String[] listOper = input.split("(?<=[\\+\\-\\*/])|(?=[\\+\\-\\*/])");
                                int pos1 = Integer.parseInt(listOper[0].replaceAll("\\D", ""));
                                int pos2 = Integer.parseInt(listOper[2].replaceAll("\\D", ""));
                                pos1 = pos1 * 4;
                                pos2 = pos2 * 4;
                                //
                                almacenarOperacion += "\nlw $t0, " + pos1 + "($sp) ";
                                almacenarOperacion += "\nlw $t1, " + pos2 + "($sp) ";

                                if(listOper[1].equals("+")) {
                                    almacenarOperacion += "\nadd $t2, $t0, $t1";
                                    almacenarOperacion += "\nsw $t2, ($sp)";
                                }
                                if(listOper[1].equals("-")) {
                                    almacenarOperacion += "\nsub $t2, $t0, $t1";
                                    almacenarOperacion += "\nsw $t2, ($sp)";
                                }
                                if(listOper[1].equals("*")) {
                                    almacenarOperacion += "\nmul $t2, $t0, $t1";
                                    almacenarOperacion += "\nsw $t2, ($sp)";
                                }
                                if(listOper[1].equals("/")) {
                                    almacenarOperacion += "\ndiv $t0, $t1";
                                    almacenarOperacion += "\nmflo $t2";
                                    almacenarOperacion += "\nsw $t2, ($sp)";
                                }
                                result += almacenarOperacion;
                            }
                        }
                        
                    } else if(etiqueta.matches("^\\s*[f][0-9]+\\s*$")) { //en caso de ser una f.

                        System.out.println("flotantes: " + linea);

                    } else { //cualquier otra cosa.
                        System.out.println("Asignaciones: " +  linea);
                    }
                    //EN caso de las asignaciones en MIPS.
                }
            }
        }
    }
    
        public void macrosGenerator(){
            result += "\n\n---------------------Macros---------------------\n";
            //macro de immprimirEtiquetas.
            result += "\n#entrada: $a0, debe ser una etiqueta.\n" +
                      "#salida: $a0.\n" +
                      "print:\n" +
                      "li $v0, 4          # Cargar el valor 4 en $v0 para la llamada al sistema para imprimir un string\n" +
                      "syscall            # Llamar al sistema para imprimir un string\n" +
                      "jr $ra             # Retornar al registro de dirección de retorno";
            //macro de imprimirEnteros
            result+= "\n\n#entrada: $t0, debe ser un numero entero.\n" +
                     "#salida: $a0\n" +
                     "print_entero:\n" +
                     "li $v0, 1         # Syscall para imprimir un entero\n" +
                     "move $a0, $t0     # Mueve el número a imprimir a $a0, entrada $t0\n" +
                     "syscall\n" +
                     "jr $ra ";
    }
    
    public void textTostring() {
        System.out.println(result);
    }
}
