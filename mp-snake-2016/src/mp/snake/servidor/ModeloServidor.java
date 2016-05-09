/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.snake.servidor;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pablo
 */
public class ModeloServidor {

    private ArrayList<Jugador> jugadores;
    private int tableroX;
    private int tableroY;
    private int velocidad = 150;
    private Punto tesoro;
    private Punto tesoroTemporal;
    Thread hilo = iniciar();

    public ModeloServidor(int tamX, int tamY) throws IOException {
        jugadores = new ArrayList<>();
        this.tableroX = tamX;
        this.tableroY = tamY;
        Random rnd = new Random();
        this.tesoro = new Punto(rnd.nextInt(tableroX), rnd.nextInt(tableroY));
        this.tesoroTemporal = new Punto();
        hilo.start();

    }

    void addJugador(int id, Socket s) throws IOException {
        Random rnd = new Random();
        Punto punto = new Punto(rnd.nextInt(tableroX), rnd.nextInt(tableroY));
        LinkedList serpiente = new LinkedList();
        serpiente.add(punto);
        Jugador jugador = new Jugador(serpiente, id, s);
        jugador.setDireccion(rnd.nextInt(4));
        jugadores.add(jugador);
        System.out.println(jugadores.size());

    }

    public void connect(int id) throws IOException {
        String cabecera = "IDC";
        String cuerpo = id + ";" + tableroX + ";" + tableroY;
        enviarMensaje(cabecera + ";" + cuerpo);
        pintarTesoro(tesoro.getX(), tesoro.getY(), 1);
    }

    public void end() throws IOException, InterruptedException {
    }

    public void enviarMensaje(String s) throws IOException {
        System.out.println("a clientes:" + s);
        for (Jugador j : jugadores) {
            j.getStreamOut().writeBytes(s + "\n");
            j.getStreamOut().flush();
        }
    }

    public void cambiarDireccion(String token, int id) {
        switch (token) {
            case "ARRIBA":
                arriba(id);
                break;
            case "ABAJO":
                abajo(id);
                break;
            case "IZQUIERDA":
                izquierda(id);
                break;
            case "DERECHA":
                derecha(id);
                break;

        }

    }

    public void arriba(int id) {
        if (jugadores.get(id).getDireccion() != 3) {
            jugadores.get(id).setDireccion(1);
        }

    }

    public void abajo(int id) {
        if (jugadores.get(id).getDireccion() != 1) {
            jugadores.get(id).setDireccion(3);
        }
    }

    public void izquierda(int id) {
        if (jugadores.get(id).getDireccion() != 2) {
            jugadores.get(id).setDireccion(0);
        }
    }

    public void derecha(int id) {
        if (jugadores.get(id).getDireccion() != 0) {
            jugadores.get(id).setDireccion(2);
        }
    }

    private void pintarTesoro(int x, int y, int t) throws IOException {
        String cabecera = "TSR";
        String cuerpo = t + ";" + x + ";" + y;
        enviarMensaje(cabecera + ";" + cuerpo);

    }

    private void addTesoro(int t) throws IOException {
        Random rnd = new Random();
        int x = rnd.nextInt(tableroX);
        int y = rnd.nextInt(tableroY);
        if (t == 1) {
            tesoro = new Punto(x, y);
            pintarTesoro(tesoro.getX(), tesoro.getY(), 1);
        } else {

            tesoroTemporal = new Punto(x, y);
            pintarTesoro(tesoro.getX(), tesoro.getY(), 2);
        }
    }

    public void tesoroComido(int t, int id) throws IOException {
        if (t == 1) {
            //actualizar puntuacion jugador id con puntuacion tesoro tipo 1; 
            addTesoro(1);
        } else {
            //actualizar puntuacion jugador id con puntuacion tesoro tipo 2;    
        }
        jugadores.get(id).getSerpiente().add(new Punto());
    }

    private void gameOver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void posicionToJugadores(int id, int xIni, int yIni, int xFin, int yFin) throws IOException {
        String cabecera = "MOV";
        String cuerpo = id + ";" + xIni + ";" + yIni + ";" + xFin + ";" + yFin;
        enviarMensaje(cabecera + cuerpo);
    }

    public Thread iniciar() {

        return new Thread() {

            @Override
            public void run() {
                int mostrarTesoro = 0;
                while (true) {
                    try {
                        Thread.sleep(velocidad);
                    } catch (InterruptedException e) {

                    }

                    //
                    for (Jugador j : jugadores) {
                        try {
                            actualizarPosicion(j.getIdCliente());
                            isTesoroComido(j.getIdCliente());

                        } catch (Exception e) {

                        }

                    }
                    mostrarTesoro++;
                    if (mostrarTesoro == 50) {
                        try {
                            pintarTesoro(tesoroTemporal.getX(), tesoroTemporal.getY(), 0);
                        } catch (IOException ex) {
                            Logger.getLogger(ModeloServidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            addTesoro(2);
                        } catch (IOException ex) {
                            Logger.getLogger(ModeloServidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

            }

            private void actualizarPosicion(int id) throws IOException {

                int xIni = ((Punto) jugadores.get(id).getSerpiente().getFirst()).getX();
                int yIni = ((Punto) jugadores.get(id).getSerpiente().getFirst()).getY();
                int xFin = ((Punto) jugadores.get(id).getSerpiente().getLast()).getX();
                int yFin = ((Punto) jugadores.get(id).getSerpiente().getLast()).getY();

                LinkedList ll = (LinkedList) jugadores.get(id).getSerpiente().clone();
                ll.removeFirst();

                //Choca contra si mismo
                if (ll.contains(jugadores.get(id).getSerpiente().getFirst())) {
                    gameOver();
                    //Choca contra otros jugadores
                } else if (chocaContraJugador(id)) {
                    gameOver();
                } else {
                    jugadores.get(id).getSerpiente().removeLast();

                    int dir = jugadores.get(id).getDireccion();

                    switch (dir) {
                        case 0:
                            if (yIni > 0) {
                                yIni--;
                            } else {
                                gameOver();
                            }
                            break;
                        case 1:
                            if (xIni > 0) {
                                xIni--;
                            } else {
                                gameOver();
                            }
                            break;
                        case 2:
                            if (yIni < tableroY) {
                                yIni++;
                            } else {
                                gameOver();
                            }
                            break;
                        case 3:
                            if (xFin < tableroX) {
                                xIni++;
                            } else {
                                gameOver();
                            }
                            break;
                    }

                    jugadores.get(id).getSerpiente().addFirst(new Punto(xIni, xFin));
                    posicionToJugadores(jugadores.get(id).getIdCliente(), xIni, yIni, xFin, yFin);
                }

            }

            private boolean chocaContraJugador(int id) {
                boolean choca = false;

                for (Jugador j : jugadores) {
                    if (j.getIdCliente() != id && !choca) {
                        if (j.getSerpiente().contains(jugadores.get(id).getSerpiente().getFirst())) {
                            choca = true;
                        }
                    }
                }
                return choca;
            }

            private void isTesoroComido(int id) throws IOException {
                if (tesoro.equals((Punto) jugadores.get(id).getSerpiente().getFirst())) {
                    tesoroComido(1, id);
                }
                if (tesoroTemporal.equals((Punto) jugadores.get(id).getSerpiente().getFirst())) {
                    tesoroComido(2, id);
                }

            }
        };
    }
}