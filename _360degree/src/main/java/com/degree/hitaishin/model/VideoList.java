/**
 * 
 */
package com.degree.hitaishin.model;

import java.io.Serializable;

/**
 * @author sachin
 * 
 */
public class VideoList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String video_id;
	String category_name;
	String video_city;
	String video_title;
	String video_description;
	String video_link;
	String upload_date;
	String video_thumbnail;
	String subcategory_name;
	String meta_data;
	String websiteLink;
	String videoType;

	public String getWebsiteLink() {
		return websiteLink;
	}

	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}

	public VideoList(String video_id, String category_name, String video_city,
			String video_title, String video_description, String video_link,
			String upload_date, String video_thumbnail,
			String subcategory_name, String meta_data, String websiteLink,String _videoType) {
		super();
		this.video_id = video_id;
		this.category_name = category_name;
		this.video_city = video_city;
		this.video_title = video_title;
		this.video_description = video_description;
		this.video_link = video_link;
		this.upload_date = upload_date;
		this.video_thumbnail = video_thumbnail;
		this.subcategory_name = subcategory_name;
		this.meta_data = meta_data;
		this.websiteLink = websiteLink;
		this.videoType = _videoType;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getSubcategory_name() {
		return subcategory_name;
	}

	public void setSubcategory_name(String subcategory_name) {
		this.subcategory_name = subcategory_name;
	}

	public String getMeta_data() {
		return meta_data;
	}

	public void setMeta_data(String meta_data) {
		this.meta_data = meta_data;
	}

	/**
	 * 
	 */
	public VideoList() {
		// TODO Auto-generated constructor stub
	}

	public String getVideo_thumbnail() {
		return video_thumbnail;
	}

	public void setVideo_thumbnail(String video_thumbnail) {
		this.video_thumbnail = video_thumbnail;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getVideo_city() {
		return video_city;
	}

	public void setVideo_city(String video_city) {
		this.video_city = video_city;
	}

	public String getVideo_title() {
		return video_title;
	}

	public void setVideo_title(String video_title) {
		this.video_title = video_title;
	}

	public String getVideo_description() {
		return video_description;
	}

	public void setVideo_description(String video_description) {
		this.video_description = video_description;
	}

	public String getVideo_link() {
		return video_link;
	}

	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}

	public String getUpload_date() {
		return upload_date;
	}

	public void setUpload_date(String upload_date) {
		this.upload_date = upload_date;
	}
}
