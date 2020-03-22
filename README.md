# URL Shortener

This is a project repository, with the purpose of create a short version of a given URL.  

## Endpoints

#### /api/shortener  

The main endpoint. It accepts only **POST** requests.  
&nbsp;
&nbsp;  
The `url` parameter sends the URL you want to shorten.  
The `short` parameter sets the short alias that your URL will be recognized by.
```
localhost:9999/api/shortener?url=http://github.com/joaogabrielzo&short=my-github  

{
    "oldUrl": "http://github.com/joaogabrielzo",
    "shortUrl": "http://localhost:9999/goto/my-github"
}
```  
&nbsp;  
Without the `short` parameter, the API will return a sliced SHA1 hash code as the URL alias.  
```
localhost:9999/api/shortener?url=http://github.com/joaogabrielzo  

{
    "oldUrl": "http://github.com/joaogabrielzo",
    "shortUrl": "http://localhost:9999/short/f21d9820"
}
```  
&nbsp;  
&nbsp;  
#### /api/goto/

This endpoint only accepts **GET** requests.  
It will redirect you to the webpage according to the alias given after it.

```
localhost:9999/api/goto/my-github
```
&nbsp;  
&nbsp;  
## Usage  

I still wasn't able to dockerize this application, so for the basic usage you'll gonna need:  
- [sbt](https://www.scala-sbt.org/)
- [Redis](https://redis.io/)  
&nbsp;  
With both installed, you need to open the application folder and run `sbt run` in your terminal.
