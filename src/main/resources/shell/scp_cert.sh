
scp apps@192.168.1.49:/tmp/user0_cert.pem .
scp apps@192.168.1.49:/tmp/user0_privk.pem .
scp apps@192.168.1.49:/tmp/service_cert.pem .
scp apps@192.168.1.49:/tmp/member0_privk.pem .
scp apps@192.168.1.49:/tmp/member0_enc_pubk.pem .
scp apps@192.168.1.49:/tmp/member0_enc_privk.pem .
scp apps@192.168.1.49:/tmp/member0_cert.pem .

--cacert service_cert.pem
--cert user0_cert.pem
--key user0_privk.pem

keytool -import -trustcacerts -alias root -file service_cert.pem -keystore certificate_with_ccf.jks
cat user0_cert.pem user0_privk.pem > user0_combined.pem

openssl pkcs12 -export -in user0_combined.pem -name test -out user0_combined.p12

keytool -importkeystore -srckeystore user0_combined.p12 -srcstoretype pkcs12 -destkeystore certificate_with_ccf.jks

keytool -importkeystore -srckeystore user0_combined.p12 -srcstoretype pkcs12 -destkeystore javaclient_with_ccf.jks

