# Registro Unico de Entidades comunes

Descripción de la aplicación:
-----------------------------

Esta aplicación gestiona un registro único de entidades utilizadas por todos los subsistemas del SACVeFor y los que se requieran del SACVeBio, tanto de orden nacional como locales. Las entidades en cuestión son personas (físicas y jurídicas) y vehículos de transporte. Los usuarios de esta aplicación serán administradores de datos comunes, del ámbito nacional y accederán mediante el panel de gestión de aplicaciones de datos comunes.


Ambiente y dependencias:
------------------------

Dado que esta aplicación provée una API REST de servicios al resto de las aplicaciones, requiere como dependencia, al momento de generar el .war, el paquete con las clases serializables de intercambio: paqRue v1.1.0  
Por otro lado, dado que requiere los servicios de datos territoriales, para su correcto funcionamiento es condición necesaria que la aplicación gestionTerritorial (scvf-terr v1.1.1) esté desplegada y accesible.


Configuraciones:
----------------

Dado que todas las dependencias de las aplicaciones desarrolladas en java están gestionadas por Maven, una vez levantado el proyecto, deberá actualizarse todas las dependencias mediante el comando respectivo del IDE para que pueda compilar sin inconvenientes.

Deberá crearse en el directorio `sacvefor-registroUnicoDeEntidades/src/main/resources/META-INF/` el archivo `persistence.xml` replicando el contenido del archivo `persistence.dist.xml` para gestionar la unidad de persistencia de datos.

Deberá crearse en el directorio `sacvefor-registroUnicoDeEntidades/src/main/resources/` el archivo `Config.properties` replicando el contenido del archivo `Config.properties.dist`, si el puerto del servidor fuera otro alternativo al 8080, deberá modificarse la línea correspondiente en la variable `Server` y si el servicio de datos territoriales (scvf-terr) se alojara en otro ambiente o esté accesible desde otro puerto, deberá modificarse la línea correspondiente en la variable `ServerServicios`.


Datos:
------

Deberá crearse en el servidor de base de datos la base `sacvefor_rue` con los parámetros de creación por defecto. Deberá restaurarse luego con el 
backup remitido por la coordinación del proyecto.

Se deberá crear en el servidor de aplicaciones el recurso `Datasource` con 
los siguientes parámetros (los que no se indiquen quedarán por defecto):  
Nombre: `Svf_rueDS`  
JNDI:  `java:/Svf_rueDS`  
Driver: `postgresql`  
Connection URL: `la que corresponda a la configuración local`  
Usuario y password de acceso: `los que correspondan a la configuración local`

Nota: El driver deberá estar previamente registrado en el servidor de aplciaciones.


Descripción de la API de servicios:
-----------------------------------

La aplicación cuenta con una API de servicios REST que permiten tanto consulta y selección de las entidades en cuestión como su registro y edición. Los usuarios con acceso a estos servicios serán los diferentes subsistemas del SACVeFor y SACVeBio, previamente registrados en la aplicación.

