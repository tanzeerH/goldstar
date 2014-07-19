package com.goldstar.model;

public class Contact {
	private String name;
	private long contact_Id;
	private String email;
	
	public Contact(String name,long contactId,String email)
	{
		this.name=name;
		this.contact_Id=contactId;
		this.email=email;
		
	}
	public String getName(){
		return this.name;
		
	}
	public long getContactId(){
		return this.contact_Id;
		
	}
	public String getEmail(){
		return this.email;
		
	}

}
