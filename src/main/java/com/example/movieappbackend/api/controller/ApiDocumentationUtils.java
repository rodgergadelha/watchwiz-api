package com.example.movieappbackend.api.controller;

public class ApiDocumentationUtils {
	
	public static final String SERVICES_PARAM_DESCRIPTION = "2 letter ISO 3166-1 alpha-2 country code of the country to search in. See the about page to check the supported countries.\r\n"
			+ "services\r\n"
			+ "\r\n"
			+ "A comma separated list of up to 4 services to search in. See /services endpoint to get the supported services and their ids/names.\r\n"
			+ "\r\n"
			+ "Syntax of the values supplied in the list can be as the followings:\r\n"
			+ "\r\n"
			+ "<sevice_id>: Searches in the entire catalog of that service, including (if applicable) rentable, buyable shows or shows available through addons i.e. netflix, prime, apple\r\n"
			+ "\r\n"
			+ "<sevice_id>.<offer_type>: Only returns the shows that are available in that service with the given offer type. Valid offer type values are subscription, free, rent, buy and addon i.e. peacock.free only returns the shows on Peacock that are free to watch, prime.subscription only returns the shows on Prime Video that are available to watch with a Prime subscription. hulu.addon only returns the shows on Hulu that are available via an addon, prime.rent only returns the shows on Prime Video that are rentable.\r\n"
			+ "\r\n"
			+ "<sevice_id>.addon.<addon_id>: Only returns the shows that are available in that service with the given addon. Check /v2/services endpoint to fetch the available addons for a service. Some sample values are: hulu.addon.hbo, prime.addon.hbomaxus.\r\n"
			+ "\r\n"
			+ "If a service supports both free and subscription, then results included under subscription will always include the free shows as well.\r\n"
			+ "\r\n"
			+ "When multiple values are passed as a comma separated list, any show that satisfies at least one of the values will be included in the result.\r\n"
			+ "\r\n"
			+ "Some sample list values:\r\n"
			+ "\r\n"
			+ "prime.rent,prime.buy,apple.rent,apple.buy: Returns all the buyable/rentable shows on Amazon Prime Video and Apple TV.\r\n"
			+ "\r\n"
			+ "prime.addon,prime.subscription: Returns all the shows on Amazon Prime Video that are available through either a Prime Video subscription or a Prime Video Channel subscription.\r\n"
			+ "\r\n"
			+ "";
	
	public static final String OUTPUT_LANGUAGE_PARAM_DESCRIPTION = "2 letter iso code of the output language. Default is en.";
	
	public static final String SHOW_TYPE_PARAM_DESCRIPTION = "Type of shows to search in. Accepted values are movie, series or all. The default value is all.";
	
	public static final String 	GENRE_PARAM_DESCRIPTION = "A genre id to only search within the shows in that genre. See /genres endpoint to see available genres and ids.";
	
	public static final String 	SHOW_ORIGINAL_LANGUAGE_PARAM_DESCRIPTION = "A 2 letter ISO 639-1 language code to only search within the shows whose original language matches with the provided language.";

	public static final String CURSOR_PARAM_DESCRIPTION = "Cursor is used for pagination. After each request, the response includes a hasMore boolean field to tell if there are more results that did not fit the returned list size. If it is set as true, to get the rest of the result set, send a new request (with the same parameters for other fields such as show_type, services etc.), and set the cursor parameter as the nextCursor value of the previous request response. Do not forget to escape the cursor value before putting it into the query as it might contain characters such as ?, &.\r\n"
			+ "\r\n"
			+ "The first request naturally does not require a cursor parameter.";
	
	public static final String 	KEYWORD_LANGUAGE_PARAM_DESCRIPTION = "A keyword to only search within the shows have that keyword in their overview or title.";
	
	public static final String COUNTRY_PARAM_DESCRIPTION = "2 letter ISO 3166-1 alpha-2 country code of the country to search in.";
	
}
