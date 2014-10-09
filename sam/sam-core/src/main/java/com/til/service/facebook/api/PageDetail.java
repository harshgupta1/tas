package com.til.service.facebook.api;

public class PageDetail {
	
	private String id;
	private String about;
	private String app_id;
	private Integer checkins;
	private boolean has_added_app = false;
	private boolean is_community_page = false;
	private boolean is_published = false;
	private String category;
	private String name;
// for linkedin	
	private String firstName;
	private String lastName;
	private String headline;
	private String updateKey;
	private String updateUrl;
	private String errorCode;
	private String message;
	//for linkedin	
	private String access_token;
	private String picture;
	private String link;
	private String website;
	private String description;
	private String username;
	private String founded;
	private String mission;
	private String phone;
	private int fan_count = 0;
	boolean can_post = false;
	private int likes = 0;
	private int shares = 0;
	private int friends = 0;
	private int followers = 0;
	private int talking_about_count = 0;
	private Location location;
	private Error error;
	private Parking parking;
	private Hours hours;
	private PaymentOptions payment_options;
	private RestaurantServices restaurant_services;
	private RestaurantSpecialties restaurant_specialties;
	private Cover cover;
	private boolean exists;
	private String[] perms;
	private String url;
	private String type;
	private String title;
	private String site_name;
	private String updated_time;
	private Page[] image;
	private Page[] admins;
	private Page application;
	private String products;
	private Integer were_here_count = 0;
	private Integer new_like_count = 0;
	private BudgetRecs budget_recs;
	private String first_name;
	private String last_name;
	private String birthday;
	private String gender;
	private String email;
	private float timezone;
	private String locale;
	private boolean verified;
	private HomeTown hometown;
	private Work[] work;
	private Education [] education;
	
