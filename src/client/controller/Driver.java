package client.controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import server.ServerInterface;
import client.model.ViraLetrasModel;
import client.view.ViraLetrasView;


public class Driver {
	
	public static void main(String[] args) {
		ViraLetrasView view = new ViraLetrasView();
		ViraLetrasModel model = new ViraLetrasModel();

		try {
			ServerInterface s = (ServerInterface)Naming.lookup("//localhost/ViraLetrasServer");

			ClientInterface controller = new ViraLetrasController(view, model, s);
			
			s.registerClient(controller);
			
			view.setVisible(true);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
