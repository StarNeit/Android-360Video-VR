/**
 * 
 */
package com.degree.hitaishin.model;

/**
 * @author sachin
 *
 */
public class Cities {

	String city;
	String city_id;
	public Cities(String city, String city_id) {
		super();
		this.city = city;
		this.city_id = city_id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
}
