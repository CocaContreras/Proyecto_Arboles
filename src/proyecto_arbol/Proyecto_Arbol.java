package proyecto_arbol;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


class Nodo {
    int valor;
    Nodo izquierda, derecha;

    public Nodo(int valor) {
        this.valor = valor;
        izquierda = derecha = null;
    }
}

class ArbolBinario {
    Nodo raiz;
    List<Integer> recorrido;

    public ArbolBinario() {
        raiz = null;
        recorrido = new ArrayList<>();
    }

    public void insertar(int valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    private Nodo insertarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) {
            return new Nodo(valor);
        }
        if (valor < nodo.valor) {
            nodo.izquierda = insertarRecursivo(nodo.izquierda, valor);
        } else if (valor > nodo.valor) {
            nodo.derecha = insertarRecursivo(nodo.derecha, valor);
        }
        return nodo;
    }

    public List<Integer> recorridoInorden() {
        recorrido.clear();
        inorden(raiz);
        return recorrido;
    }

    private void inorden(Nodo nodo) {
        if (nodo != null) {
            inorden(nodo.izquierda);
            recorrido.add(nodo.valor);
            inorden(nodo.derecha);
        }
    }

    public List<Integer> recorridoPreorden() {
        recorrido.clear();
        preorden(raiz);
        return recorrido;
    }

    private void preorden(Nodo nodo) {
        if (nodo != null) {
            recorrido.add(nodo.valor);
            preorden(nodo.izquierda);
            preorden(nodo.derecha);
        }
    }

    public List<Integer> recorridoPostorden() {
        recorrido.clear();
        postorden(raiz);
        return recorrido;
    }

    private void postorden(Nodo nodo) {
        if (nodo != null) {
            postorden(nodo.izquierda);
            postorden(nodo.derecha);
            recorrido.add(nodo.valor);
        }
    }

    public void eliminar(int valor) {
        raiz = eliminarNodo(raiz, valor);
    }

    private Nodo eliminarNodo(Nodo nodo, int valor) {
        if (nodo == null) {
            return null;
        }
        if (valor < nodo.valor) {
            nodo.izquierda = eliminarNodo(nodo.izquierda, valor);
        } else if (valor > nodo.valor) {
            nodo.derecha = eliminarNodo(nodo.derecha, valor);
        } else {
            if (nodo.izquierda == null) return nodo.derecha;
            if (nodo.derecha == null) return nodo.izquierda;

            nodo.valor = encontrarMinimo(nodo.derecha);
            nodo.derecha = eliminarNodo(nodo.derecha, nodo.valor);
        }
        return nodo;
    }
    

    private int encontrarMinimo(Nodo nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }
        return nodo.valor;
    }
    public int buscarAltura(int valor) {
    return buscarAlturaRecursivo(raiz, valor, 0);
}

private int buscarAlturaRecursivo(Nodo nodo, int valor, int altura) {
    if (nodo == null) {
        return -1; // Nodo no encontrado
    }
    if (nodo.valor == valor) {
        return altura;
    }
    if (valor < nodo.valor) {
        return buscarAlturaRecursivo(nodo.izquierda, valor, altura + 1);
    } else {
        return buscarAlturaRecursivo(nodo.derecha, valor, altura + 1);
    }
  }
}

public class Proyecto_Arbol extends JFrame {
    private final ArbolBinario arbol;
    private final JPanel panelDibujo;
    private final JLabel labelRecorrido;

