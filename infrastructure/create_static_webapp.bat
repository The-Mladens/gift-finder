@echo off
SET RESOURCE_GROUP=the-mladens-gift-finder
SET LOCATION=westeurope
SET APP_NAME=the-mladens-gift-finder
SET GITHUB_REPO=https://github.com/The-Mladens/gift-finder

REM az login --tenant 13a3cd93-9724-47f8-a7d7-c467004b3519 --subscription ecb983df-e8c1-496e-bf7b-7fd270c62f1e
REM az login --tenant 13a3cd93-9724-47f8-a7d7-c467004b3519

echo Creating resource group...
REM az group create --name %RESOURCE_GROUP% --location %LOCATION%

echo Creating Azure Static Web App...
az staticwebapp create ^
  --name %APP_NAME% ^
  --resource-group %RESOURCE_GROUP% ^
  --location %LOCATION% ^
  --source %GITHUB_REPO% ^
  --branch main ^
  --app-location "frontend" ^
  --output-location "" ^
  --login-with-github

echo Azure Static Web App created successfully.
pause
