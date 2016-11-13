/**
 * 
 */
package com.degree.hitaishin.model;

/**
 * @author sachin
 *
 */
public class Catgory {
	String cat_id;
	String catgory_name;
	String subcategory;
	public Catgory(String cat_id, String catgory_name, String subcategory) {
		super();
		this.cat_id = cat_id;
		this.catgory_name = catgory_name;
		this.subcategory = subcategory;
	}
	public String getCat_id() {
		return cat_id;
	}
	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
	public String getCatgory_name() {
		return catgory_name;
	}
	public void setCatgory_name(String catgory_name) {
		this.catgory_name = catgory_name;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
}
