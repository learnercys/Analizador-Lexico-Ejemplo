/**
* Editor que realiza un análisis léxico sobre un texto 'fuente'
* Notas: Se omitio el método initComponents y main que generá netbeans debido a 
* que el diseño puede variar. Además esto lo puedes hacer ;D.
* Los valores necesarios son: 
*   TextArea 'texto' que contendrá el texto fuente.
*   TextArea 'destino' que mostrará los resultados del análisis.
*   Button que define el inicio del proceso.
*/

import java.util.ArrayList;

/**
 *
 * @author carlos
 */
public class Editor extends javax.swing.JFrame {
    // Estado actual.
    private int estado = 0;
    // Posición actual con respecto al editor.
    private int posicion = 0;
    // Representa el texto fuente del análisis.
    private String fuente = "";
    // Se analizará carácter por carácter el String fuente. 'caracter'
    // representará el simbolo.
    private char caracter;
    // Es el carácter o conjunto de caracteres que representan una 
    // acción, atributo, etc. en el lenguaje.
    private String lexema = "";
    // Lista completa de lexemas encontradas en el String fuente.
    private ArrayList<String> listaLexema = new ArrayList();
    // Indica que tipo de lexema se encuentra en la lista lexema.
    // los atributos estan ordenados: 
    // listaLexema(i) es de tipo listaToken(i)
    private ArrayList<String> listaToken = new ArrayList();
  
    public Editor() {
        destino.setEditable(false);
    }

