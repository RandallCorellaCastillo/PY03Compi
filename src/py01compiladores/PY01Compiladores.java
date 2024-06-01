/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package py01compiladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.time.Clock.system;
import java_cup.parser;
import java_cup.runtime.Symbol;
import jflex.exceptions.SilentExit;
import static jflex.logging.Out.println;

import java.util.HashMap; // import the HashMap class
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author truez
 */
public class PY01Compiladores {
    /**
     * @param args the command line arguments
     * @throws jflex.exceptions.SilentExit
     * @throws java.lang.Exception
     */
    HashMap<String, List<String>> TablaSimbolos = new HashMap<>(); 
     
    public static void generarLexer(String path) throws SilentExit {
        String[] strArr =  {path};
        jflex.Main.generate(strArr);
    }
    
    public static void generarParser(String path) throws IOException, Exception {
        String[] strArr = { "-parser", "Parser", path };
        java_cup.Main.main(strArr);
    }

    public static String LexerToString(String ruta) throws FileNotFoundException, IOException, Exception {
        try (Reader reader = new BufferedReader(new FileReader(ruta))) {
            Lexer lex = new Lexer(reader);
            int i = 0;
            Symbol token;
            String document = "";
            while (true) {
                token = lex.next_token();
                if (token.sym != 0) {
                    document += "Token: " + token.sym + " " + (token.value == null ? lex.yytext() : token.value.toString()) + "\n";
                } else {
                    document += "Cantidad de lexemas encontrados: " + i;
                    return document;
                }
                i++;
            }
        }
    }

    
    public static void runParser(String ruta) throws IOException, Exception {
        try (Reader reader = new BufferedReader(new FileReader(ruta))) {
            Lexer lex = new Lexer(reader);  // Crea un analizador léxico para el archivo
            Parser myParser = new Parser(lex);  // Crea un analizador sintáctico y le pasa el analizador léxico
            myParser.parse();  // Parsea el contenido del archivo
        }
    }
    
    public static void saveLexems(String content, String ruta) throws IOException, Exception {
        try {
            // Crear un objeto FileWriter para escribir en el archivo
            FileWriter archivoEscritura = new FileWriter(ruta);
            // Crear un objeto BufferedWriter para escribir texto en el archivo
            BufferedWriter escritor = new BufferedWriter(archivoEscritura);
            // Escribir la cadena en el archivo
            escritor.write(content);
            // Cerrar el BufferedWriter para liberar recursos
            escritor.close();

            System.out.println("Cadena guardada en el archivo correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar la cadena en el archivo: " + e.getMessage());
        }
    }
    public static void saveCod3D(String content, String ruta) throws IOException, Exception {
        try {
            // Crear un objeto FileWriter para escribir en el archivo
            FileWriter archivoEscritura = new FileWriter(ruta);
            // Crear un objeto BufferedWriter para escribir texto en el archivo
            BufferedWriter escritor = new BufferedWriter(archivoEscritura);
            // Escribir la cadena en el archivo
            escritor.write(content);
            // Cerrar el BufferedWriter para liberar recursos
            escritor.close();
            
            System.out.println("Cadena guardada en el archivo correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar la cadena en el archivo: " + e.getMessage());
        }
    }
    
    
    
    public static void main(String[] args) throws SilentExit, Exception {
                
        Path currentPath = Paths.get("");
        Path absolutePath = currentPath.toAbsolutePath();
        String currentDirectory = absolutePath.toString(); 
        
        String path = currentDirectory + "\\src\\py01compiladores\\lexer.flex";
        String path2 = currentDirectory + "\\src\\py01compiladores\\ASint.cup";
        
        String path3 = currentDirectory + "\\src\\py01compiladores\\test.txt";
        
        String symLocation = currentDirectory + "\\sym.java";
        String ParserLocation = currentDirectory + "\\Parser.java";
       
        Path sym1 = Paths.get("").toAbsolutePath();
        String symd1 = sym1.toString();
        symd1 = symd1 + "\\src\\py01compiladores\\sym.java";

        Path par1 = Paths.get("").toAbsolutePath();
        String pard1 = par1.toString();
        pard1 = pard1 + "\\src\\py01compiladores\\Parser.java";
        
        generarLexer(path);
        generarParser(path2);
        //
        Files.move(Paths.get(symLocation), Paths.get(symd1), StandardCopyOption.REPLACE_EXISTING);
        Files.move(Paths.get(ParserLocation), Paths.get(pard1), StandardCopyOption.REPLACE_EXISTING);
        
        
        String lexemas = LexerToString(path3);
        //System.out.print(lexemas);
        String path4 = currentDirectory + "\\src\\py01compiladores\\lexemas.txt";
        saveLexems(lexemas, path4);
        runParser(path3);  
        
    }
}
