package com.example.rovercontrol;

import java.io.IOException;
import java.io.OutputStream;

import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;

public class MotorDriver {
	
	private IOIO ioio;
	private Uart uart;
	private OutputStream out;
	private int Address;

	/**
	 * 
	 * @param ioioInstance IOIO object providing the UART
	 * @param rxPin required by IOIO API, unused.
	 * @param txPin pin connected to Sabertooth compatible UART input
	 * @param AddressIn Address of Sabertooth compatible device
	 * @throws ConnectionLostException
	 */
	public MotorDriver(IOIO ioioInstance, int rxPin, int txPin, int AddressIn) throws ConnectionLostException
	{
		ioio = ioioInstance;
		uart = ioio.openUart(rxPin, txPin, 19200, Uart.Parity.NONE, Uart.StopBits.ONE);
		out = uart.getOutputStream();
		Address = AddressIn;
		try {
			out.write(170);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //send baud rate detection byte
	}
	
	public void setSpeed(double speed) throws IOException
	{
		int command = 8;
		double speedInput = 0;
		
		if (speed < 0)
		{
			command = 9;
		}
		else
		{
			command = 8;
		}
		
		speedInput = Math.abs(speed) * 127;
		sendPacket(speedInput, command);
	}
	
	public void setRotationSpeed(double rotationalSpeed) throws IOException
	{
		int command = 8;
		double speedInput = 0;
		
		if (rotationalSpeed < 0)
		{
			command = 11;
		}
		else
		{
			command = 10;
		}
		
		speedInput = Math.abs(rotationalSpeed) * 127;
		sendPacket(speedInput, command);
	}
	
	private void sendPacket(double input, int command) throws IOException
	{
		byte checksum = 127;
		
		out.write((byte)Address);
		out.write((byte)command);
		out.write((byte)input);
		out.write(((byte)Address + (byte)0 + (byte)input) & checksum); 
	}
	
}
