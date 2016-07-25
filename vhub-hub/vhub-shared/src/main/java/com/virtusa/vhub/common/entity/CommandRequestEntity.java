package com.virtusa.vhub.common.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "commandRequest")
public class CommandRequestEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String command;
	private int id;
	private String type;

	@XmlElement
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@XmlElement
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
