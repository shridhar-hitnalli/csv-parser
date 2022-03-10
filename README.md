CSV-Parser REST API

## Built With

* Java 11 - The programming language used
* Micronaut 3.3.4 - The web framework used
* Lombok - Lombok is used to reduce boilerplate code for model/data objects,
* Maven 3.x - Dependency Management

## Steps to run the application

**1. Clone the repository**

```bash
git clone https://github.com/shridhar-hitnalli/csv-parser.git
```
**2. To run the application using maven**
```bash
./mvnw mn:run
```
The app will start running at <http://localhost:8080>

**4. To run the tests**
```bash
 ./mvnw clean test
```

## Rest APIs

The app defines following API.

### CSV-Parser

| Method | Url             | Decription                                                                               | Sample Valid Request Body | Sample Valid Output file |
|--------|-----------------|------------------------------------------------------------------------------------------|---------------------------|------------------------- |
| POST   | /api/csv-parser | Reads the csv file at the given path and <br/>generate output text file in a json format | [JSON](#jsoncreate)       | [1_data_json.txt](#jsonoutput)      |


Test it using postman or any other rest client.

You can find a csv file to test at ```/src/test/resources/data.csv```.

The API will create a text file in a json format inside a directory ```/src/test/resources/csv_parser_output```.

## Sample Valid JSON Request

##### <a id="jsoncreate"> /api/csv-parser > request</a>
```json
{
  "path" : "<code-repository-location>/csv-parser/src/test/resources/data.csv"
}
```

## Sample Valid text file format
For the above request the output file will be created at```<code-repository-location>/csv-parser/src/test/resources/csv_parser_output/1_data_json.txt```
##### <a id="jsonoutput"> Output > 1_data_json.txt</a>
```json
{
  "record" : [ {
    "fieldName" : "name",
    "value" : "Josephine",
    "datatype" : "STRING"
  }, {
    "fieldName" : "email",
    "value" : "osephine_darakjy@darakjy.org",
    "datatype" : "EMAIL"
  }, {
    "fieldName" : "phone",
    "value" : "810-374-9840",
    "datatype" : "PHONE_NUMBER"
  }, {
    "fieldName" : "zip",
    "value" : "48116",
    "datatype" : "NUMBER"
  } ]
}

```
