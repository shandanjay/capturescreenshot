(ns app.core
	(:gen-class)
)
(import 'java.net.URL)
(import 'java.io.OutputStreamWriter)
(import 'java.io.BufferedReader)
(import 'java.io.InputStreamReader)
(import 'java.io.ByteArrayInputStream)

(require '[clojure.xml])
(use '[clojure.java.io :only (as-url)])
(defn -main [ & args ]
	(def msg "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:getHelloWorldAsString xmlns:ns2=\"http://ws.iwa_live.com/\"><url>http://www.youtube.com</url></ns2:getHelloWorldAsString></S:Body></S:Envelope>")
    (try
      (let [url (URL. "http://localhost:9999/ws/hello")
            connection (doto (.openConnection url)
                         (.setConnectTimeout 10000)
                         (.setDoOutput true)
                         (.setDoInput true)
						 (.setRequestProperty "Connection" "keep-alive")                                                  
						 (.setRequestProperty "Connection" "keep-alive")                                                  
						 (.setRequestProperty "SOAPAction" "")                         
                         (.setRequestProperty "Content-Length" (str
                         (count msg)))
                         (.setRequestProperty "Content-Type"
                                              "text/xml; charset=utf-8")
                         (.setRequestMethod "POST"))
            writer (OutputStreamWriter. (.getOutputStream connection))]
       (doto writer 
         (.write msg)
         (.flush)
         (.close))
       ;; must open input stream AFTER posting message
       (let [reader (BufferedReader.
                     (InputStreamReader. (.getInputStream connection)))
             resp-code (.getResponseCode connection)]
         (if (= resp-code 200)
           (let [str-resp (slurp reader)
                 resp (clojure.xml/parse (ByteArrayInputStream.
                                          (.getBytes str-resp)))]
             (.close reader)
             (prn resp)
             resp)
           {:tag :ErrorCode, :attrs nil, :content [resp-code]})))
    (catch Exception e {:tag :CaughtException :attrs nil :content
      [(.toString e)]} (prn e))))
