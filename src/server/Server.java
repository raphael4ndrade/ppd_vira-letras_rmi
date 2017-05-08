package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.controller.ClientInterface;

public class Server extends UnicastRemoteObject implements ServerInterface{
	
	private static final long serialVersionUID = 1L;
	
	private List<ClientInterface> listClients;
	
	public Server() throws RemoteException{
		this.listClients = new ArrayList<ClientInterface>();
	}
	
	@Override
	public synchronized int getClientSize(){
		return listClients.size();
	}

	@Override
	public synchronized void registerClient(ClientInterface client) throws RemoteException {
		if(listClients.size() < 2){
			listClients.add(client);
			System.out.println("Cliente registrado! Quantidade: " + listClients.size());
		} else
			System.out.println("Capacidade máxima atingida... tente mais tarde.");
	}

	@Override
	public synchronized void sendChatMessage(String message) throws RemoteException {
		for (ClientInterface c : listClients) {
			c.receiveChatMessage(message);
		}
	}

	@Override
	public synchronized void startGame(ClientInterface client, String message) throws RemoteException {
		for (ClientInterface c : listClients) {
			if(!c.equals(client))
				c.startGame(message);
		}
	}

	@Override
	public synchronized void flipPiece(int index) throws RemoteException {
		for (ClientInterface c : listClients) {
			c.flipGamePiece(index);
		}
	}

	@Override
	public synchronized void validatePiece(int index) throws RemoteException {
		for (ClientInterface c : listClients) {
			c.validateGamePiece(index);
		}
	}

	@Override
	public synchronized void surrender(ClientInterface client) throws RemoteException {
		for (ClientInterface c : listClients) {
			if(!c.equals(client))
				c.surrenderGame();
		}
	}

	@Override
	public synchronized boolean getGameStatus() throws RemoteException {
		boolean result = false;
		for (ClientInterface c : listClients) {
			if(c.getTurnStatus() == false)
				result = true;
		}
		return result;
	}

	@Override
	public synchronized void rollDices(ClientInterface client, int value) throws RemoteException {
		for (ClientInterface c : listClients) {
			if(!c.equals(client))
				c.rollDices(value);
		}
	}
	
	@Override
	public synchronized void endTurn(ClientInterface client, int value) throws RemoteException {
		for (ClientInterface c : listClients) {
			if(!c.equals(client))
				c.nextTurn(value);
		}
	}

	@Override
	public synchronized boolean restartValidation(ClientInterface client) throws RemoteException {
		boolean result = false;
		for (ClientInterface c : listClients) {
			if(!c.equals(client)){
				result = c.restartValidation();
			}
		}
		return result;
	}

	@Override
	public synchronized void restartGame() throws RemoteException {
		for (ClientInterface c : listClients) {
			c.restartGame();
		}
	}
	
	@Override
	public synchronized void finishGame() throws RemoteException {
		for (ClientInterface c : listClients) {
			c.finishGame();
		}
	}
	
	public static void main(String args[]) {
		try{
			System.out.println("Iniciando o servidor de nomes...");
			Registry registry = LocateRegistry.createRegistry(1099);
			ServerInterface skeleton = (ServerInterface) new Server();
			registry.bind("ViraLetrasServer", skeleton);
			System.out.println("Sucesso!");
		} catch(Exception e){
			
		}
	}

}
