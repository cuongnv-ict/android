package com.example.danhba;

import android.widget.ImageView;

public class Contacter {
	private String name;
	private String didong;
	private String dienthoai;
	private String diachi;
	private String email;
	private String tochuc;
	private ImageView image;
	private int id;
	public void SetId(int id)
	{
		this.id=id;
	}
	public int GetId()
	{
		return id;
	}
	public void SetName(String name)
	{
		this.name=name;
	}
	public String GetName()
	{
		return name;
	}
	public void SetDidong(String didong)
	{
		this.didong= didong;
	}
	public String GetDidong()
	{
		return didong;
	}
	public void SetDienthoai(String dienthoai)
	{
		this.dienthoai=dienthoai;
	}
	public String GetDienthoai()
	{
		return dienthoai;
	}
	public void SetDiachi(String diachi)
	{
		this.diachi=diachi;
	}
	public String GetDiaChi()
	{
		return diachi;
	}
	public void SetEmail(String email)
	{
		this.email=email;
	}
	public String GetEmail()
	{
		return email;
	}
	public void SetTochuc(String tochuc)
	{
		this.tochuc= tochuc;
	}
	public String GetTochuc()
	{
		return tochuc;
	}
	public void SetImage(ImageView image)
	{
		this.image = image;
	}
	public ImageView GetImage()
	{
		return image;
	}
}