    public Proyecto_Arbol() {
        arbol = new ArbolBinario();
        
        setTitle("Árbol Binario");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        JButton btnCargarArchivo = new JButton("Cargar archivo");
        JButton btnInorden = new JButton("Inorden");
        JButton btnPreorden = new JButton("Preorden");
        JButton btnPostorden = new JButton("Postorden");
        JButton btnAgregar = new JButton("Agregar Nodo");
        JButton btnEliminar = new JButton("Eliminar Nodo");
        JButton btnBuscar = new JButton("Buscar Nodo");

        panelBotones.add(btnCargarArchivo);
        panelBotones.add(btnInorden);
        panelBotones.add(btnPreorden);
        panelBotones.add(btnPostorden);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        
        add(panelBotones, BorderLayout.NORTH);
        
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarArbol(g, getWidth() / 2, 50, arbol.raiz, getWidth() / 4);
            }
        };
        add(panelDibujo, BorderLayout.CENTER);
        
        labelRecorrido = new JLabel("Recorrido: ");
        add(labelRecorrido, BorderLayout.SOUTH);
        
        btnCargarArchivo.addActionListener(e -> cargarDesdeArchivo());
        btnInorden.addActionListener(e -> actualizarRecorrido(arbol.recorridoInorden()));
        btnPreorden.addActionListener(e -> actualizarRecorrido(arbol.recorridoPreorden()));
        btnPostorden.addActionListener(e -> actualizarRecorrido(arbol.recorridoPostorden()));
        btnAgregar.addActionListener(e -> agregarNodo());
        btnEliminar.addActionListener(e -> eliminarNodo());
        btnBuscar.addActionListener(e -> buscarNodo());

    }

    private void actualizarRecorrido(List<Integer> recorrido) {
        labelRecorrido.setText("Recorrido: " + recorrido.toString());
        repaint();
    }

    private void cargarDesdeArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim();
                    if (!linea.isEmpty()) {
                        String[] valores = linea.split("\\s+"); 
                        for (String valorStr : valores) {
                            try {
                                int valor = Integer.parseInt(valorStr);
                                arbol.insertar(valor);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Formato incorrecto en el archivo: " + valorStr);
                            }
                        }
                    }
                }
                repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo");
            }
        }
    }

    private void agregarNodo() {
        String valorStr = JOptionPane.showInputDialog("Ingrese el valor del nodo:");
        try {
            int valor = Integer.parseInt(valorStr);
            arbol.insertar(valor);
            repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
        }
    }

    private void eliminarNodo() {
        String valorStr = JOptionPane.showInputDialog("Ingrese el valor a eliminar:");
        try {
            int valor = Integer.parseInt(valorStr);
            arbol.eliminar(valor);
            repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
        }
    }
    private void buscarNodo() {
    String valorStr = JOptionPane.showInputDialog("Ingrese el valor a buscar:");
    try {
        int valor = Integer.parseInt(valorStr);
        int altura = arbol.buscarAltura(valor);
        String resultado;
        if (altura == -1) {
            resultado = "Nodo " + valor + " no encontrado en el árbol.";
            JOptionPane.showMessageDialog(this, resultado);
        } else {
            resultado = "Nodo " + valor + " encontrado en la altura: " + altura;
            JOptionPane.showMessageDialog(this, resultado);
        }
        // Guardar el resultado en un archivo
        guardarEnArchivo(resultado, "resultados_busqueda.txt");
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
    }
}

   private void guardarEnArchivo(String contenido, String nombreArchivo) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo, true))) { // "true" para agregar contenido
        bw.write(contenido);
        bw.newLine(); // Agrega una nueva línea al final

        // Imprimir la ruta del archivo en consola
        File archivo = new File(nombreArchivo);
        System.out.println("Archivo guardado en: " + archivo.getAbsolutePath());

        JOptionPane.showMessageDialog(this, "Resultado guardado en el archivo: " + nombreArchivo);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage());
    }
}



    
    private void dibujarArbol(Graphics g, int x, int y, Nodo nodo, int espacio) {
        if (nodo == null) return;
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(nodo.valor), x - 5, y + 5);
        
        if (nodo.izquierda != null) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, x - espacio, y + 50);
            dibujarArbol(g, x - espacio, y + 50, nodo.izquierda, espacio / 2);
        }
        if (nodo.derecha != null) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, x + espacio, y + 50);
            dibujarArbol(g, x + espacio, y + 50, nodo.derecha, espacio / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Proyecto_Arbol().setVisible(true));
    }
}
    