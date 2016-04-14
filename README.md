# SimplyRested
A simple REST CLI in Java, with no external dependencies. 

Meant to provide a simple interface, to make multiple REST calls especially during development/testing without having to set the same headers over and over again.

### Getting Started
Download or copy the source. Compile it. Then go to the compile output directory. Run this:
```
java -cp . com.rajesuwerps.SimplyRested
```

### Usage Options:

#### To set base url: 
```
url <base-url>
```
Example: 
```
url http://api.example.com/
```
or just `url api.example.com` without `http://`

Need to be set only once. Need to set before calling endpoint -see below.
Multiple calls ok. Last value takes effect.

####  To set header(s): 
```
header <key> <value>
```
Example: 
```
header Accept application/json
header Authorization DKJ36EI72429238400792DJ3290834
```
Call once per header. Can be called anytime. Takes effect for subsequent calls.                   
Also, see shortcuts below.                                                                        

#### To remove header(s):
```
header <key> <no-value>
```
Example:
```
header Accept
```
Call once per header. Can be called anytime. Takes effect for subsequent calls.

#### To call service endpoint: 
```
/<end-point>
```
Example:
```
/myresource                                                                             
```
Preceding '/' needed.                                                                             

#### You can also use these shortcuts

* Shortcut to set JSON:
```
json
```
Same as: header Accept application/json                                                           

* Shortcut to set XML:                                                                              
```
xml
```
Same as: header Accept application/xml                                                            

* Shortcut to set Authorization:                                                                    
```
auth <value>
```
Same as: header Authorization <value>

* You can also call url directly, without first having to sett the base url. Example:
```
http://api.example.com/myresource
```

* Currently supports HTTP Only, GET Only

To Exit: Ctrl+D or Ctrl+C

### Output
Output will be something like this:
```
>> url api.example.com
>> [Set]
>> 
>> json
>> [Set]
>> 
>> /categories
>> [200]
>> [{"id":1,"label":"cat1","title":"cat1 title"},{"id":2,"label":"cat1","title":"cat2 title"},{"id":3,"label":"cat3","title":"cat3 title"}]
>> 
<Ctrl+D>
>> [Bye now]

```

