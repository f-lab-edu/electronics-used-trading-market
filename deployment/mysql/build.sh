docker-compose down -v
rm -rf ./master/data/*
rm -rf ./slave/data/*
docker-compose build
docker-compose up -d

until docker exec mysql_master sh -c 'export MYSQL_PWD=#password; mysql -u root -e ";"'
do
    echo "Master DB 접속중..."
    sleep 10
done

priv_stmt='CREATE USER "mydb_slave_user"@"%" IDENTIFIED BY "mydb_slave_pwd"; GRANT REPLICATION SLAVE ON *.* TO "mydb_slave_user"@"%"; FLUSH PRIVILEGES;'

docker exec mysql_master sh -c "export MYSQL_PWD=#password; mysql -u root -e '$priv_stmt'"

until docker-compose exec mysql_slave sh -c 'export MYSQL_PWD=#password; mysql -u root -e ";"'
do
    echo "Slave DB 접속중..."
    sleep 10
done

MS_STATUS=`docker exec mysql_master sh -c 'export MYSQL_PWD=#password; mysql -u root -e "SHOW MASTER STATUS"'`
CURRENT_LOG=`echo $MS_STATUS | awk '{print $6}'`
CURRENT_POS=`echo $MS_STATUS | awk '{print $7}'`

start_slave_stmt="CHANGE MASTER TO MASTER_HOST='mysql_master',MASTER_USER='root',MASTER_PASSWORD='#password',MASTER_LOG_FILE='$CURRENT_LOG',MASTER_LOG_POS=$CURRENT_POS,GET_MASTER_PUBLIC_KEY=1; START SLAVE;"
start_slave_cmd='export MYSQL_PWD=#password; mysql -u root -e "'
start_slave_cmd+="$start_slave_stmt"
start_slave_cmd+='"'

docker exec mysql_slave sh -c "$start_slave_cmd"

echo "테이블 생성 대기중"
sleep 30
docker exec mysql_master sh -c "export MYSQL_PWD=#password; mysql -u root -e 'CREATE DATABASE mymarket'"
sleep 1
docker exec -i mysql_master sh -c "mysql -u root -p#password mymarket < /app/mymarket.sql"
echo "테이블 생성 완료"
sleep 1
docker exec mysql_slave sh -c "export MYSQL_PWD=#password; mysql -u root -e 'SHOW SLAVE STATUS \G'"