    /**
    * Inicia el proceso de análisis. Inicia los valores 
    * que corresponden a cada atributo.
    * texto: Representa el TextArea en el que se encuentra el texto fuente.
    */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        estado = 0;
        posicion = 0;
        lexema = "";
        listaLexema.clear();
        listaToken.clear();
        fuente = texto.getText();
        fuente = fuente.trim();
        if(fuente.length() == 0){
        	destino.setText("El cuadro de entrada no contiene\ncaracteres a"
                + " evaluar. ");
    	}
        else{
            iniciarProceso();
            imprimirLista();
        }
    }
    
    /**
     * Método principal del proceso de verificación de tokens, 
     * cambia de estados dependiendo del caracter encontrado, 
     * en el estado cero no cambia de estado con los caracteres +, =, ; 
     * los toma como signos de aceptación (unicamente los agrega al listado
     * de lexemas). El cambio de estado se realiza cuando en el estado cero 
     * aparecen los caracteres número o de 'a' to 'z'.
     */
    private void iniciarProceso(){
        caracter = fuente.charAt(posicion);
        switch(estado){
            case 0:{
            	/**
            	* En este estado el lexema actual esta vacío. Si se obtiene 
            	* como carácter actual ';', '+' o '=' no cambia de estado debido
            	* a que estos simbolos son de aceptación, entonces, se agregan a la 
            	* lista de lexemas, y se vuelve a iniciar el proceso. Si obtiene
            	* un caracter de tipo vacío no realiza ningún cambio de estados,
            	* solo reinicia el proceso. para los dígitos o letras.
            	* Se realiza el cambio de estado que corresponde con el automata.
            	*/
                if(caracter ==';'){
                    lexema += Character.toString(caracter);
                    addList(lexema,"punto y coma");
                    lexema = "";
                }
                else if(caracter == '+'){
                    lexema += Character.toString(caracter);
                    addList(lexema,"mas");
                    lexema = "";
                }
                else if(caracter == '='){
                    lexema += Character.toString(caracter);
                    addList(lexema,"igual");
                    lexema = "";
                }
                else if(Character.isDigit(caracter)){
                    estado = 5;
                    lexema += Character.toString(caracter);
                }
                else if(Character.isLetter(caracter)){
                    estado = 1;
                    lexema += Character.toString(caracter);
                }
                else if(esEspacio(caracter)){}
                else{error();}
                break;
            }
            case 1:{
            	/**
		* Estado 1, inicia cuando se encuentra una letra.
            	*/
                if(caracter == ';'){
                    addList(lexema,"identificador");
                    addList(";","punto y coma");
                    estado = 0;
                    lexema = "";
                }
                else if(caracter == '='){
                    addList(lexema,"identificador");
                    addList("=","igual");
                    estado = 0;
                    lexema = "";
                }
                else if(caracter == '+'){
                    addList(lexema,"identificador");
                    addList("+","mas");
                    estado = 0;
                    lexema = "";
                }
                else if(esEspacio(caracter)){
                    addList(lexema,"identificador");
                    estado = 0;
                    lexema = "";
                }
                else if(Character.isDigit(caracter)||Character.isLetter(caracter))
                {
                    lexema += Character.toString(caracter);
                }
                else{error();}
                imprimir();
                break;
            }
            case 5:{
            	/**
            	* Estado 5, inicia cuando se encuentra un digito.
            	*/
                if(caracter == ';'){
                    addList(lexema,"numero");
                    addList(";","punto y coma");
                    lexema = "";
                    estado = 0;
                }
                else if(caracter == '='){
                    addList(lexema,"numero");
                    addList("=","igual");
                    lexema = "";
                    estado = 0;
                }
                else if(caracter == '+'){
                    addList(lexema,"numero");
                    addList("+","mas");
                    lexema = "";
                    estado = 0;
                }
                else if(esEspacio(caracter)){
                    addList(lexema,"numero");
                    lexema = "";
                    estado = 0;
                }
                else if(Character.isDigit(caracter))
                { 
                    lexema += Character.toString(caracter);
                }
                else {
                    error();
                }
                break;
            }
            default:
                break;
        }
        /**
	* Al finalizar el análisis con el carácter actual se toma la
	* posición siguiente y se repite el análisis hasta llegar al punto final 
	* del String fuente.
        */
        posicion++;
        imprimir();
        if (posicion >= fuente.length()){
            if(estado == 1){
                addList(lexema,"identificador");
            }
            else if(estado == 5){
                addList(lexema,"numero");
            }
        }
        else{
            iniciarProceso();
        }
    }
    /**
     * Llamado cuando se encuentra un error en la entrada. Se llama el mismo
     * hasta que encuentra un caracter limitador.
     * Los caracteres limitadores son: + = ; o espacio.
     */
    private void error(){
        lexema += Character.toString(caracter);
        posicion++;
        if(posicion >= fuente.length()){
            estado = 0;
            addList(lexema,"error");
        }else{
            caracter = fuente.charAt(posicion);
            if(caracter == '='){
                addList(lexema,"error");
                addList("=","igual");
                estado = 0;
                lexema = "";
            }
            else if(caracter == '+'){
                addList(lexema,"error");
                addList("+","suma");
                estado = 0;
                lexema = "";
            }
            else if(caracter == ';'){
                addList(lexema,"error");
                addList(";","punto y coma");
                estado = 0;
                lexema = "";
            }
            else if(esEspacio(caracter)){
                addList(lexema,"error");
                estado = 0;
                lexema = "";
            }
            else{error();}
        }
    }
    /**
     * 
     * @param c Agregarle char c es una total tontería porque siempre se analizará 
     * caracter( pero bueno =D).
     * @return true si el caracter actual es espacio, tabulador o cambio de linea.
     * También se pueden utilizar métodos de la clase Character para verificar si el 
     * carácter actual pertecence a los tipos buscados( tabulación, espacio o salto
     * de línea.)
     */
    private boolean esEspacio(char c){
        boolean flag = false;
        if(c == '\n' || c == '\t' || c == ' '){flag = true;}
        return flag;
    }
    /**
    * destino: representa el textArea en el que se mostrarán los resultados obtenidos.
    * Imprime en el textArea 'destino' la lista de tokens y sus lexemas.
    */
    private void imprimirLista(){
        String auxiliar = "Token    -------   Lexema\n";
        for(int i = 0; i < listaLexema.size(); i++){
            auxiliar += listaToken.get(i) + "  -------  " + listaLexema.get(i) + "\n";
        }
        destino.setText(auxiliar);
    }
    /**
     * imprime en consola el estado, caracter, lexema y posicion actual.
     */
    private void imprimir(){
        System.out.println("estado:" + estado + " caracter:" + caracter + " lexema:" + lexema + " posicion:" + posicion );
    }
    /**
     * 
     * @param lex
     * @param token 
     * agrega lex y token a la lista de lexemas y tokens.
     */
    private void addList(String lex, String token){
        listaLexema.add(lex);
        listaToken.add(token);
    }
    private javax.swing.JTextArea destino;
    private javax.swing.JButton jButton1;
    private javax.swing.JTextArea texto;

    // pd. cualquier HORROR me lo hacen saber =D.
}
