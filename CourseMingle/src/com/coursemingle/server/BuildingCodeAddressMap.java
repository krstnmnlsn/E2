package com.coursemingle.server;

import java.util.Hashtable;
import java.util.Map;

public class BuildingCodeAddressMap{
	
	private final static BuildingCodeAddressMap INSTANCE = new BuildingCodeAddressMap();
	private Map<String,String> database;
	
	public static BuildingCodeAddressMap getDefaultInstance(){
		return INSTANCE;
	}
	
	public String getAddress(String code){
		return getDatabase().get(code);
	}
	
	private Map<String,String> getDatabase(){
		return database;
	}
	
	private BuildingCodeAddressMap(){
		database = new Hashtable<String,String>();
		database.put("ACU","2211 Wesbrook Mall");
		database.put("ALRD","1822 East Mall");
		database.put("ANSO","6303 North West Marine Drive");
		database.put("AERL","2202 Main Mall");
		database.put("ACEN","1871 West Mall");
		database.put("AUDI","6344 Memorial Road");
		database.put("AUDX","1924 West Mall");
		database.put("BINN","6373 University Boulevard");
		database.put("BIOL","6270 University Boulevard");
		database.put("BRKX","1874 East Mall");
		database.put("BUCH","1866 Main Mall");
		database.put("BUTO","1873 East Mall");
		database.put("CIRS","2260 West Mall, V6T 1Z4");
		database.put("CHAN","6265 Crescent Road");
		database.put("CHBE","2360 East Mall V6T 1Z3");
		database.put("CHEM","2036 Main Mall");
		database.put("CEME","6250 Applied Science Lane");
		database.put("COPP","2146 Health Sciences Mall");
		database.put("DLAM","2033 Main Mall V6T 1Z2");
		database.put("DSOM","6361 University Blvd");
		database.put("KENN","2136 West Mall");
		database.put("EOSM","6339 Stores Road");
		database.put("ESB","2207 Main Mall");
		database.put("FNH","2205 East Mall");
		database.put("FSC","2424 Main Mall");
		database.put("FORW","6350 Stores Road");
		database.put("LASR","6333 Memorial Road");
		database.put("FRWO","6354 Crescent Road");
		database.put("FRDM","2177 Wesbrook Mall V6T 1Z3");
		database.put("GEOG","1984 West Mall");
		database.put("CUNN","2146 East Mall");
		database.put("HEBB","2045 East Mall");
		database.put("HENN","6224 Agricultural Road");
		database.put("ANGU","2053 Main Mall");
		database.put("DMP","6245 Agronomy Road V6T 1Z4");
		database.put("HM22","109 West Mall");
		database.put("ICCS","2366 Main Mall");
		database.put("IBLC","1961 East Mall V6T 1Z1");
		database.put("MCDN","2199 West Mall");
		database.put("SOWK","2080 West Mall");
		database.put("LSK","6356 Agricultural Road");
		database.put("LSC","2350 Health Sciences Mall");
		database.put("MCLD","2356 Main Mall");
		database.put("MCML","2357 Main Mall");
		database.put("MATH","1984 Mathematics Road");
		database.put("MATX","1986 Mathematics Road");
		database.put("MEDC","2176 Health Sciences Mall");
		database.put("MUSC","6361 Memorial Road");
		database.put("SCRF","2125 Main Mall");
		database.put("PHRM","2405 Wesbrook Mall");
		database.put("PONE","2034 Lower Mall");
		database.put("PONF","2008 Lower Mall");
		database.put("PONH","2008 Lower Mall");
		database.put("OSBO","6108 Thunderbird Boulevard");
		database.put("SPPH","2206 East Mall");
		database.put("SRC","6000 Student Union Blvd");
		database.put("UCLL","6331 Crescent Road V6T 1Z1");
		database.put("TFPB","6358 University Blvd, V6T 1Z4");
		database.put("TFPX","6358 University Blvd, V6T 1Z4");
		database.put("MGYM","6081 University Blvd");
		database.put("WESB","6174 University Boulevard");
		database.put("WMAX","1933 West Mall");
		database.put("SWNG","2175 West Mall V6T 1Z4");
		database.put("WOOD","2194 Health Sciences Mall");
	}
}
