@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo   紫金学院管理系统 - 启动脚本
echo ========================================
echo.

REM === Step 1: Database ===
echo [1/3] 初始化数据库...
mysql -uroot -p205318 -e "DROP DATABASE IF EXISTS zijin; CREATE DATABASE zijin DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>nul
mysql -uroot -p205318 --default-character-set=utf8mb4 zijin < "%~dp0zijin-server\src\main\resources\sql\init.sql" 2>nul
if %errorlevel% equ 0 (echo 数据库初始化完成!) else (echo 数据库初始化失败，请检查MySQL是否运行)
echo.

REM === Step 2: Backend ===
echo [2/3] 编译并启动后端服务 (端口 8088)...
set "MAVEN_HOME=%USERPROFILE%\apache-maven-3.9.9\apache-maven-3.9.9"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

cd /d "%~dp0zijin-server"
echo 正在编译项目...
call mvn clean package -DskipTests -q 2>nul
if %errorlevel% neq 0 (
    echo 后端编译失败！请检查JDK和Maven配置
    cd /d "%~dp0"
    pause
    exit /b 1
)
echo 编译成功，正在启动...

start "紫金学院-后端" cmd /c "chcp 65001 >nul && title 紫金学院-后端 && java --enable-native-access=ALL-UNNAMED -jar target\zijin-server-1.0.0.jar"
cd /d "%~dp0"
echo 后端正在启动，约10秒后就绪...
echo.

REM === Step 3: Frontend ===
echo [3/3] 启动前端服务 (端口 5173)...
cd /d "%~dp0zijin-web"
call npm install --silent 2>nul
start "紫金学院-前端" cmd /c "chcp 65001 >nul && title 紫金学院-前端 && npm run dev"
cd /d "%~dp0"
echo.

echo ========================================
echo   启动完成!
echo   地址: http://localhost:5173
echo   测试账号: admin / 123456
echo ========================================
echo.
echo 请等待后端窗口出现 Started 字样后刷新浏览器
echo 按任意键关闭此窗口...
pause >nul