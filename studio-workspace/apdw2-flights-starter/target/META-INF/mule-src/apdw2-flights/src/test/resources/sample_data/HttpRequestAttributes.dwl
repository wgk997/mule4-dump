%dw 2.0
output application/java
---
{
	clientCertificate: {
		encoded: null,
		publicKey: {
		} as Object {
			class : "java.security.PublicKey"
		},
		'type': "????"
	} as Object {
		class : "java.security.cert.Certificate"
	},
	headers: {
	},
	listenerPath: "????",
	method: "????",
	queryParams: [
		code: "PDX",
		airline: "american"
	],
	queryString: "????",
	relativePath: "????",
	remoteAddress: "????",
	requestPath: "????",
	requestUri: "????",
	scheme: "????",
	uriParams: {
	},
	version: "????"
} as Object {
	class : "org.mule.extension.http.api.HttpRequestAttributes"
}