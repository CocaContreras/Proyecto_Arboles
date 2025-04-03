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
    int altura;

    public Nodo(int valor) {
        this.valor = valor;
        this.altura = 1;
        izquierda = derecha = null;
    }
}

class ArbolAVL {
    Nodo raiz;
    List<Integer> recorrido;

    public ArbolAVL() {
        raiz = null;
        recorrido = new ArrayList<>();
    }

    // Métodos de altura y balance
    private int altura(Nodo n) {
        return n == null ? 0 : n.altura;
    }

    private int getBalance(Nodo n) {
        return n == null ? 0 : altura(n.izquierda) - altura(n.derecha);
    }

    private void actualizarAltura(Nodo n) {
        if (n != null) {
            n.altura = 1 + Math.max(altura(n.izquierda), altura(n.derecha));
        }
    }

    // Rotaciones AVL
    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izquierda;
        Nodo T2 = x.derecha;

        x.derecha = y;
        y.izquierda = T2;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecha;
        Nodo T2 = y.izquierda;

        y.izquierda = x;
        x.derecha = T2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    // Inserción AVL
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
        } else {
            return nodo; // No duplicados
        }

        actualizarAltura(nodo);
        return balancear(nodo, valor);
    }

    // Eliminación AVL
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
            if (nodo.izquierda == null || nodo.derecha == null) {
                Nodo temp = (nodo.izquierda != null) ? nodo.izquierda : nodo.derecha;
                if (temp == null) {
                    temp = nodo;
                    nodo = null;
                } else {
                    nodo = temp;
                }
            } else {
                Nodo temp = encontrarMinimo(nodo.derecha);
                nodo.valor = temp.valor;
                nodo.derecha = eliminarNodo(nodo.derecha, temp.valor);
            }
        }

        if (nodo == null) {
            return null;
        }

        actualizarAltura(nodo);
        return balancear(nodo, nodo.valor);
    }

    private Nodo balancear(Nodo nodo, int valor) {
        int balance = getBalance(nodo);

        // Caso izquierda-izquierda
        if (balance > 1 && valor < nodo.izquierda.valor) {
            return rotarDerecha(nodo);
        }

        // Caso derecha-derecha
        if (balance < -1 && valor > nodo.derecha.valor) {
            return rotarIzquierda(nodo);
        }

        // Caso izquierda-derecha
        if (balance > 1 && valor > nodo.izquierda.valor) {
            nodo.izquierda = rotarIzquierda(nodo.izquierda);
            return rotarDerecha(nodo);
        }

        // Caso derecha-izquierda
        if (balance < -1 && valor < nodo.derecha.valor) {
            nodo.derecha = rotarDerecha(nodo.derecha);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    // Métodos de recorrido (se mantienen igual)
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

    private Nodo encontrarMinimo(Nodo nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }
        return nodo;
    }

    public int buscarAltura(int valor) {
        return buscarAlturaRecursivo(raiz, valor, 0);
    }

    private int buscarAlturaRecursivo(Nodo nodo, int valor, int altura) {
        if (nodo == null) {
            return -1;
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
    private final ArbolAVL arbol;
    private final JPanel panelDibujo;
    private final JLabel labelRecorrido;

    public Proyecto_Arbol() {
        arbol = new ArbolAVL();
        
        setTitle("Árbol AVL");
        setSize(800, 600);
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
            guardarEnArchivo(resultado, "resultados_busqueda.txt");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
        }
    }

    private void guardarEnArchivo(String contenido, String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            bw.write(contenido);
            bw.newLine();
            File archivo = new File(nombreArchivo);
            System.out.println("Archivo guardado en: " + archivo.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Resultado guardado en el archivo: " + nombreArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage());
        }
    }

    private void dibujarArbol(Graphics g, int x, int y, Nodo nodo, int espacio) {
        if (nodo == null) return;
        
        // Dibujar nodo con información de altura y balance
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(nodo.valor), x - 5, y + 5);
        
        // Mostrar altura y factor de balance (opcional)
        g.setColor(Color.BLUE);
        g.drawString("h:" + nodo.altura, x - 15, y + 25);
        
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
        SwingUtilities.invokeLater(() -> {
            Proyecto_Arbol frame = new Proyecto_Arbol();
            frame.setVisible(true);
        });
    }
}