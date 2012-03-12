EncomendaZ RESTful Web Services
=============================

Uma alternativa para acessar os servi�os do EncomendaZ via RESTful Web Services.

Rastreamento
------------

Este servi�o exp�e as informa��es contidas nas p�ginas HTML dos Correios. O parse � feito com aux�lio do projeto Alfred Library (http://alfredlibrary.org).  

### Consulta

O servi�o de rastreamento est� dispon�vel nesta URL:

http://rest.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6

Os par�metros "start" e "end" s�o opcionais, mas o "id" � obrigat�rio.

Para requisi��es JSONP, use esta URL:

http://rest.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6&callback=myJSONPCallback

Contribui��o
--------------

O projeto est� de portas abertas para contribui��o. Se quiser ajudar fa�a um fork, incremente seu c�digo e fa�a um pull request.

Este servi�o � de gra�a!