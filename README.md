
# Airport CLI

## Overview

A Java-based Airport CLI application that allows users to interact with an Airport API:

- View airports located in each city  
- View aircrafts flown by each passenger  
- View airports aircraft take off from and land at  
- View airports used by each passenger  
- Includes input validation for reliable user experience  
- Features automated tests with mocking using JUnit and Mockito 


## How to Run

**This CLI requires the Airport API to be running locally. https://github.com/NickPow/Sem4API**

1. Make sure the Airport API server is running on `http://localhost:8080`
   - See the API repository for setup instructions
2. Run the CLI application from the project root:

```bash
mvn exec:java -Dexec.mainClass="com.example.airportcli.App"

```


