package com.partior.httpclient.config;


import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.function.Supplier;

@Configuration
public class RestConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.requestFactory(myRequestFactorySupplier()).build();
	}

	@Bean
	public MyRequestFactorySupplier myRequestFactorySupplier() {
		return new MyRequestFactorySupplier();
	}

	class MyRequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {

		@Override
		public ClientHttpRequestFactory get() {

			HttpComponentsClientHttpRequestFactory requestFactory = null;

			try {
			    SSLConnectionSocketFactory socketFactory =  fromJksFile();
				CloseableHttpClient httpClient = HttpClientBuilder.create()
						.setSSLSocketFactory(socketFactory).build();

				requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
				requestFactory.setBufferRequestBody(false);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return requestFactory;
		}

	}


	private SSLConnectionSocketFactory fromJksFile() throws Exception {

		String serverCert = "cert/certificate_with_ccf.jks";
		String clientCert = "cert/javaclient_with_ccf.jks";

		char[] password = "changeit".toCharArray();

		KeyStore ks = KeyStore.getInstance("JKS");

		InputStream ksStream = this.getClass().getClassLoader()
				.getResourceAsStream(clientCert);

		ks.load(ksStream, password);

		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(this.getClass().getClassLoader()
						.getResource( serverCert ), password)
				.loadKeyMaterial(ks, password).build();

	 return  new SSLConnectionSocketFactory(sslContext,
				NoopHostnameVerifier.INSTANCE);

	}

}
