@echo off
echo ========================================
echo   ????????
echo ========================================
echo.

echo [1/3] ??????...
mysql -uroot -p205318 < zijin-server\src\main\resources\sql\init.sql
echo ????????!
echo.

echo [2/3] ??????...
start "????-??" cmd /c "cd zijin-server && mvn spring-boot:run"
echo ????? (?? 8088)...
echo.

echo [3/3] ??????...
cd zijin-web
call npm install
start "????-??" cmd /c "npm run dev"
echo.

echo ========================================
echo   ????!
echo   ??: http://localhost:8088
echo   ??: http://localhost:5173
echo ========================================
pause
