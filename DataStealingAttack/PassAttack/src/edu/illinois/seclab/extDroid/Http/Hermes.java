package edu.illinois.seclab.extDroid.Http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Hermes (/ˈhɜrmiːz/; Greek : Ἑρμῆς) was an Olympian god in Greek religion and mythology, 
 * son of Zeus and the Pleiad Maia. He was second youngest of the Olympian gods.
 *
 *Hermes was a god of transitions and boundaries. 
 *He was quick and cunning, and moved freely between the worlds of the mortal and divine, 
 *as emissary and messenger of the gods,[1] intercessor between mortals and the divine, and 
 *conductor of souls into the afterlife. He was protector and patron of travelers, herdsmen, thieves,[2] 
 *orators and wit, literature and poets, athletics and sports, invention and trade.[3] In some myths he 
 *is a trickster, and outwits other gods for his own satisfaction or the sake of humankind. [wikipedia]
 * @author soteris demetriou
 *
 */
public class Hermes {
	HttpClient httpClient;
	
	public Hermes(){
		httpClient = new DefaultHttpClient();
	}
	
	public HttpResponse send_post(List<NameValuePair> data_list, String server_url){
		HttpPost httpost = new HttpPost(server_url);
		try {
			httpost.setEntity(new UrlEncodedFormEntity(data_list));
			
			//exec
			HttpResponse response = httpClient.execute(httpost);
			
			return response;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}

