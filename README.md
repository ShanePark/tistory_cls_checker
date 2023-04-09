# Tistory blog CLS Checker

## 1. Make reports

### Sequential

it's slower but not stress the server.  
If you need to check all metrics, it's recommended

```bash
npm install -g lighthouse
chmod +x lighthouse_batch.sh
./lighthouse_batch.sh https://shanepark.tistory.com 466
```

### Parallel

it's faster, but maybe affect server performance.  
But if you only need to check CLS, it's recommended

```bash
npm install -g lighthouse
chmod +x lighthouse_batch_parallel.sh
./lighthouse_batch_parallel.sh https://shanepark.tistory.com 466
```

## 2. check results

```bash
./gradle clean build
java -jar build/libs/tistory-blog-cls-checker-1.0-SNAPSHOT.jar \
  --input=./results \
  --output=./bad-cls-list \
  --cls=0.03
```
