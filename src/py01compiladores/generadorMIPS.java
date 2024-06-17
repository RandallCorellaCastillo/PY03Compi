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
    private int Tcant;

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
    
    public void textGenerator() {
        result += "\n.text" + "\n.globl main" + "\nmain:";
        for (String linea : lineas) {
            String[] partes = linea.split(" ");
            String[] partes_ = linea.split("_");
            String[] partesEquals = linea.split("=");
            if (!partes[0].equals("")) { // En caso de no ser una línea vacía.
                String etiqueta = partesEquals[0];
                result += etiquetaText(partes_, partes);     
                result += asignacionText(linea, etiqueta, partesEquals);
                result += gotoText(partes);       
            }
        }
    }

    public String gotoText(String[] partes){
        String texto = "";
        if (partes[0].equals("goto")) {
            if (partes.length > 1) {
                String label = partes[1];
                texto += "\nj " + label;
            }
            return texto;
        } else {
            return texto;
        }
    }

    public String etiquetaText(String[] partes_,String[] partes){
        String texto = "";  
        if (partes_[0].equals("begin") || partes_[0].equals("end")) {
            texto += "\n\n" + partes[0];
        }
        return texto;
    }

    public String asignacionText(String linea, String etiqueta, String[] partesEquals){
        String texto = "";  
        if (linea.contains("=")) {
            if (etiqueta.matches("^\\s*[t][0-9]+\\s*$")) {
                String value = partesEquals[1].trim();
                if (value.matches("^'[^']*'$")) { // En caso de ser una declaración de string.
                    System.out.println("String Asignacion: " + linea);
                } else {
                    String almacenarEntero = "";
                    String input = partesEquals[1].trim();
                    String patternNumber = "\\s*-?\\d+\\s*"; // Regex un número entero.
                    boolean isNumber = input.matches(patternNumber);
                    if (isNumber) {
                        texto += "\naddi $sp, $sp, -4";
                        almacenarEntero += "\nli $v0, " + input;
                        almacenarEntero += "\nsw $v0, 0($sp)";
                        texto += almacenarEntero;
                    }
                    String patternOper = "\\b\\w+\\d+\\s*([-+*/])\\s*\\w+\\d+\\b"; // Regex para el operando.
                    boolean isOper = input.matches(patternOper);
                    String almacenarOperacion = "";
                    if (isOper) {
                        texto += "\naddi $sp, $sp, -4";
                        String[] listOper = input.split("(?<=[\\+\\-\\*/])|(?=[\\+\\-\\*/])");
                        int pos1 = Integer.parseInt(listOper[0].replaceAll("\\D", ""));
                        int pos2 = Integer.parseInt(listOper[2].replaceAll("\\D", ""));
                        pos1 = pos1 * 4;
                        pos2 = pos2 * 4;
                        //
                        almacenarOperacion += "\nlw $t0, " + pos1 + "($sp) ";
                        almacenarOperacion += "\nlw $t1, " + pos2 + "($sp) ";

                        if (listOper[1].equals("+")) {
                            almacenarOperacion += "\nadd $t2, $t0, $t1";
                            almacenarOperacion += "\nsw $t2, 0($sp)";
                        }
                        if (listOper[1].equals("-")) {
                            almacenarOperacion += "\nsub $t2, $t0, $t1";
                            almacenarOperacion += "\nsw $t2, 0($sp)";
                        }
                        if (listOper[1].equals("*")) {
                            almacenarOperacion += "\nmul $t2, $t0, $t1";
                            almacenarOperacion += "\nsw $t2, 0($sp)";
                        }
                        if (listOper[1].equals("/")) {
                            almacenarOperacion += "\ndiv $t0, $t1";
                            almacenarOperacion += "\nmflo $t2";
                            almacenarOperacion += "\nsw $t2, 0($sp)";
                        }
                        texto += almacenarOperacion;
                    }
                }
            }
        }
        return texto;
    }

    public String comparacionesIF(String[] partes){
        String texto = "";  
        if (partes[0].equals("if")) { // Manejar condicionales (if)
            if (partes.length == 4 && partes[2].equals("goto")) { // Caso `if t2 goto label`
                String var = partes[1];
                String label = partes[3];
                int pos = Integer.parseInt(var.replaceAll("\\D", "")) * 4;
                result += "\nlw $t0, " + pos + "($sp)";
                result += "\nbnez $t0, " + label; // Saltar si no es cero
            } else if (partes.length > 2 && partes[2].startsWith("goto")) { // Caso `if (t1 == t2) goto label`
                String condition = partes[1].substring(1, partes[1].length() - 1);
                String[] condParts = condition.split(" ");
                if (condParts.length == 3) {
                    String var1 = condParts[0];
                    String operator = condParts[1];
                    String var2 = condParts[2];
                    String label = partes[3]; // La etiqueta a saltar

                    int pos1 = Integer.parseInt(var1.replaceAll("\\D", ""));
                    int pos2 = Integer.parseInt(var2.replaceAll("\\D", ""));

                    pos1 = pos1 * 4;
                    pos2 = pos2 * 4;

                    texto += "\nlw $t0, " + pos1 + "($sp)";
                    texto += "\nlw $t1, " + pos2 + "($sp)";

                    switch (operator) {
                        case "==":
                            result += "\nbeq $t0, $t1, " + label;
                            break;
                        case "!=":
                            result += "\nbne $t0, $t1, " + label;
                            break;
                        case "<":
                            result += "\nblt $t0, $t1, " + label;
                            break;
                        case "<=":
                            result += "\nble $t0, $t1, " + label;
                            break;
                        case ">":
                            result += "\nbgt $t0, $t1, " + label;
                            break;
                        case ">=":
                            result += "\nbge $t0, $t1, " + label;
                            break;
                    }
                }
            }
        }
        return texto;
    }

    private String generateReadCode(String tipoDato, int pos) {
        StringBuilder code = new StringBuilder();
        switch (tipoDato) {
            case "String":
                code.append("\nli $v0, 8"); // Syscall para leer una cadena
                code.append("\nla $a0, ").append(pos); // Dirección de la cadena en la pila
                code.append("\nli $a1, 256"); // Tamaño máximo de la cadena
                code.append("\nsyscall");
                break;
            case "int":
                code.append("\nli $v0, 5"); // Syscall para leer un entero
                code.append("\nsyscall");
                code.append("\nsw $v0, ").append(pos).append("($sp)");
                break;
            case "float":
                code.append("\nli $v0, 6"); // Syscall para leer un flotante
                code.append("\nsyscall");
                code.append("\nswc1 $f0, ").append(pos).append("($sp)");
                break;
            case "char":
                code.append("\nli $v0, 12"); // Syscall para leer un carácter
                code.append("\nsyscall");
                code.append("\nsb $v0, ").append(pos).append("($sp)");
                break;
            default:
                code.append("\n# Tipo de dato no soportado para read: ").append(tipoDato);
                break;
        }
        return code.toString();
    }

    private String generatePrintCode(String tipoDato, int pos) {
        StringBuilder code = new StringBuilder();
        switch (tipoDato) {
            case "String":
                code.append("\nli $v0, 4"); // Syscall para imprimir una cadena
                code.append("\nla $a0, ").append(pos); // Dirección de la cadena en la pila
                code.append("\nsyscall");
                break;
            case "int":
                code.append("\nlw $t0, ").append(pos).append("($sp)");
                code.append("\nli $v0, 1"); // Syscall para imprimir un entero
                code.append("\nmove $a0, $t0");
                code.append("\nsyscall");
                break;
            case "float":
                code.append("\nlwc1 $f12, ").append(pos).append("($sp)");
                code.append("\nli $v0, 2"); // Syscall para imprimir un flotante
                code.append("\nsyscall");
                break;
            case "char":
                code.append("\nlb $t0, ").append(pos).append("($sp)");
                code.append("\nli $v0, 11"); // Syscall para imprimir un carácter
                code.append("\nmove $a0, $t0");
                code.append("\nsyscall");
                break;
            default:
                code.append("\n# Tipo de dato no soportado para print: ").append(tipoDato);
                break;
        }
        return code.toString();
    }

    public String getResult() {
        return result.toString();
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
        result += "\n\n#entrada: $t0, debe ser un numero entero.\n" +
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
