### Postgres:
```
docker run --name sbdemoapp -e POSTGRES_PASSWORD=mysecret -e POSTGRES_DB=sbdemoapp -p 5432:5432 -d postgres
docker container start sbdemoapp
```

### Database

Using postgresql as database.

## Useful docker commands

### Remove container
```
docker stop sbdemoapp
docker rm sbdemoapp
```

### Ssh into running container

```
docker ps
docker exec -it <running_container_name_or_id> /bin/bash
```

## Import/Export DB

### Data export with pg_dump

```
pg_dump sbdemoapp --host localhost --user postgres > sbdemoapp.sql
```

### Importing data from SQL files

```
psql sbdemoapp < sbdemoapp.sql
```


## Links

- https://github.com/bezkoder/spring-boot-refresh-token-jwt
- 