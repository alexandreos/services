EncomendaZ RESTful Web Services
=============================

Uma alternativa para acessar os serviços do EncomendaZ via RESTful Web Services.

Rastreamento
------------

Este serviço expõe as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Consulta

O serviço de rastreamento está disponível nesta URL:

http://rest.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6

Os parâmetros "start" e "end" são opcionais, mas o "id" é obrigatório.

Para resquisições JSONP, use esta URL:

http://rest.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6&callback=myJSONPCallback

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar, experimente diversos códigos de rastreio e registre os bugs aqui:

https://github.com/encomendaz/rest/issues

Se preferir submeter código-fonte, faça um fork e envie seu pull request.

Este serviço é de graça!