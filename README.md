# spring-cloud-blabla
spring cloud+protostuff

<h3>Key point</h3>
<strong>1) As protostuff not support List, HashMap that used the result of method, so</strong>

   public class ProtoWrapper
   <br>{<br>
      // your object want to encode/decode<br>
      <tab>public Object data;</tab><br>
   }<br>
<strong>2) Must extends the class AbstractGenericHttpMessageConverter, not AbstractHttpMessageConverter</strong>

