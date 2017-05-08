package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.controller.ClientInterface;

public interface ServerInterface extends Remote{
	
	public void registerClient(ClientInterface client) throws RemoteException;
	
	public void sendChatMessage(String message) throws RemoteException;
	
	public void startGame(ClientInterface client, String message) throws RemoteException;
	
	public void flipPiece(int index) throws RemoteException;
	
	public void validatePiece(int index) throws RemoteException;
	
	public void surrender(ClientInterface client) throws RemoteException;
	
	public boolean restartValidation(ClientInterface client) throws RemoteException;
	
	public void restartGame() throws RemoteException;
	
	public void rollDices(ClientInterface client, int value) throws RemoteException;
	
	public void endTurn(ClientInterface client, int value) throws RemoteException;
	
	public void finishGame() throws RemoteException;
	
	public int getClientSize() throws RemoteException;
	
	public boolean getGameStatus() throws RemoteException;

}