	private CategoryList [] category_list;
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getBirthday() {
		return birthday;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public float getTimezone() {
		return timezone;
	}

	public void setTimezone(float timezone) {
		this.timezone = timezone;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public HomeTown getHometown() {
		return hometown;
	}

	public void setHometown(HomeTown hometown) {
		this.hometown = hometown;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFounded() {
		return founded;
	}

	public void setFounded(String founded) {
		this.founded = founded;
	}

	public String getMission() {
		return mission;
	}

	public void setMission(String mission) {
		this.mission = mission;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getFan_count() {
		return fan_count;
	}

	public void setFan_count(int fan_count) {
		this.fan_count = fan_count;
	}

	public boolean isCan_post() {
		return can_post;
	}

	public void setCan_post(boolean can_post) {
		this.can_post = can_post;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}
	
	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getTalking_about_count() {
		return talking_about_count;
	}

	public void setTalking_about_count(int talking_about_count) {
		this.talking_about_count = talking_about_count;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public Page[] getImage() {
		return image;
	}

	public void setImage(Page[] image) {
		this.image = image;
	}

	public Page[] getAdmins() {
		return admins;
	}

	public void setAdmins(Page[] admins) {
		this.admins = admins;
	}

	public Page getApplication() {
		return application;
	}

	public void setApplication(Page application) {
		this.application = application;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return "id :" + id + ", category:" + category + ", name:" + name
		+ ", access_token:" + access_token + ", picture:" + picture
		+ ", link:" + link + ", website:" + website + ", description:"
		+ description + ", shares:" + shares + ",likes=" + likes
		+ ", can_post:" + can_post;
	}
	
	public static class Page
	{
		private String id;
		private String name;
		private String url;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	public static class Location
	{
		private String street;
		private String city;
		private String state;
		private String country;
		private String zip;
		
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		
	}
	
	public static class HomeTown
	{
		private String id;
		private String name;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	public static class BudgetRecs
	{
		private BudgetList[] budget_list;
		
		public static class BudgetList
		{
			private ReachInterval reach_interval;
			
			public static class ReachInterval{
				
			}

			public ReachInterval getReach_interval() {
				return reach_interval;
			}

			public void setReach_interval(ReachInterval reach_interval) {
				this.reach_interval = reach_interval;
			}
			
		}

		public BudgetList[] getBudget_list() {
			return budget_list;
		}

		public void setBudget_list(BudgetList[] budget_list) {
			this.budget_list = budget_list;
		}
		
	}
	
	public static class Parking
	{
		private Integer street;
		private Integer lot;
		private Integer valet;
		
		public Integer getStreet() {
			return street;
		}
		public void setStreet(Integer street) {
			this.street = street;
		}
		public Integer getLot() {
			return lot;
		}
		public void setLot(Integer lot) {
			this.lot = lot;
		}
		public Integer getValet() {
			return valet;
		}
		public void setValet(Integer valet) {
			this.valet = valet;
		}
	}
	
	public static class Hours
	{
		private Integer mon_1_open;
		private Integer mon_1_close;
		private Integer tue_1_open;
		private Integer tue_1_close;
		private Integer wed_1_open;
		private Integer wed_1_close;
		private Integer thu_1_open;
        private Integer thu_1_close;
        private Integer fri_1_open;
        private Integer fri_1_close;
        private Integer sat_1_open;
        private Integer sat_1_close;
        private Integer sun_1_open;
        private Integer sun_1_close;
        private Integer mon_2_open;
        private Integer mon_2_close;
        private Integer tue_2_open;
        private Integer tue_2_close;
        private Integer wed_2_open;
        private Integer wed_2_close;
        private Integer thu_2_open;
        private Integer thu_2_close;
        private Integer fri_2_open;
        private Integer fri_2_close;
        private Integer sat_2_open;
        private Integer sat_2_close;
        private Integer sun_2_open;
        private Integer sun_2_close;
        
		public Integer getMon_1_open() {
			return mon_1_open;
		}
		public void setMon_1_open(Integer mon_1_open) {
			this.mon_1_open = mon_1_open;
		}
		public Integer getMon_1_close() {
			return mon_1_close;
		}
		public void setMon_1_close(Integer mon_1_close) {
			this.mon_1_close = mon_1_close;
		}
		public Integer getTue_1_open() {
			return tue_1_open;
		}
		public void setTue_1_open(Integer tue_1_open) {
			this.tue_1_open = tue_1_open;
		}
		public Integer getTue_1_close() {
			return tue_1_close;
		}
		public void setTue_1_close(Integer tue_1_close) {
			this.tue_1_close = tue_1_close;
		}
		public Integer getWed_1_open() {
			return wed_1_open;
		}
		public void setWed_1_open(Integer wed_1_open) {
			this.wed_1_open = wed_1_open;
		}
		public Integer getWed_1_close() {
			return wed_1_close;
		}
		public void setWed_1_close(Integer wed_1_close) {
			this.wed_1_close = wed_1_close;
		}
		public Integer getThu_1_open() {
			return thu_1_open;
		}
		public void setThu_1_open(Integer thu_1_open) {
			this.thu_1_open = thu_1_open;
		}
		public Integer getThu_1_close() {
			return thu_1_close;
		}
		public void setThu_1_close(Integer thu_1_close) {
			this.thu_1_close = thu_1_close;
		}
		public Integer getFri_1_open() {
			return fri_1_open;
		}
		public void setFri_1_open(Integer fri_1_open) {
			this.fri_1_open = fri_1_open;
		}
		public Integer getFri_1_close() {
			return fri_1_close;
		}
		public void setFri_1_close(Integer fri_1_close) {
			this.fri_1_close = fri_1_close;
		}
		public Integer getSat_1_open() {
			return sat_1_open;
		}
		public void setSat_1_open(Integer sat_1_open) {
			this.sat_1_open = sat_1_open;
		}
		public Integer getSat_1_close() {
			return sat_1_close;
		}
		public void setSat_1_close(Integer sat_1_close) {
			this.sat_1_close = sat_1_close;
		}
		public Integer getSun_1_open() {
			return sun_1_open;
		}
		public void setSun_1_open(Integer sun_1_open) {
			this.sun_1_open = sun_1_open;
		}
		public Integer getSun_1_close() {
			return sun_1_close;
		}
		public void setSun_1_close(Integer sun_1_close) {
			this.sun_1_close = sun_1_close;
		}
		public Integer getMon_2_open() {
			return mon_2_open;
		}
		public void setMon_2_open(Integer mon_2_open) {
			this.mon_2_open = mon_2_open;
		}
		public Integer getMon_2_close() {
			return mon_2_close;
		}
		public void setMon_2_close(Integer mon_2_close) {
			this.mon_2_close = mon_2_close;
		}
		public Integer getTue_2_open() {
			return tue_2_open;
		}
		public void setTue_2_open(Integer tue_2_open) {
			this.tue_2_open = tue_2_open;
		}
		public Integer getTue_2_close() {
			return tue_2_close;
		}
		public void setTue_2_close(Integer tue_2_close) {
			this.tue_2_close = tue_2_close;
		}
		public Integer getWed_2_open() {
			return wed_2_open;
		}
		public void setWed_2_open(Integer wed_2_open) {
			this.wed_2_open = wed_2_open;
		}
		public Integer getWed_2_close() {
			return wed_2_close;
		}
		public void setWed_2_close(Integer wed_2_close) {
			this.wed_2_close = wed_2_close;
		}
		public Integer getThu_2_open() {
			return thu_2_open;
		}
		public void setThu_2_open(Integer thu_2_open) {
			this.thu_2_open = thu_2_open;
		}
		public Integer getThu_2_close() {
			return thu_2_close;
		}
		public void setThu_2_close(Integer thu_2_close) {
			this.thu_2_close = thu_2_close;
		}
		public Integer getFri_2_open() {
			return fri_2_open;
		}
		public void setFri_2_open(Integer fri_2_open) {
			this.fri_2_open = fri_2_open;
		}
		public Integer getFri_2_close() {
			return fri_2_close;
		}
		public void setFri_2_close(Integer fri_2_close) {
			this.fri_2_close = fri_2_close;
		}
		public Integer getSat_2_open() {
			return sat_2_open;
		}
		public void setSat_2_open(Integer sat_2_open) {
			this.sat_2_open = sat_2_open;
		}
		public Integer getSat_2_close() {
			return sat_2_close;
		}
		public void setSat_2_close(Integer sat_2_close) {
			this.sat_2_close = sat_2_close;
		}
		public Integer getSun_2_open() {
			return sun_2_open;
		}
		public void setSun_2_open(Integer sun_2_open) {
			this.sun_2_open = sun_2_open;
		}
		public Integer getSun_2_close() {
			return sun_2_close;
		}
		public void setSun_2_close(Integer sun_2_close) {
			this.sun_2_close = sun_2_close;
		}
        
	}
	
	public static class PaymentOptions
	{
		private Integer cash_only;
		private Integer visa;
		private Integer amex;
		private Integer mastercard;
		private Integer discover;
		
		public Integer getCash_only() {
			return cash_only;
		}
		public void setCash_only(Integer cash_only) {
			this.cash_only = cash_only;
		}
		public Integer getVisa() {
			return visa;
		}
		public void setVisa(Integer visa) {
			this.visa = visa;
		}
		public Integer getAmex() {
			return amex;
		}
		public void setAmex(Integer amex) {
			this.amex = amex;
		}
		public Integer getMastercard() {
			return mastercard;
		}
		public void setMastercard(Integer mastercard) {
			this.mastercard = mastercard;
		}
		public Integer getDiscover() {
			return discover;
		}
		public void setDiscover(Integer discover) {
			this.discover = discover;
		}
		
	}
	
	public static class RestaurantServices
	{
		private Integer reserve;
		private Integer walkins;
		private Integer groups;
		private Integer kids;
		private Integer takeout;
		private Integer delivery;
		private Integer catering;
		private Integer waiter;
		private Integer outdoor;
		
		public Integer getReserve() {
			return reserve;
		}
		public void setReserve(Integer reserve) {
			this.reserve = reserve;
		}
		public Integer getWalkins() {
			return walkins;
		}
		public void setWalkins(Integer walkins) {
			this.walkins = walkins;
		}
		public Integer getGroups() {
			return groups;
		}
		public void setGroups(Integer groups) {
			this.groups = groups;
		}
		public Integer getKids() {
			return kids;
		}
		public void setKids(Integer kids) {
			this.kids = kids;
		}
		public Integer getTakeout() {
			return takeout;
		}
		public void setTakeout(Integer takeout) {
			this.takeout = takeout;
		}
		public Integer getDelivery() {
			return delivery;
		}
		public void setDelivery(Integer delivery) {
			this.delivery = delivery;
		}
		public Integer getCatering() {
			return catering;
		}
		public void setCatering(Integer catering) {
			this.catering = catering;
		}
		public Integer getWaiter() {
			return waiter;
		}
		public void setWaiter(Integer waiter) {
			this.waiter = waiter;
		}
		public Integer getOutdoor() {
			return outdoor;
		}
		public void setOutdoor(Integer outdoor) {
			this.outdoor = outdoor;
		}
	}
	
	public static class RestaurantSpecialties
	{
		private Integer breakfast;
		private Integer lunch;
		private Integer dinner;
		private Integer coffee;
		private Integer drinks;
		
		public Integer getBreakfast() {
			return breakfast;
		}
		public void setBreakfast(Integer breakfast) {
			this.breakfast = breakfast;
		}
		public Integer getLunch() {
			return lunch;
		}
		public void setLunch(Integer lunch) {
			this.lunch = lunch;
		}
		public Integer getDinner() {
			return dinner;
		}
		public void setDinner(Integer dinner) {
			this.dinner = dinner;
		}
		public Integer getCoffee() {
			return coffee;
		}
		public void setCoffee(Integer coffee) {
			this.coffee = coffee;
		}
		public Integer getDrinks() {
			return drinks;
		}
		public void setDrinks(Integer drinks) {
			this.drinks = drinks;
		}
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}

	public Hours getHours() {
		return hours;
	}

	public void setHours(Hours hours) {
		this.hours = hours;
	}

	public PaymentOptions getPayment_options() {
		return payment_options;
	}

	public void setPayment_options(PaymentOptions payment_options) {
		this.payment_options = payment_options;
	}

	public RestaurantServices getRestaurant_services() {
		return restaurant_services;
	}

	public void setRestaurant_services(RestaurantServices restaurant_services) {
		this.restaurant_services = restaurant_services;
	}

	public RestaurantSpecialties getRestaurant_specialties() {
		return restaurant_specialties;
	}

	public void setRestaurant_specialties(
			RestaurantSpecialties restaurant_specialties) {
		this.restaurant_specialties = restaurant_specialties;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public String[] getPerms() {
		return perms;
	}

	public void setPerms(String[] perms) {
		this.perms = perms;
	}
	
	public static class Cover
	{
		private String cover_id;
		private String source;
		private Integer offset_y;
		
		/**
		 * @return the cover_id
		 */
		public String getCover_id() {
			return cover_id;
		}
		/**
		 * @param cover_id the cover_id to set
		 */
		public void setCover_id(String cover_id) {
			this.cover_id = cover_id;
		}
		/**
		 * @return the source
		 */
		public String getSource() {
			return source;
		}
		/**
		 * @param source the source to set
		 */
		public void setSource(String source) {
			this.source = source;
		}
		/**
		 * @return the offset_y
		 */
		public Integer getOffset_y() {
			return offset_y;
		}
		/**
		 * @param offset_y the offset_y to set
		 */
		public void setOffset_y(Integer offset_y) {
			this.offset_y = offset_y;
		}
	}

	/**
	 * @return the cover
	 */
	public Cover getCover() {
		return cover;
	}

	/**
	 * @param cover the cover to set
	 */
	public void setCover(Cover cover) {
		this.cover = cover;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public Integer getCheckins() {
		return checkins;
	}

	public void setCheckins(Integer checkins) {
		this.checkins = checkins;
	}

	public boolean isHas_added_app() {
		return has_added_app;
	}

	public void setHas_added_app(boolean has_added_app) {
		this.has_added_app = has_added_app;
	}

	public boolean isIs_community_page() {
		return is_community_page;
	}

	public void setIs_community_page(boolean is_community_page) {
		this.is_community_page = is_community_page;
	}

	public boolean isIs_published() {
		return is_published;
	}

	public void setIs_published(boolean is_published) {
		this.is_published = is_published;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public Integer getWere_here_count() {
		return were_here_count;
	}

	public void setWere_here_count(Integer were_here_count) {
		this.were_here_count = were_here_count;
	}

	public Integer getNew_like_count() {
		return new_like_count;
	}

	public void setNew_like_count(Integer new_like_count) {
		this.new_like_count = new_like_count;
	}
	
	public BudgetRecs getBudget_recs() {
		return budget_recs;
	}

	public void setBudget_recs(BudgetRecs budget_recs) {
		this.budget_recs = budget_recs;
	}

	public Work[] getWork() {
		return work;
	}

	public void setWork(Work[] work) {
		this.work = work;
	}

	public Education[] getEducation() {
		return education;
	}

	public void setEducation(Education[] education) {
		this.education = education;
	}
	public String getUpdateKey() {
		return updateKey;
	}

	public void setUpdateKey(String updateKey) {
		this.updateKey = updateKey;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public static class CategoryList
	{
		private Integer id;
		private String name;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
	}

	public CategoryList[] getCategory_list() {
		return category_list;
	}

	public void setCategory_list(CategoryList[] category_list) {
		this.category_list = category_list;
	}

}
