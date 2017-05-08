package client.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    public void receiveChatMessage(String message) throws RemoteException;
    
    public void flipGamePiece(int index) throws RemoteException;
    
    public void validateGamePiece(int index) throws RemoteException;
    
    public void surrenderGame() throws RemoteException;
    
    public boolean restartValidation() throws RemoteException;
    
    public void startGame(String message) throws RemoteException;
    
    public void restartGame() throws RemoteException;

    public void rollDices(int value) throws RemoteException;
    
    public void nextTurn(int value) throws RemoteException;
    
    public void finishGame() throws RemoteException;
    
    public boolean getTurnStatus() throws RemoteException;
    
}
